package net.sfelabs.knox.hilt.di

import net.sfelabs.knox.core.feature.api.PolicyComponent
import net.sfelabs.knox.core.feature.api.PolicyState
import net.sfelabs.knox.core.feature.data.repository.CachedPolicyRegistry
import net.sfelabs.knox.core.feature.data.repository.DefaultPolicyRegistry
import net.sfelabs.knox.core.feature.domain.registry.PolicyRegistry
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Hilt-injectable PolicyRegistry that delegates to CachedPolicyRegistry.
 *
 * Uses Kotlin's `by` delegation to automatically forward all PolicyRegistry
 * methods, eliminating boilerplate and ensuring new interface methods are
 * automatically supported.
 */
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
