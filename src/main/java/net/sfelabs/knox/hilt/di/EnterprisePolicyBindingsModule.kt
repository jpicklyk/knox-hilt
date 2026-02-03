package net.sfelabs.knox.hilt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import net.sfelabs.knox.core.feature.api.PolicyComponent
import net.sfelabs.knox.core.feature.api.PolicyState
import net.sfelabs.knox_enterprise.generated.policy.GeneratedPolicyComponents

/**
 * Hilt module that provides Knox Enterprise policy components.
 *
 * This module bridges the DI-agnostic [GeneratedPolicyComponents] registry
 * with Hilt's dependency injection system using multibindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object EnterprisePolicyBindingsModule {

    @Provides
    @ElementsIntoSet
    fun provideEnterprisePolicies(): Set<@JvmSuppressWildcards PolicyComponent<out PolicyState>> =
        GeneratedPolicyComponents.getAll().toSet()
}
