package com.levi.jokesforall.data.repository

import com.levi.jokesforall.data.database.JokeDao
import com.levi.jokesforall.data.model.JokeEntity
import com.levi.jokesforall.data.model.asDatabaseModel
import com.levi.jokesforall.localJokes
import com.levi.jokesforall.remoteJokes

class FakeJokesDao : JokeDao {

    private val jokeEntities = mutableListOf<JokeEntity>()

    override suspend fun insertJokes(jokes: List<JokeEntity>): List<Long> {
        jokeEntities.addAll(localJokes)
        return jokeEntities.map { it.id.toLong() }
    }

    override suspend fun deleteAll() {
        jokeEntities.clear()
    }

    override fun loadAllUnseenJokes(): List<JokeEntity> {
        return jokeEntities.filter { !it.seen }
    }

    override fun markAsSeen(id: Int) {
        jokeEntities.firstOrNull { it.id == id }?.let {
            jokeEntities.remove(it)
            jokeEntities.add(it.copy(seen = true))
        }
    }
}
