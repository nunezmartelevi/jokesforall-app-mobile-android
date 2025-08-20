package com.levi.jokesforall.ui.views

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.model.JokeType

class JokesPreviewParameterProvider : PreviewParameterProvider<List<Joke>> {
    override val values: Sequence<List<Joke>> = sequenceOf(data.jokes)
}

private object data {
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
}
