package com.levi.jokesforall.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.levi.jokesforall.data.model.JokeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJokes(jokes: List<JokeEntity>)

    @Query("DELETE FROM jokes")
    suspend fun deleteAll()

    @Query("SELECT * FROM jokes")
    fun loadAllJokes(): Flow<List<JokeEntity>>
}
