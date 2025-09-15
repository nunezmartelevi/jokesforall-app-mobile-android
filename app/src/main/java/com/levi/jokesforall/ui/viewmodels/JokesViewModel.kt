package com.levi.jokesforall.ui.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levi.jokesforall.data.remote.Result
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.repository.PreferencesRepository
import com.levi.jokesforall.domain.repository.JokesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class JokesViewModel @Inject constructor(
    private val jokesRepository: JokesRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _refreshJokeState = MutableStateFlow<RefreshJokesState>(RefreshJokesState.NotStarted)
    private val _shouldDisplayPunchline = MutableStateFlow(false)

    val uiState: StateFlow<JokesScreenUiState> = jokesScreenUiState(
        preferencesRepository = preferencesRepository,
        jokesRepository = jokesRepository,
        refreshJokesState = _refreshJokeState,
        shouldDisplayPunchline = _shouldDisplayPunchline
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = JokesScreenUiState(isSoundOn = false)
    )

    fun setHasSeenIntro() {
        viewModelScope.launch {
            preferencesRepository.setHasSeenIntro()
        }
    }

    fun refreshJokes() {
        viewModelScope.launch {
            when (jokesRepository.refreshJokes()) {
                is Result.Success -> _refreshJokeState.value = RefreshJokesState.NotStarted
                is Result.Error -> _refreshJokeState.value = RefreshJokesState.Error
            }
        }
    }

    fun displayPunchline() {
        _shouldDisplayPunchline.update { true }
    }

    fun hidePunchline() {
        _shouldDisplayPunchline.update { false }
    }

    fun nextJoke() {
        viewModelScope.launch {
            uiState.value.currentJoke?.let { joke ->
                jokesRepository.markJokeAsSeen(joke.id)
                hidePunchline()
            }
        }
    }

    fun toggleSound(isSoundOn: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setSoundOn(!isSoundOn)
        }
    }
}

private fun jokesScreenUiState(
    preferencesRepository: PreferencesRepository,
    jokesRepository: JokesRepository,
    refreshJokesState: MutableStateFlow<RefreshJokesState>,
    shouldDisplayPunchline: MutableStateFlow<Boolean>
): Flow<JokesScreenUiState> {

    val userPreferences = preferencesRepository.observeUserPreferences
    val jokes = jokesRepository.observeAllUnseenJokes

    return combine(
        userPreferences,
        jokes,
        refreshJokesState,
        shouldDisplayPunchline
    ) { preferences, jokes, refreshState, shouldDisplayPunchline ->

        if (!preferences.hasSeenIntro) {
            return@combine JokesScreenUiState(
                isSoundOn = preferences.isSoundOn,
                shouldDisplayIntro = true
            )
        }

        if (jokes.isNotEmpty()) {
            return@combine JokesScreenUiState(
                currentJoke = jokes.first(),
                shouldDisplayPunchline = shouldDisplayPunchline,
                isSoundOn = preferences.isSoundOn
            )
        }

        when (refreshState) {
            is RefreshJokesState.NotStarted -> JokesScreenUiState(
                shouldRefresh = true,
                isSoundOn = preferences.isSoundOn,
            )

            is RefreshJokesState.Error -> JokesScreenUiState(
                hasError = true,
                isSoundOn = preferences.isSoundOn
            )
        }
    }
}


private sealed interface RefreshJokesState {
    data object NotStarted : RefreshJokesState
    data object Error : RefreshJokesState
}

data class JokesScreenUiState(
    val isSoundOn: Boolean,
    val shouldDisplayIntro: Boolean = false,
    val shouldRefresh: Boolean = false,
    val hasError: Boolean = false,
    val shouldDisplayPunchline: Boolean = false,
    val currentJoke: Joke? = null
)
