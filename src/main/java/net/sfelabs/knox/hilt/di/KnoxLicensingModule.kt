package net.sfelabs.knox.hilt.di

import com.github.jpicklyk.knox.licensing.domain.KnoxLicenseInitializer
import com.github.jpicklyk.knox.licensing.domain.KnoxStartupManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides Knox licensing dependencies.
 *
 * This module provides:
 * - [KnoxLicenseInitializer] as a singleton
 *
 * The provided instance is also registered with [KnoxStartupManager] for
 * backward compatibility with code that uses the static methods.
 */
@Module
@InstallIn(SingletonComponent::class)
object KnoxLicensingModule {

    @Provides
    @Singleton
    fun provideKnoxLicenseInitializer(): KnoxLicenseInitializer {
        val initializer = KnoxLicenseInitializer()
        // Register with KnoxStartupManager for backward compatibility
        KnoxStartupManager.setInstance(initializer)
        return initializer
    }
}
