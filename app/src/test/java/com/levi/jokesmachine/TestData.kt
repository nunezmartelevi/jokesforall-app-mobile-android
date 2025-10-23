package com.levi.jokesmachine

import com.levi.jokesmachine.data.model.FlagsDto
import com.levi.jokesmachine.data.model.JokeDto
import com.levi.jokesmachine.domain.model.Joke
import com.levi.jokesmachine.domain.model.JokeType

val remoteJokes: List<JokeDto> = listOf(
    JokeDto(
        id = 135,
        category = "Pun",
        type = "twopart",
        joke = null,
        setup = "Why do Hong Kong cops like to go to work early?",
        delivery = "To beat the crowd.",
        safe = false,
        lang = "en",
        flags = FlagsDto(
            nsfw = false,
            religious = false,
            political = false,
            racist = false,
            sexist = false,
            explicit = true
        )
    ),
    JokeDto(
        id = 268,
        category = "Dark",
        type = "single",
        joke = "Hey girl are you a school? Because I want to shoot some kids up inside of you.",
        setup = null,
        delivery = null,
        safe = false,
        lang = "en",
        flags = FlagsDto(
            nsfw = true,
            religious = false,
            political = false,
            racist = false,
            sexist = false,
            explicit = false
        )
    )
)

val domainJokes: List<Joke> = listOf(
    Joke(
        id = 135,
        type = JokeType.TWOPART,
        singleText = null,
        setup = "Why do Hong Kong cops like to go to work early?",
        delivery = "To beat the crowd."
    ),
    Joke(
        id = 268,
        type = JokeType.SINGLE,
        singleText = "Hey girl are you a school? Because I want to shoot some kids up inside of you.",
        setup = null,
        delivery = null
    )
)
