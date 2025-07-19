package com.levi.jokesforall.data.repository

import com.levi.jokesforall.data.model.JokesResponse
import com.levi.jokesforall.data.remote.JokesService
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeJokeService(private val shouldReturnError: Boolean = false) : JokesService {
    override suspend fun getJokes(): Response<JokesResponse> {
        return if (!shouldReturnError) {
            Response<JokesResponse>.success(
                JokesResponse(
                    error = false,
                    amount = jokesTestData.size,
                    jokes = jokesTestData
                )
            )
        } else {
            Response<JokesResponse>.error(500, byteArrayOf().toResponseBody(null))
        }
    }
}
