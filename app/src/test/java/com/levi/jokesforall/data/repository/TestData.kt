package com.levi.jokesforall.data.repository

import com.levi.jokesforall.data.model.FlagsDto
import com.levi.jokesforall.data.model.JokeDto

val jokesTestData: List<JokeDto> = listOf(
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
