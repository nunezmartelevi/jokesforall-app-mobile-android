package com.levi.jokesforall.datas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.levi.jokesforall.domains.model.Joke
import com.levi.jokesforall.domains.model.JokeType

@Entity(tableName = "jokes")
data class JokeEntity(
    @PrimaryKey val id: Int,
    val category: String,
    val type: String,
    val joke: String?,
    val setup: String?,
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
            singleText = jokeEntity.joke,
            delivery = jokeEntity.delivery
        )
    }
