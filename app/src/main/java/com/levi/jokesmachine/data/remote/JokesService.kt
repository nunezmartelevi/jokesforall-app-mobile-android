package com.levi.jokesmachine.data.remote
import com.levi.jokesmachine.data.model.JokesResponse
import retrofit2.Response
import retrofit2.http.GET
interface JokesService {
    @GET("joke/Programming?amount=10")
    suspend fun getJokes(): Response<JokesResponse>
}
