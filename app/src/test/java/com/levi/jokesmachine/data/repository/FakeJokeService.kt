package com.levi.jokesmachine.data.repository

import com.levi.jokesmachine.data.model.JokesResponse
import com.levi.jokesmachine.data.remote.JokesService
import com.levi.jokesmachine.remoteJokes
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
