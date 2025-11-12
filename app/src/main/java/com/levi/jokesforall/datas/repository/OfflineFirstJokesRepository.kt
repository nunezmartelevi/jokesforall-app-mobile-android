package com.levi.jokesforall.datas.repository

import com.levi.jokesforall.datas.database.JokeDao
import com.levi.jokesforall.datas.model.asDatabaseModel
import com.levi.jokesforall.datas.model.asDomainModel
import com.levi.jokesforall.datas.remote.Dispatcher
import com.levi.jokesforall.datas.remote.JokesDispatcher.IO
import com.levi.jokesforall.datas.remote.JokesService
import com.levi.jokesforall.datas.remote.Result
import com.levi.jokesforall.domains.model.Joke
import com.levi.jokesforall.domains.repository.JokesRepository
import com.levi.jokesforall.domains.repository.NoInternetException
import com.levi.jokesforall.domains.repository.SyncingDataException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
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
    private val syncDelayInMillis = 1500L

    override val observeAllUnseenJokes: Flow<List<Joke>> =
        localDataSource.loadAllUnseenJokes()
            .map { cachedJokes ->
                cachedJokes.asDomainModel()
            }.flowOn(dispatcher)


    override suspend fun refreshJokes(): Result<Unit> {
        return withContext(dispatcher) {
            try {
                val wasSyncSuccessful = sync()
                if (wasSyncSuccessful) {
                    println("calling refresh from repository")
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
            // Delay to allow the loading animation to show
            delay(syncDelayInMillis)
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
