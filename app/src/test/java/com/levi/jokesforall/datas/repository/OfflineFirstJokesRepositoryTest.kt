package com.levi.jokesforall.datas.repository

import com.levi.jokesforall.datas.database.JokeDao
import com.levi.jokesforall.datas.remote.JokesService
import com.levi.jokesforall.datas.remote.Result
import com.levi.jokesforall.domains.repository.JokesRepository
import com.levi.jokesforall.remoteJokes
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
    fun `observeAllUnseenJokes returns the correct jokes`() = runTest {
        repository = OfflineFirstJokesRepository(
            service,
            dao,
            StandardTestDispatcher(testScheduler)
        )
        repository.refreshJokes()
        val unseenJokes = repository.observeAllUnseenJokes.first()
        assert(unseenJokes.isNotEmpty())
    }

    @Test
    fun `refreshJokes when sync is successful returns Success`() = runTest {
        repository = OfflineFirstJokesRepository(
            service,
            dao,
            StandardTestDispatcher(testScheduler)
        )
        val result = repository.refreshJokes()
        assert(result is Result.Success)
    }

    @Test
    fun `refreshJokes when sync is unsuccessful returns Error`() = runTest {
        service = FakeJokeService(isSuccessful = false)
        repository = OfflineFirstJokesRepository(
            service,
            dao,
            StandardTestDispatcher(testScheduler)
        )
        val result = repository.refreshJokes()
        assert(result is Result.Error)
    }

    @Test
    fun `markJokeAsSeen updates the joke in the database`() = runTest {
        val jokeToMark = remoteJokes.first()
        repository = OfflineFirstJokesRepository(
            service,
            dao,
            StandardTestDispatcher(testScheduler)
        )
        repository.refreshJokes()
        repository.markJokeAsSeen(jokeToMark.id)
        val unseenJokes = repository.observeAllUnseenJokes.first()
        assert(unseenJokes.none { it.id == jokeToMark.id })
    }
}
