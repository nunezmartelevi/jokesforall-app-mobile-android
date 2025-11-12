package com.levi.jokesforall.dis

import com.levi.jokesforall.datas.repository.OfflineFirstJokesRepository
import com.levi.jokesforall.datas.repository.UserPreferencesRepository
import com.levi.jokesforall.domains.repository.PreferencesRepository
import com.levi.jokesforall.domains.repository.JokesRepository
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
