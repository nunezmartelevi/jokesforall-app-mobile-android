package com.levi.jokesforall.datas.remote
import com.levi.jokesforall.datas.model.JokesResponse
import retrofit2.Response
import retrofit2.http.GET
interface JokesService {
    @GET("joke/Programming?amount=10")
    suspend fun getJokes(): Response<JokesResponse>
}
