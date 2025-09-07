package com.levi.jokesforall.domain.repository

import com.levi.jokesforall.data.remote.Result
import com.levi.jokesforall.domain.model.Joke
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for accessing and managing joke data.
 * This repository acts as the single source of truth for jokes, handling both
 * remote fetching and local persistence.
 */
interface JokesRepository {
    val observeAllUnseenJokes: Flow<List<Joke>>
    suspend fun refreshJokes(): Result<Unit>
    suspend fun markJokeAsSeen(id: Int)
}
