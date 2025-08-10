package com.levi.jokesforall.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.levi.jokesforall.data.model.JokeEntity

@Dao
interface JokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJokes(jokes: List<JokeEntity>): List<Long>

    @Query("DELETE FROM jokes")
    suspend fun deleteAll()

    @Query("SELECT * FROM jokes WHERE seen = false")
    fun loadAllUnseenJokes(): List<JokeEntity>

    @Query("UPDATE jokes SET seen = true WHERE id = :id")
    fun markAsSeen(id: Int)
}
