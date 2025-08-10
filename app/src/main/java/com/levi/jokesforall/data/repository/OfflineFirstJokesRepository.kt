package com.levi.jokesforall.data.repository

import com.levi.jokesforall.data.database.JokeDao
import com.levi.jokesforall.data.model.asDatabaseModel
import com.levi.jokesforall.data.model.asDomainModel
import com.levi.jokesforall.data.remote.Dispatcher
import com.levi.jokesforall.data.remote.JokesDispatchers.IO
import com.levi.jokesforall.data.remote.JokesService
import com.levi.jokesforall.data.remote.Result
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.repository.JokesRepository
import com.levi.jokesforall.domain.repository.Syncable
import com.levi.jokesforall.domain.repository.SyncingDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineFirstJokesRepository @Inject constructor(
    private val remoteDataSource: JokesService,
    private val localDataSource: JokeDao,
    @param:Dispatcher(IO) private val dispatcher: CoroutineDispatcher
) : JokesRepository, Syncable {

    override suspend fun getJokes(): Result<List<Joke>> {
        return withContext(dispatcher) {
            try {
                val jokesFromCache = localDataSource.loadAllUnseenJokes()
                val isCacheEmptyOrAllJokesSeen =
                    jokesFromCache.isEmpty() || jokesFromCache.all { it.seen }
                when {
                    isCacheEmptyOrAllJokesSeen -> {
                        val wasSyncSuccessful = sync()
                        if (wasSyncSuccessful) {
                            val newJokes = localDataSource.loadAllUnseenJokes().asDomainModel()
                            Result.Success(newJokes)
                        } else {
                            Result.Error(SyncingDataException())
                        }
                    }

                    else -> {
                        Result.Success(jokesFromCache.asDomainModel())
                    }
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }
    }

    override suspend fun sync(): Boolean {
        val response = remoteDataSource.getJokes()
        if (response.isSuccessful) {
            val jokes = response.body()?.jokes ?: emptyList()
            localDataSource.deleteAll()
            val rowIds = localDataSource.insertJokes(jokes.asDatabaseModel())
            return rowIds.all { it > 0 }
        } else {
            return false
        }
    }

    override suspend fun markJokeAsSeen(id: Int) {
        withContext(dispatcher) {
            localDataSource.markAsSeen(id)
        }
    }
}
