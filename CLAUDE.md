# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Module Overview

knox-hilt is a Dagger Hilt integration module that provides dependency injection bindings for the Knox SDK modules (knox-core, knox-enterprise). It follows the same pattern as `androidx.hilt:hilt-work` - the Knox modules remain DI-agnostic while this module provides the Hilt-specific wiring.

## Build Commands

```bash
# Build module
./gradlew :knox-hilt:build

# Run tests
./gradlew :knox-hilt:test

# Check (lint + tests)
./gradlew :knox-hilt:check
```

## Architecture

### Module Dependency Flow
```
app -> knox-hilt -> knox-core/knox-enterprise -> core modules
```

### Hilt Modules Provided

All modules install into `SingletonComponent`:

| Module | Provides |
|--------|----------|
| `EnterprisePolicyBindingsModule` | `Set<PolicyComponent>` via multibindings from `GeneratedPolicyComponents` |
| `PolicyRegistryModule` | `PolicyRegistry` (bound to `HiltPolicyRegistry`) |
| `AndroidContextProviderModule` | `AndroidApplicationContextProvider` (bound to `HiltAndroidContextProvider`) |
| `DispatcherModule` | Coroutine dispatchers with `@IoDispatcher`, `@DefaultDispatcher`, `@MainDispatcher`, `@MainImmediateDispatcher` qualifiers |
| `AndroidServiceModule` | System services: `DevicePolicyManager`, `WifiManager`, `ConnectivityManager`, `PowerManager`, `TelephonyManager`, `PackageManager` |
| `CommonModule` | `ResourceProvider` for string resource access |
| `DataStoreModule` | `DataStore<Preferences>`, `DataStoreSource`, `PreferencesRepository` |
| `PolicyGroupingModule` | `PolicyGroupingStrategy` (default: `CapabilityBasedGroupingStrategy`) |
| `KnoxLicensingModule` | `KnoxLicenseInitializer` |

### Backward Compatibility Pattern

Several modules register instances with companion object factories for non-Hilt code paths:
- `DataStoreSource.setInstance()`
- `PreferencesRepository.setInstance()`
- `KnoxStartupManager.setInstance()`

### Overriding Default Bindings

To override `PolicyGroupingStrategy` (or other defaults):

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object CustomGroupingModule {
    @Provides
    @Singleton
    fun provideGroupingStrategy(): PolicyGroupingStrategy {
        return ConfigurableGroupingStrategy(
            GroupingConfiguration.builder()
                .addGroup("quick", "Quick Access")
                .addGroup("advanced", "Advanced")
                .assignPolicies("quick", "device_lock_mode", "screen_brightness")
                .build()
        )
    }
}
```

## Key Classes

- `HiltPolicyRegistry`: Uses Kotlin `by` delegation to forward all `PolicyRegistry` methods to `CachedPolicyRegistry`. Receives policy components via Hilt setter injection. The delegation pattern ensures new interface methods are automatically supported without code changes.
- `HiltAndroidContextProvider`: Simple wrapper providing application context to knox modules

## Design Pattern: Kotlin Delegation for Hilt Wrappers

When creating Hilt-injectable wrappers around knox-core interfaces, use Kotlin's `by` delegation to avoid manual method forwarding:

```kotlin
@Singleton
class HiltPolicyRegistry @Inject constructor() : PolicyRegistry by delegate {
    companion object {
        private val delegate = CachedPolicyRegistry(DefaultPolicyRegistry())
    }

    @Inject
    fun setComponents(components: Set<@JvmSuppressWildcards PolicyComponent<out PolicyState>>) {
        delegate.components = components
    }
}
```

This eliminates boilerplate and ensures interface changes are automatically supported.

## Testing Notes

- Uses MockK for mocking
- No instrumentation tests (bindings are verified via app module tests)
