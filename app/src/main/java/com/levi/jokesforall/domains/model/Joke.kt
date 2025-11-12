package com.levi.jokesforall.domains.model

data class Joke(
    val id: Int,
    val type: JokeType,
    private val singleText: String?,
    private val setup: String?,
    val delivery: String?
) {
    val startingText: String?
        get() = when (type) {
            JokeType.SINGLE -> singleText
            JokeType.TWOPART -> setup
        }
}

enum class JokeType(val value: String) {
    SINGLE("single"),
    TWOPART("twopart");

    companion object {
        fun fromValue(value: String) = entries.firstOrNull { it.value == value }
    }
}
