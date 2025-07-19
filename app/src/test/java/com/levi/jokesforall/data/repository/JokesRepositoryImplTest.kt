package com.levi.jokesforall.data.repository

import com.levi.jokesforall.data.database.JokeDao
import com.levi.jokesforall.data.remote.JokesService
import com.levi.jokesforall.data.remote.Result
import com.levi.jokesforall.domain.repository.JokesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class JokesRepositoryImplTest {

    private lateinit var service: JokesService
    private lateinit var dao: JokeDao
    private lateinit var repository: JokesRepository

    @Before
    fun setup() {
        service = FakeJokeService()
        dao = FakeJokesDao()
    }

    @Test
    fun `refreshJokes on success response inserts jokes into the database`() = runTest {
        repository = JokesRepositoryImpl(
            service,
            dao,
            StandardTestDispatcher(testScheduler)
        )
        repository.refreshJokes()
        val jokes = repository.jokes.first()
        assert(jokes.isNotEmpty())
    }

    @Test
    fun `refreshJokes on error response returns error result`() = runTest {
        repository = JokesRepositoryImpl(
            FakeJokeService(true),
            dao,
            StandardTestDispatcher(testScheduler)
        )
        val result = repository.refreshJokes()
        assert(result is Result.Error)
    }

}
