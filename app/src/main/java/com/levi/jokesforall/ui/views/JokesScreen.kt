package com.levi.jokesforall.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levi.jokesforall.ui.viewmodels.JokesViewModel
import com.levi.jokesforall.ui.views.jokeviews.ErrorView
import com.levi.jokesforall.ui.views.jokeviews.IntroView
import com.levi.jokesforall.ui.views.jokeviews.LoadingView
import com.levi.jokesforall.ui.views.jokeviews.JokeView


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun JokesScreen(viewModel: JokesViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState.shouldRefresh) {
            viewModel.refreshJokes()
        }
    }

    BoxWithConstraints {
        with(uiState) {
            when  {
                shouldDisplayIntro -> {
                    IntroView(
                        isSoundOn = isSoundOn,
                        onContinuePress = viewModel::setHasSeenIntro,
                        onToggleSound = viewModel::toggleSound
                    )
                }

                currentJoke != null -> {
                    JokeView(
                        isSoundOn = isSoundOn,
                        joke = currentJoke,
                        shouldDisplayPunchline = shouldDisplayPunchline,
                        onNextJoke = viewModel::nextJoke,
                        onDisplayPunchline = viewModel::displayPunchline,
                        onHidePunchline = viewModel::hidePunchline,
                        onToggleSound = viewModel::toggleSound
                    )
                }

                hasError -> {
                    ErrorView(
                        isSoundOn = isSoundOn,
                        onToggleSound = viewModel::toggleSound,
                        onRetry = viewModel::refreshJokes
                    )
                }

                else -> {
                    LoadingView(
                        isSoundOn = isSoundOn,
                        onToggleSound = viewModel::toggleSound
                    )
                }
            }
        }
    }
}
