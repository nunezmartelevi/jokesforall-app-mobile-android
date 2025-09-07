package com.levi.jokesforall.di

import com.levi.jokesforall.data.repository.OfflineFirstJokesRepository
import com.levi.jokesforall.data.repository.UserPreferencesRepository
import com.levi.jokesforall.domain.repository.PreferencesRepository
import com.levi.jokesforall.domain.repository.JokesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindJokesRepository(
        impl: OfflineFirstJokesRepository
    ): JokesRepository

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        impl: UserPreferencesRepository
    ): PreferencesRepository
}
