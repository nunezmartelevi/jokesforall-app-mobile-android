package com.levi.jokesforall.di

import com.levi.jokesforall.data.repository.OfflineFirstJokesRepository
import com.levi.jokesforall.domain.repository.JokesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @javax.inject.Singleton
    abstract fun bindJokesRepository(
        impl: OfflineFirstJokesRepository
    ): JokesRepository
}
