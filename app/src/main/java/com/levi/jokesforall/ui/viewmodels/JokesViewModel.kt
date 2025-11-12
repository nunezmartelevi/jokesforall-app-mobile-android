package com.levi.jokesforall.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.levi.jokesforall.data.remote.Results
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.repository.PreferencesRepository
import com.levi.jokesforall.domain.repository.JokesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val _refreshJoke = MutableStateFlow<RefreshJokesState>(RefreshJokesState.CanRefresh)
    private val _shouldDisplayPunchline = MutableStateFlow(false)
    private var refreshJob: Job? = null

    val uiState: StateFlow<JokesScreenUiState> = toJokesScreenUiState(
        preferencesRepository = preferencesRepository,
        jokesRepository = jokesRepository,
        refreshJokesState = _refreshJoke,
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
        if (refreshJob != null) return

        _refreshJoke.value = RefreshJokesState.Refreshing
        refreshJob = viewModelScope.launch {
            when (jokesRepository.refreshJokes()) {
                is Results.Success -> _refreshJoke.value = RefreshJokesState.Success
                is Results.Error -> _refreshJoke.value = RefreshJokesState.Error
            }
            refreshJob = null
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
                _refreshJoke.value = RefreshJokesState.CanRefresh
            }
        }
    }

    fun toggleSound(isSoundOn: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setSoundOn(!isSoundOn)
        }
    }
}

private fun toJokesScreenUiState(
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
                shouldDisplayIntro = true,
                isSoundOn = preferences.isSoundOn
            )
        }

        if (jokes.isNotEmpty()) {
            return@combine JokesScreenUiState(
                currentJoke = jokes.firstOrNull(),
                shouldDisplayPunchline = shouldDisplayPunchline,
                isSoundOn = preferences.isSoundOn
            )
        }

        when (refreshState) {
            is RefreshJokesState.CanRefresh -> JokesScreenUiState(
                shouldRefresh = true,
                isSoundOn = preferences.isSoundOn
            )

            is RefreshJokesState.Refreshing -> JokesScreenUiState(
                isLoading = true,
                isSoundOn = preferences.isSoundOn
            )

            is RefreshJokesState.Success -> JokesScreenUiState(
                isSoundOn = preferences.isSoundOn
            )

            is RefreshJokesState.Error -> JokesScreenUiState(
                hasError = true,
                isSoundOn = preferences.isSoundOn
            )
        }
    }
}

data class JokesScreenUiState(
    val isSoundOn: Boolean,
    val shouldDisplayIntro: Boolean = false,
    val shouldRefresh: Boolean = false,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val currentJoke: Joke? = null,
    val shouldDisplayPunchline: Boolean = false
)

private sealed interface RefreshJokesState {
    data object CanRefresh : RefreshJokesState
    data object Refreshing : RefreshJokesState
    data object Success : RefreshJokesState
    data object Error : RefreshJokesState
}
