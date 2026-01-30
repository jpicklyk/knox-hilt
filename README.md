# knox-hilt

Dagger Hilt integration module for Knox SDK modules. This module provides the dependency injection bindings needed to use knox-core and knox-enterprise in a Hilt-based Android application.

## Purpose

The Knox modules (knox-core, knox-enterprise) are designed to be DI-agnostic. This module provides the Hilt-specific bindings to wire everything together, following the same pattern as `androidx.hilt:hilt-work` for WorkManager integration.

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Your App                              │
├─────────────────────────────────────────────────────────┤
│  knox-hilt (this module)                                │
│  - Provides Hilt bindings for knox modules              │
├─────────────────────────────────────────────────────────┤
│  Knox Modules (DI-agnostic)                             │
│  ┌─────────────┐ ┌────────────────┐                     │
│  │ knox-core   │ │ knox-enterprise│                     │
│  └─────────────┘ └────────────────┘                     │
└─────────────────────────────────────────────────────────┘
```

## Provided Modules

### PolicyRegistryModule
Binds `HiltPolicyRegistry` as the `PolicyRegistry` implementation. This registry manages all Knox policy components and provides access to policy state.

### AndroidContextProviderModule
Binds `HiltAndroidContextProvider` as the `AndroidApplicationContextProvider`. This allows knox modules to access the application context without direct Hilt dependencies.

### DispatcherModule
Provides coroutine dispatchers with qualifiers for proper threading:
- `@IoDispatcher` - For IO-bound operations
- `@DefaultDispatcher` - For CPU-bound operations
- `@MainDispatcher` - For main thread operations
- `@MainImmediateDispatcher` - For immediate main thread dispatch

### AndroidServiceModule
Provides Android system services (ConnectivityManager, etc.) via Hilt injection.

### CommonModule
Provides common dependencies like JSON serialization.

## Usage

### 1. Add dependency

```kotlin
// In your app's build.gradle.kts
dependencies {
    implementation(project(":knox-hilt"))

    // Knox modules you need
    implementation(project(":knox-core:feature"))
    implementation(project(":knox-enterprise"))
}
```

### 2. Annotate your Application class

```kotlin
@HiltAndroidApp
class YourApplication : Application()
```

### 3. Use Knox policies in your ViewModels

```kotlin
@HiltViewModel
class YourViewModel @Inject constructor(
    private val policyRegistry: PolicyRegistry
) : ViewModel() {

    fun loadPolicies() {
        viewModelScope.launch {
            val policies = policyRegistry.getAllPolicies()
            // Handle policies
        }
    }
}
```

### 4. Use Knox use cases directly

Knox use cases don't require injection - they can be instantiated directly:

```kotlin
// Use cases manage their own dependencies internally
val result = SetBrightnessValueUseCase().invoke(brightness = 100)
```

## Licensing

For Knox license management, apps should provide their own Hilt bindings using `knox-licensing`. See the app module's `KnoxLicensingModule` for an example implementation. This gives apps full control over license configuration and selection strategy.

## Alternative DI Frameworks

If you're using a different DI framework (Koin, manual DI, etc.), you don't need this module. Instead, implement the required interfaces directly:

- `AndroidApplicationContextProvider` - Provide application context
- `PolicyRegistry` - Use `DefaultPolicyRegistry` or `CachedPolicyRegistry`

The knox modules will work with any DI solution that can provide these dependencies.
