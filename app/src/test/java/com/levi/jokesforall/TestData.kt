package com.levi.jokesforall

import com.levi.jokesforall.data.model.FlagsDto
import com.levi.jokesforall.data.model.JokeDto
import com.levi.jokesforall.data.model.JokeEntity
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.model.JokeType

val remoteJokes: List<JokeDto> = listOf(
    JokeDto(
        id = 135,
        category = "Pun",
        type = "twopart",
        setup = "Why do Hong Kong cops like to go to work early?",
        delivery = "To beat the crowd.",
        safe = false,
        lang = "en",
        flags = FlagsDto(false, false, false, false, false, true)
    ),
    JokeDto(
        id = 268,
        category = "Dark",
        type = "single",
        setup = "Hey girl are you a school? Because I want to shoot some kids up inside of you.",
        delivery = null,
        safe = false,
        lang = "en",
        flags = FlagsDto(true, false, false, false, false, false)
    )
)

val localJokes: List<JokeEntity> = listOf(
    JokeEntity(
        id = 135,
        category = "Pun",
        type = "twopart",
        setup = "Why do Hong Kong cops like to go to work early?",
        delivery = "To beat the crowd.",
        safe = false,
        lang = "en",
        seen = false
    ),
    JokeEntity(
        id = 268,
        category = "Dark",
        type = "single",
        setup = "Hey girl are you a school? Because I want to shoot some kids up inside of you.",
        delivery = null,
        safe = false,
        lang = "en",
        seen = false
    )
)

val jokes: List<Joke> = listOf(
    Joke(
        id = 135,
        type = JokeType.TWOPART,
        setup = "Why do Hong Kong cops like to go to work early?",
        delivery = "To beat the crowd."
    ),
    Joke(
        id = 268,
        type = JokeType.SINGLE,
        setup = "Hey girl are you a school? Because I want to shoot some kids up inside of you.",
        delivery = null
    )
)

val singlePartJokes: List<Joke> = jokes.filter { it.type == JokeType.SINGLE }
val twoPartJokes: List<Joke> = jokes.filter { it.type == JokeType.TWOPART }
