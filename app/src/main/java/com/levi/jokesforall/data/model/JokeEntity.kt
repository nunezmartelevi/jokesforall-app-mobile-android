package com.levi.jokesforall.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.model.JokeType

@Entity(tableName = "jokes")
data class JokeEntity(
    @PrimaryKey val id: Int,
    val category: String,
    val type: String,
    val setup: String,
    val delivery: String?,
    val safe: Boolean,
    val lang: String,
    val seen: Boolean
)

fun List<JokeEntity>.asDomainModel(): List<Joke> =
    map { jokeEntity ->
        Joke(
            id = jokeEntity.id,
            type = JokeType.fromValue(jokeEntity.type)
                ?: if (jokeEntity.delivery != null) JokeType.TWOPART else JokeType.SINGLE,
            setup = jokeEntity.setup,
            delivery = jokeEntity.delivery
        )
    }
