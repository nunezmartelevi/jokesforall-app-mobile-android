package com.levi.jokesforall.data

import com.levi.jokesforall.data.model.FlagsDto
import com.levi.jokesforall.data.model.JokeDto

val jokesData: List<JokeDto> = listOf(
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
    ),
    JokeDto(
        id = 173,
        category = "Dark",
        type = "twopart",
        setup = "What is the difference between a pizza and a black man?",
        delivery = "A pizza can feed a family of five.",
        safe = false,
        lang = "en",
        flags = FlagsDto(false, false, false, true, false, false)
    ),
    JokeDto(
        id = 216,
        category = "Misc",
        type = "twopart",
        setup = "How do construction workers party?",
        delivery = "They raise the roof.",
        safe = true,
        lang = "en",
        flags = FlagsDto(false, false, false, false, false, false)
    ),
    JokeDto(
        id = 116,
        category = "Dark",
        type = "twopart",
        setup = "Dark humor is like cancer.",
        delivery = "It's funnier when children get it.",
        safe = false,
        lang = "en",
        flags = FlagsDto(false, false, false, false, false, false)
    ),
    JokeDto(
        id = 87,
        category = "Misc",
        type = "twopart",
        setup = "Mom asked me where I'm taking her to go out to eat for mother's day.",
        delivery = "I told her, \"We already have food in the house\".",
        safe = true,
        lang = "en",
        flags = FlagsDto(false, false, false, false, false, false)
    ),
    JokeDto(
        id = 218,
        category = "Dark",
        type = "twopart",
        setup = "What's the difference between Harry Potter and the jews?",
        delivery = "Harry escaped the chamber.",
        safe = false,
        lang = "en",
        flags = FlagsDto(false, false, false, true, false, false)
    ),
    JokeDto(
        id = 154,
        category = "Pun",
        type = "twopart",
        setup = "What do you call a deaf gynecologist?",
        delivery = "A lip reader.",
        safe = false,
        lang = "en",
        flags = FlagsDto(true, false, false, false, false, true)
    ),
    JokeDto(
        id = 213,
        category = "Programming",
        type = "twopart",
        setup = "Why did the programmer jump on the table?",
        delivery = "Because debug was on his screen.",
        safe = true,
        lang = "en",
        flags = FlagsDto(false, false, false, false, false, false)
    ),
    JokeDto(
        id = 284,
        category = "Pun",
        type = "twopart",
        setup = "Which part of the hospital has the least privacy?",
        delivery = "The ICU.",
        safe = true,
        lang = "en",
        flags = FlagsDto(false, false, false, false, false, false)
    )
).sortedBy { it.id }
