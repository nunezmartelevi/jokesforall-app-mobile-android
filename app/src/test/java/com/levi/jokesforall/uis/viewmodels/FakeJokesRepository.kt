package com.levi.jokesforall.uis.viewmodels

import com.levi.jokesforall.datas.remote.Result
import com.levi.jokesforall.domains.model.Joke
import com.levi.jokesforall.domains.repository.JokesRepository
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
