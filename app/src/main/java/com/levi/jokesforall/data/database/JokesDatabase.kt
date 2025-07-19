package com.levi.jokesforall.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.levi.jokesforall.data.model.JokeEntity

@Database(entities = [JokeEntity::class], version = 1, exportSchema = false)
abstract class JokesDatabase: RoomDatabase() {
    abstract fun jokeDao(): JokeDao
}
