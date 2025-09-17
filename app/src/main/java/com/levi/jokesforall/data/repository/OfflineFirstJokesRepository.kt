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
import com.levi.jokesforall.domain.repository.NoInternetException
import com.levi.jokesforall.domain.repository.SyncingDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineFirstJokesRepository @Inject constructor(
    private val remoteDataSource: JokesService,
    private val localDataSource: JokeDao,
    @param:Dispatcher(IO) private val dispatcher: CoroutineDispatcher
) : JokesRepository {

    override val observeAllUnseenJokes: Flow<List<Joke>> =
        localDataSource.loadAllUnseenJokes()
            .map { cachedJokes ->
                cachedJokes.asDomainModel()
            }
            .flowOn(dispatcher)

    override suspend fun refreshJokes(): Result<Unit> {
        return withContext(dispatcher) {
            try {
                val wasSyncSuccessful = sync()
                if (wasSyncSuccessful) {
                    Result.Success(Unit)
                } else {
                    Result.Error(SyncingDataException())
                }
            } catch (_: java.io.IOException) {
                Result.Error(NoInternetException())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    private suspend fun sync(): Boolean {
        val response = remoteDataSource.getJokes()
        return if (response.isSuccessful) {
            val jokes = response.body()?.jokes ?: emptyList()
            localDataSource.deleteAll()
            val rowIds = localDataSource.insertJokes(jokes.asDatabaseModel())
            rowIds.any { it > 0 }
        } else {
            false
        }
    }

    override suspend fun markJokeAsSeen(id: Int) {
        withContext(dispatcher) {
            localDataSource.markAsSeen(id)
        }
    }
}
