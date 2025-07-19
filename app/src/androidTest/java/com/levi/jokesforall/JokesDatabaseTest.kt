package com.levi.jokesforall

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.levi.jokesforall.data.database.JokeDao
import com.levi.jokesforall.data.database.JokesDatabase
import com.levi.jokesforall.data.model.JokeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
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
    fun writeJokesAndLoadList() = runTest {
        jokeDao.insertJokes(jokesEntitiesTestData)
        val jokes = jokeDao.loadAllJokes().first()
        assert(jokes.isNotEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllJokesReturnsEmptyList() = runTest {
        jokeDao.insertJokes(jokesEntitiesTestData)
        jokeDao.deleteAll()
        val jokes = jokeDao.loadAllJokes().first()
        assert(jokes.isEmpty())
    }

}
