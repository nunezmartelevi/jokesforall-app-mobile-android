package com.levi.jokesforall

import com.levi.jokesforall.data.model.JokeEntity

val jokesEntities = listOf(
    JokeEntity(
        id = 135,
        category = "Pun",
        type = "twopart",
        joke = null,
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
        joke = "Hey girl are you a school? Because I want to shoot some kids up inside of you.",
        setup = null,
        delivery = null,
        safe = false,
        lang = "en",
        seen = false
    )
)
