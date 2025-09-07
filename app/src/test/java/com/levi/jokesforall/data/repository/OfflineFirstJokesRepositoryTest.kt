package com.levi.jokesforall.data.repository

import com.levi.jokesforall.data.database.JokeDao
import com.levi.jokesforall.data.remote.JokesService
import com.levi.jokesforall.domain.repository.JokesRepository
import com.levi.jokesforall.remoteJokes
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OfflineFirstJokesRepositoryTest {
    private lateinit var service: JokesService
    private lateinit var dao: JokeDao
    private lateinit var repository: JokesRepository

    @Before
    fun setup() {
        service = FakeJokeService()
        dao = FakeJokesDao()
    }

    @Test
    fun `getJoke when response is successful returns a joke`() = runTest {
        repository = OfflineFirstJokesRepository(
            service,
            dao,
            StandardTestDispatcher(testScheduler)
        )
        val joke = repository.observeAllUnseenJokes.
        assert(joke.id == remoteJokes.first().id)
    }

//    @Test
//    fun `getJoke when response is error returns an Error`() = runTest {
//        service = FakeJokeService(isSuccessful = false)
//        repository = OfflineFirstJokesRepository(
//            service,
//            dao,
//            StandardTestDispatcher(testScheduler)
//        )
//        val result = repository.getJoke()
//        assert(result is Result.Error)
//    }
//
//    @Test
//    fun `markJokeAsSeen updates the joke in the database`() = runTest {
//        val jokeToMark = remoteJokes.first()
//        repository = OfflineFirstJokesRepository(
//            service,
//            dao,
//            StandardTestDispatcher(testScheduler)
//        )
//        repository.markJokeAsSeen(jokeToMark.id)
//        val unSeenJokes = dao.loadAllUnseenJokes()
//        assert(unSeenJokes.none { it.id == jokeToMark.id })
//    }
}
