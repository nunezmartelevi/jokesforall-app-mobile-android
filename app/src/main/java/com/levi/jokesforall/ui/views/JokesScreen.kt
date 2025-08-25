package com.levi.jokesforall.ui.views

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levi.jokesforall.ui.viewmodels.JokesScreenUiState.Error
import com.levi.jokesforall.ui.viewmodels.JokesScreenUiState.Loading
import com.levi.jokesforall.ui.viewmodels.JokesScreenUiState.SinglePartJoke
import com.levi.jokesforall.ui.viewmodels.JokesScreenUiState.TwoPartJoke
import com.levi.jokesforall.ui.viewmodels.JokesViewModel
import com.levi.jokesforall.ui.views.jokeviews.ErrorView
import com.levi.jokesforall.ui.views.jokeviews.LoadingView
import com.levi.jokesforall.ui.views.jokeviews.SinglePartJokeView
import com.levi.jokesforall.ui.views.jokeviews.TwoPartJokeView

@Composable
fun JokesScreen(viewModel: JokesViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSoundOn = viewModel.isSoundOn

    BoxWithConstraints {
        when (val state = uiState) {
            is Loading -> {
                LoadingView(
                    isSoundOn = isSoundOn
                )
            }

            is Error -> {
                ErrorView(
                    isSoundOn = isSoundOn,
                    onRetry = viewModel::nextJoke
                )
            }

            is SinglePartJoke -> {
                SinglePartJokeView(
                    isSoundOn = isSoundOn,
                    joke = state.joke,
                    canGoBack = state.canGoBack,
                    onNextJoke = viewModel::nextJoke,
                    onPreviousJoke = viewModel::previousJoke,
                    onToggleSound = viewModel::toggleSound
                )
            }

            is TwoPartJoke -> {
                TwoPartJokeView(
                    isSoundOn = viewModel.isSoundOn,
                    joke = state.joke,
                    canShowDelivery = state.canShowDelivery,
                    canGoBack = state.canGoBack,
                    onNextJoke = viewModel::nextJoke,
                    onPreviousJoke = viewModel::previousJoke,
                    onShowDelivery = viewModel::showDelivery,
                    onToggleSound = viewModel::toggleSound
                )
            }
        }
    }
}
