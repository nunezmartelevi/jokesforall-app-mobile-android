package com.levi.jokesforall.datas.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.levi.jokesforall.datas.model.JokeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJokes(jokes: List<JokeEntity>): List<Long>

    @Query("DELETE FROM jokes")
    suspend fun deleteAll()

    @Query("SELECT * FROM jokes WHERE seen = 0")
    fun loadAllUnseenJokes(): Flow<List<JokeEntity>>

    @Query("UPDATE jokes SET seen = 1 WHERE id = :id")
    suspend fun markAsSeen(id: Int)
}
