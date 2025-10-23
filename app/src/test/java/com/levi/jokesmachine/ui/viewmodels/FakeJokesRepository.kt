package com.levi.jokesmachine.ui.viewmodels

import com.levi.jokesmachine.data.remote.Result
import com.levi.jokesmachine.domain.model.Joke
import com.levi.jokesmachine.domain.repository.JokesRepository
import com.levi.jokesmachine.domainJokes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeJokesRepository(
    private val isSuccessful: Boolean = true
) : JokesRepository {
    private val jokesList: MutableList<Joke> = mutableListOf()

    override val observeAllUnseenJokes: Flow<List<Joke>> = flow {
        emit(jokesList)
    }

    override suspend fun refreshJokes(): Result<Unit> {
        return if (isSuccessful) {
            jokesList.addAll(domainJokes)
            Result.Success(Unit)
        } else {
            Result.Error(Exception("Error"))
        }
    }

    override suspend fun markJokeAsSeen(id: Int) {
        jokesList.firstOrNull { it.id == id }?.let { joke ->
            jokesList.remove(joke)
        }
    }
}
