package com.levi.jokesforall.ui.views

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levi.jokesforall.ui.viewmodels.JokesViewModel

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
            when {
                shouldDisplayIntro -> {
                    IntroView(
                        isSoundOn = isSoundOn,
                        onContinuePress = viewModel::setHasSeenIntro,
                        onToggleSound = viewModel::toggleSound
                    )
                }

                isLoading -> {
                    LoadingView(
                        isSoundOn = isSoundOn,
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

                else -> {
                    EmptyView(
                        isSoundOn = isSoundOn,
                        onToggleSound = viewModel::toggleSound
                    )
                }
            }
        }
    }
}
