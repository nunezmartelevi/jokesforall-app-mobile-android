package com.levi.jokesforall.data.repository

import com.levi.jokesforall.data.database.JokeDao
import com.levi.jokesforall.data.model.JokeEntity
import com.levi.jokesforall.data.model.asDatabaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeJokesDao: JokeDao {

    private val jokeEntities = mutableListOf<JokeEntity>()

    override suspend fun insertJokes(jokes: List<JokeEntity>) {
        jokeEntities.addAll(jokesTestData.asDatabaseModel())
    }

    override suspend fun deleteAll() {
        jokeEntities.clear()
    }

    override fun loadAllJokes(): Flow<List<JokeEntity>> {
        return flow { emit(jokeEntities) }
    }
}
