package com.levi.jokesforall.data.repository

import com.levi.jokesforall.data.database.JokeDao
import com.levi.jokesforall.data.model.asDatabaseModel
import com.levi.jokesforall.data.remote.JokesService
import com.levi.jokesforall.data.remote.Result
import com.levi.jokesforall.domain.repository.JokesRepository
import kotlinx.coroutines.flow.first
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
    fun `getJokes returns a list of jokes`() = runTest {
        repository = OfflineFirstJokesRepository(
            service,
            dao,
            StandardTestDispatcher(testScheduler)
        )
        val result = repository.getJokes()
        assert((result as Result.Success).data.isNotEmpty())
    }

    @Test
    fun `getJokes returns an error when service fails`() = runTest {
        service = FakeJokeService(isSuccessful = false)
        repository = OfflineFirstJokesRepository(
            service,
            dao,
            StandardTestDispatcher(testScheduler)
        )
        val result = repository.getJokes()
        assert(result is Result.Error)
    }

    @Test
    fun `markJokeAsSeen updates the joke in the database`() = runTest {
        val jokeToMark = jokesTestData.first()
        repository = OfflineFirstJokesRepository(
            service,
            dao,
            StandardTestDispatcher(testScheduler)
        )
        repository.markJokeAsSeen(jokeToMark.id)
        val unSeenJokes = dao.loadAllUnseenJokes()
        assert(unSeenJokes.none { it.id == jokeToMark.id })
    }
}
