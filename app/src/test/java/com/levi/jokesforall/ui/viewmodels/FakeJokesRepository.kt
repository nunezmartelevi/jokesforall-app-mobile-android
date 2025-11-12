package com.levi.jokesforall.ui.viewmodels

import com.levi.jokesforall.data.remote.Results
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.repository.JokesRepository
import com.levi.jokesforall.domainJokes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeJokesRepository(
    private val isSuccessful: Boolean = true
) : JokesRepository {
    private val jokesList: MutableList<Joke> = mutableListOf()

    override val observeAllUnseenJokes: Flow<List<Joke>> = flow {
        emit(jokesList)
    }

    override suspend fun refreshJokes(): Results<Unit> {
        return if (isSuccessful) {
            jokesList.addAll(domainJokes)
            Results.Success(Unit)
        } else {
            Results.Error(Exception("Error"))
        }
    }

    override suspend fun markJokeAsSeen(id: Int) {
        jokesList.firstOrNull { it.id == id }?.let { joke ->
            jokesList.remove(joke)
        }
    }
}
