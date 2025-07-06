package com.levi.jokesforall.domain.repository

import com.levi.jokesforall.domain.model.Joke
import kotlinx.coroutines.flow.Flow

interface JokesRepository {

    val jokes: Flow<List<Joke>>

    suspend fun refreshJokes()
}
