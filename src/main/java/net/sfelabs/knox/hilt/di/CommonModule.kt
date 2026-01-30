package net.sfelabs.knox.hilt.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sfelabs.knox.core.common.factory.DefaultResourceProviderFactory
import net.sfelabs.knox.core.common.presentation.ResourceProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return DefaultResourceProviderFactory.create(context)
    }
}
