package net.sfelabs.knox.hilt.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sfelabs.knox.core.feature.domain.registry.PolicyRegistry
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PolicyRegistryModule {
    @Binds
    @Singleton
    abstract fun bindPolicyRegistry(impl: HiltPolicyRegistry): PolicyRegistry
}
