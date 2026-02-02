package net.sfelabs.knox.hilt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sfelabs.knox.core.feature.api.CapabilityBasedGroupingStrategy
import net.sfelabs.knox.core.feature.api.PolicyGroupingStrategy
import javax.inject.Singleton

/**
 * Hilt module that provides the default [PolicyGroupingStrategy].
 *
 * By default, policies are grouped by their primary capability using
 * [CapabilityBasedGroupingStrategy]. Applications can override this
 * by providing their own binding.
 *
 * ## Overriding the Default Strategy
 *
 * To use a custom grouping strategy, create a module with higher priority:
 *
 * ```kotlin
 * @Module
 * @InstallIn(SingletonComponent::class)
 * object CustomGroupingModule {
 *     @Provides
 *     @Singleton
 *     fun provideGroupingStrategy(): PolicyGroupingStrategy {
 *         return MyCustomGroupingStrategy()
 *     }
 * }
 * ```
 *
 * Or use [ConfigurableGroupingStrategy] for runtime configuration:
 *
 * ```kotlin
 * @Module
 * @InstallIn(SingletonComponent::class)
 * object CustomGroupingModule {
 *     @Provides
 *     @Singleton
 *     fun provideGroupingStrategy(): PolicyGroupingStrategy {
 *         val config = GroupingConfiguration.builder()
 *             .addGroup("quick", "Quick Toggles")
 *             .addGroup("advanced", "Advanced")
 *             .assignPolicies("quick", "tactical_device_mode", "enable_night_vision_mode")
 *             .assignPolicies("advanced", "enable_hdm", "band_locking_5g")
 *             .build()
 *         return ConfigurableGroupingStrategy(config)
 *     }
 * }
 * ```
 */
@Module
@InstallIn(SingletonComponent::class)
object PolicyGroupingModule {

    @Provides
    @Singleton
    fun providePolicyGroupingStrategy(): PolicyGroupingStrategy {
        return CapabilityBasedGroupingStrategy()
    }
}
