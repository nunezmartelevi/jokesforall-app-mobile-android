package com.levi.jokesforall.ui.viewmodels

import com.levi.jokesforall.data.remote.Result
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.model.JokeType
import com.levi.jokesforall.domain.repository.JokesRepository
import com.levi.jokesforall.singlePartJokes
import com.levi.jokesforall.twoPartJokes
import com.levi.jokesforall.jokes
import kotlinx.coroutines.flow.Flow

class FakeJokesRepository(
    private val isSuccessful: Boolean = true,
    val type: JokeType? = null
) : JokesRepository {
    private val jokesList: MutableList<Joke> = jokes.toMutableList()

    suspend fun getJoke(): Result<Joke> {
        return if (isSuccessful) {
            val joke = when (type) {
                JokeType.SINGLE -> singlePartJokes.first()
                JokeType.TWOPART -> twoPartJokes.first()
                else -> jokes.first()
            }
            Result.Success(joke)
        } else {
            Result.Error(Exception("Error"))
        }
    }

    override val observeAllUnseenJokes: Flow<Joke>
        get() = TODO("Not yet implemented")

    override suspend fun markJokeAsSeen(id: Int) {
        jokesList.firstOrNull { it.id == id }?.let { joke ->
            jokesList.remove(joke)
        }
    }

    override suspend fun sync(): Boolean {
        TODO("Not yet implemented")
    }
}
