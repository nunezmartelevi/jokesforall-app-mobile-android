package com.levi.jokesforall.datas.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.levi.jokesforall.datas.model.JokeEntity

@Database(entities = [JokeEntity::class], version = 1, exportSchema = false)
abstract class JokesDatabase: RoomDatabase() {
    abstract fun jokeDao(): JokeDao
}
