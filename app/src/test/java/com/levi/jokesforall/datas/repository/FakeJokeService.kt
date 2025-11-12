package com.levi.jokesforall.datas.repository

import com.levi.jokesforall.datas.model.JokesResponse
import com.levi.jokesforall.datas.remote.JokesService
import com.levi.jokesforall.remoteJokes
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeJokeService(private val isSuccessful: Boolean = true) : JokesService {
    override suspend fun getJokes(): Response<JokesResponse> {
        return if (isSuccessful) {
            Response<JokesResponse>.success(
                JokesResponse(
                    error = false,
                    amount = remoteJokes.size,
                    jokes = remoteJokes
                )
            )
        } else {
            Response<JokesResponse>.error(
                500,
                byteArrayOf().toResponseBody(null)
            )
        }
    }
}
