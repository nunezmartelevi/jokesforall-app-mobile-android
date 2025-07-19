package com.levi.jokesforall.data.remote
import com.levi.jokesforall.data.model.JokesResponse
import retrofit2.Response
import retrofit2.http.GET
interface JokesService {
    @GET("")
    suspend fun getJokes(): Response<JokesResponse>
}
