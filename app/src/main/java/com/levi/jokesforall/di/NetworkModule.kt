package com.levi.jokesforall.di

import com.levi.jokesforall.data.jokesData
import com.levi.jokesforall.data.model.JokesResponse
import com.levi.jokesforall.data.remote.JokesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMockJokesService(): JokesService {
        return object : JokesService {
            override suspend fun getJokes() = Response.success(
                JokesResponse(
                    error = false,
                    jokes = jokesData,
                    amount = jokesData.size
                )
            )
        }
    }

//    @Provides
//    fun provideJokesService(): JokesService {
//        return Retrofit.Builder()
//            .baseUrl("https://example.com/")
//            .addConverterFactory(
//                Json.asConverterFactory(
//                    "application/json; charset=UTF8".toMediaType()
//                )
//            )
//            .build()
//            .create(JokesService::class.java)
//    }
}
