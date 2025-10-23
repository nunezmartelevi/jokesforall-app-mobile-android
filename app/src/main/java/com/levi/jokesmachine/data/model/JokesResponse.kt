package com.levi.jokesmachine.data.model

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
    val joke: String? = null,
    val setup: String? = null,
    val delivery: String? = null,
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
            joke = jokeDto.joke,
            setup = jokeDto.setup,
            delivery = jokeDto.delivery,
            safe = jokeDto.safe,
            lang = jokeDto.lang,
            seen = false
        )
    }
