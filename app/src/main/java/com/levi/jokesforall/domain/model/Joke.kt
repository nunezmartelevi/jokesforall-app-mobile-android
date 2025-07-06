package com.levi.jokesforall.domain.model

data class Joke(
    val id: Int,
    val type: JokeType,
    val setup: String,
    val delivery: String?
)

enum class JokeType(val value: String) {
    SINGLE("single"),
    TWOPART("twopart");

    companion object {
        fun fromValue(value: String) = entries.firstOrNull { it.value == value }
    }
}
