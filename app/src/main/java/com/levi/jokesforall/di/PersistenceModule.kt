package com.levi.jokesforall.di

import android.content.Context
import androidx.room.Room
import com.levi.jokesforall.data.database.JokeDao
import com.levi.jokesforall.data.database.JokesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideJokesDatabase(@ApplicationContext context: Context): JokesDatabase {
        return Room.databaseBuilder(
            context,
            JokesDatabase::class.java,
            "jokes-database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideDao(database: JokesDatabase): JokeDao {
        return database.jokeDao()
    }
}
