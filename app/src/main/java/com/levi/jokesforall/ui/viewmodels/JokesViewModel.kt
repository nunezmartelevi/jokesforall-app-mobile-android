package com.levi.jokesforall.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levi.jokesforall.data.remote.Result
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.model.JokeType
import com.levi.jokesforall.domain.repository.JokesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JokesViewModel @Inject constructor(private val repository: JokesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<JokesScreenUiState>(JokesScreenUiState.Loading)
    val uiState: StateFlow<JokesScreenUiState> = _uiState.asStateFlow()

    private var currentJokeIndex = -1
    private val jokes = mutableListOf<Joke>()

    val batteryLevel = maxOf(Math.random() * 100, 50.0).toInt()
    val isSoundOn = true

    init {
        nextJoke()
    }

    fun nextJoke() {
        viewModelScope.launch {
            currentJokeIndex++
            if (jokes.isEmpty() || currentJokeIndex >= jokes.size) {
                refreshJokes()
            } else {
                _uiState.update { state ->
                    val currentJoke = jokes[currentJokeIndex]
                    repository.markJokeAsSeen(currentJoke.id)
                    uiStateFromJokeType(currentJoke, currentJokeIndex > 0)
                }
            }
        }
    }

    private suspend fun refreshJokes() {
        _uiState.update { JokesScreenUiState.Loading }
        when (val result = repository.getJokes()) {
            is Result.Success -> {
                _uiState.update {
                    jokes.clear()
                    jokes.addAll(result.data)
                    currentJokeIndex = 0
                    val currentJoke = jokes[currentJokeIndex]
                    repository.markJokeAsSeen(currentJoke.id)
                    uiStateFromJokeType(currentJoke)
                }
            }

            is Result.Error -> {
                _uiState.update { JokesScreenUiState.Error }
            }
        }
    }

    fun previousJoke() {
        if (currentJokeIndex <= 0) {
            return
        }
        currentJokeIndex--
        _uiState.update { state ->
            val joke = jokes[currentJokeIndex]
            uiStateFromJokeType(joke, currentJokeIndex > 0)
        }
    }

    fun showDelivery() {
        _uiState.update { state ->
            if (state is JokesScreenUiState.TwoPartJoke) {
                state.copy(canShowDelivery = true)
            } else {
                state
            }
        }
    }

    fun toggleSound() {
        // TODO: implement toggle sound
    }

}

sealed interface JokesScreenUiState {
    val canGoBack: Boolean

    data object Loading : JokesScreenUiState {
        override val canGoBack: Boolean = false
    }

    data object Error : JokesScreenUiState {
        override val canGoBack: Boolean = false
    }

    data class SinglePartJoke(
        val joke: Joke,
        override val canGoBack: Boolean = false
    ) : JokesScreenUiState

    data class TwoPartJoke(
        val joke: Joke,
        val canShowDelivery: Boolean = false,
        override val canGoBack: Boolean = false
    ) : JokesScreenUiState
}

private fun uiStateFromJokeType(joke: Joke, canGoBack: Boolean = false): JokesScreenUiState =
    when (joke.type) {
        JokeType.SINGLE -> JokesScreenUiState.SinglePartJoke(joke = joke, canGoBack = canGoBack)
        JokeType.TWOPART -> JokesScreenUiState.TwoPartJoke(joke = joke, canGoBack = canGoBack)
    }
