package net.sfelabs.knox.hilt.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sfelabs.knox.core.common.data.datasource.DataStoreSource
import net.sfelabs.knox.core.common.data.datasource.DefaultDataStoreSource
import net.sfelabs.knox.core.common.domain.repository.DefaultPreferencesRepository
import net.sfelabs.knox.core.common.domain.repository.PreferencesRepository
import okio.Path.Companion.toPath
import javax.inject.Singleton

/**
 * Hilt module that provides DataStore-related dependencies as singletons.
 *
 * This module provides:
 * - [DataStore] for Preferences
 * - [DataStoreSource] implementation
 * - [PreferencesRepository] implementation
 *
 * The provided instances are also registered with the companion object factories
 * in [DataStoreSource] and [PreferencesRepository] for backward compatibility
 * with code that uses the `getInstance()` pattern (e.g., standalone libraries).
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val DATASTORE_FILE_NAME = "knox_showcase_settings.preferences_pb"

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        produceFile = {
            context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath.toPath()
        }
    )

    @Provides
    @Singleton
    fun provideDataStoreSource(
        dataStore: DataStore<Preferences>
    ): DataStoreSource {
        val source = DefaultDataStoreSource(dataStore)
        // Register with companion object for backward compatibility with getInstance()
        DataStoreSource.setInstance(source)
        return source
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(
        dataStoreSource: DataStoreSource
    ): PreferencesRepository {
        val repository = DefaultPreferencesRepository(dataStoreSource)
        // Register with companion object for backward compatibility with getInstance()
        PreferencesRepository.setInstance(repository)
        return repository
    }
}
