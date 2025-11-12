package com.levi.jokesforall.datas.repository

import com.levi.jokesforall.datas.database.JokeDao
import com.levi.jokesforall.datas.model.JokeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeJokesDao : JokeDao {

    private val jokeEntities = mutableListOf<JokeEntity>()

    override suspend fun insertJokes(jokes: List<JokeEntity>): List<Long> {
        jokeEntities.addAll(jokes)
        return jokeEntities.map { it.id.toLong() }
    }

    override suspend fun deleteAll() {
        jokeEntities.clear()
    }

    override fun loadAllUnseenJokes(): Flow<List<JokeEntity>> = flow {
        emit(jokeEntities.filter { !it.seen })
    }

    override fun markAsSeen(id: Int) {
        jokeEntities.firstOrNull { it.id == id }?.let {
            jokeEntities.remove(it)
            jokeEntities.add(it.copy(seen = true))
        }
    }
}
