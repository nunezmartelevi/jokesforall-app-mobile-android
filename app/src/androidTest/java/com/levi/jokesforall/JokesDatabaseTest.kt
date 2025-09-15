package com.levi.jokesforall

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.levi.jokesforall.data.database.JokeDao
import com.levi.jokesforall.data.database.JokesDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class JokesDatabaseTest {
    private lateinit var jokeDao: JokeDao
    private lateinit var database: JokesDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, JokesDatabase::class.java
        ).build()
        jokeDao = database.jokeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertJokesWritesToDatabase() = runTest {
        val result = jokeDao.insertJokes(jokesEntities)
        assert(result.all { it > 0 })
    }

    @Test
    @Throws(Exception::class)
    fun loadAllUnseenJokesReadsUnseenJokes() = runTest {
        jokeDao.insertJokes(jokesEntities)
        val unseenJokes = jokeDao.loadAllUnseenJokes().first()
        assert(unseenJokes.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllJokesReturnsEmptyList() = runTest {
        jokeDao.insertJokes(jokesEntities)
        jokeDao.deleteAll()
        val jokes = jokeDao.loadAllUnseenJokes().first()
        assert(jokes.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun markASSeenUpdatesJoke() = runTest {
        jokeDao.insertJokes(jokesEntities)
        val jokeId = jokesEntities.first().id
        jokeDao.markAsSeen(jokeId)
        val unseenJokes = jokeDao.loadAllUnseenJokes().first()
        assert(unseenJokes.none { it.id == jokeId })
    }
}
