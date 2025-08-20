package com.levi.jokesforall.ui.viewmodels

import com.levi.jokesforall.data.remote.Result
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.model.JokeType
import com.levi.jokesforall.domain.repository.JokesRepository
import com.levi.jokesforall.jokes
import com.levi.jokesforall.singlePartJokes
import com.levi.jokesforall.twoPartJokes

class FakeJokesRepository(
    private val isSuccessful: Boolean = true,
    type: JokeType? = null
) : JokesRepository {
    private val unSeenJokes: MutableList<Joke> = type?.let {
        when(it)   {
            JokeType.SINGLE -> singlePartJokes.toMutableList()
            JokeType.TWOPART -> twoPartJokes.toMutableList()
        }
    }?: jokes.toMutableList()

    override suspend fun getJokes(): Result<List<Joke>> {
        return if (isSuccessful) {
            Result.Success(unSeenJokes)
        } else {
            Result.Error(Exception("Error"))
        }
    }

    override suspend fun markJokeAsSeen(id: Int) {
        unSeenJokes.firstOrNull { it.id == id }?.let { joke ->
            unSeenJokes.remove(joke)
        }
    }
}
