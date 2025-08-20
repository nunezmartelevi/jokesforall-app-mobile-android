package com.levi.jokesforall.domain.repository

import com.levi.jokesforall.data.remote.Result
import com.levi.jokesforall.domain.model.Joke

interface JokesRepository {
    suspend fun getJokes(): Result<List<Joke>>
    suspend fun markJokeAsSeen(id: Int)
}
