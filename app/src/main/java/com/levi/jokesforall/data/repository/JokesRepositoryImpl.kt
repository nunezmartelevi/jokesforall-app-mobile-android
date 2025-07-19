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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class JokesRepositoryImpl @Inject constructor(
    private val service: JokesService,
    private val dao: JokeDao,
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher
) : JokesRepository {

    override val jokes: Flow<List<Joke>> = dao.loadAllJokes().map { it.asDomainModel() }

    override suspend fun refreshJokes(): Result<Unit> {
        return withContext(dispatcher) {
            try {
                val response = service.getJokes()
                if (response.isSuccessful) {
                    response.body()?.jokes?.let { jokesList ->
                        dao.deleteAll()
                        dao.insertJokes(jokesList.asDatabaseModel())
                        Result.Success(Unit)
                    } ?: Result.Error(
                        Exception("Successful response with empty or malformed body")
                    )
                } else {
                    Result.Error(HttpException(response))
                }
            } catch (e: IOException) {
                Result.Error(e)
            }
        }
    }
}
