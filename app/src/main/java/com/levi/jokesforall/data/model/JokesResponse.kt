package com.levi.jokesforall.data.model

import kotlinx.serialization.Serializable

@Serializable
data class JokesResponse(
    val error: Boolean,
    val amount: Int,
    val jokes: List<JokeDto>
)

@Serializable
data class JokeDto(
    val id: Int,
    val category: String,
    val type: String,
    val setup: String,
    val delivery: String?,
    val safe: Boolean,
    val lang: String,
    val flags: FlagsDto
)

@Serializable
data class FlagsDto(
    val nsfw: Boolean,
    val religious: Boolean,
    val political: Boolean,
    val racist: Boolean,
    val sexist: Boolean,
    val explicit: Boolean
)

fun List<JokeDto>.asDatabaseModel(): List<JokeEntity> =
    map { jokeDto ->
        JokeEntity(
            id = jokeDto.id,
            category = jokeDto.category,
            type = jokeDto.type,
            setup = jokeDto.setup,
            delivery = jokeDto.delivery,
            safe = jokeDto.safe,
            lang = jokeDto.lang
        )
    }
