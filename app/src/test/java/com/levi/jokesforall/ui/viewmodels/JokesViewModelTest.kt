package com.levi.jokesforall.ui.viewmodels

import com.levi.jokesforall.domain.repository.JokesRepository
import com.levi.jokesforall.domain.repository.PreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNotSame
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class JokesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    lateinit var jokesRepository: JokesRepository
    lateinit var preferencesRepository: PreferencesRepository
    lateinit var viewModel: JokesViewModel

    @Before
    fun setup() {
        jokesRepository = FakeJokesRepository()
        preferencesRepository = FakeUserPreferencesRepository()
        viewModel = JokesViewModel(jokesRepository, preferencesRepository)
    }

    @Test
    fun `setHasSeenIntro updates shouldDisplayIntro to false`() = runTest {
        viewModel = JokesViewModel(
            jokesRepository,
            FakeUserPreferencesRepository(false)
        )
        val initialState = viewModel.uiState.first()
        assertTrue(initialState.shouldDisplayIntro)
        viewModel.setHasSeenIntro()
        advanceUntilIdle()
        val updatedState = viewModel.uiState.first()
        assertFalse(updatedState.shouldDisplayIntro)
    }

    @Test
    fun `refreshJokes sets currentJoke`() = runTest {
        viewModel.refreshJokes()
        val uiState = viewModel.uiState.first()
        assertNotNull(uiState.currentJoke)
    }

    @Test
    fun `refreshJokes when result is error set RefreshJokeState to Error`() = runTest {
        viewModel = JokesViewModel(
            FakeJokesRepository(false),
            preferencesRepository
        )
        viewModel.refreshJokes()
        val uiState = viewModel.uiState.first()
        assertTrue(uiState.hasError)
    }

    @Test
    fun `displayPunchline sets shouldDisplayPunchline to true`() = runTest {
        viewModel.refreshJokes()
        viewModel.displayPunchline()
        val uiState = viewModel.uiState.first()
        assertTrue(uiState.shouldDisplayPunchline)
    }

    @Test
    fun `hidePunchline sets shouldDisplayPunchline to false`() = runTest {
        viewModel.refreshJokes()
        viewModel.displayPunchline()
        val initialState = viewModel.uiState.first()
        assertTrue(initialState.shouldDisplayPunchline)
        viewModel.hidePunchline()
        val updatedState = viewModel.uiState.first()
        assertFalse(updatedState.shouldDisplayPunchline)
    }

    @Test
    fun `nextJoke updates currentJoke`() = runTest {
        viewModel.refreshJokes()
        val initialState = viewModel.uiState.first()
        viewModel.nextJoke()
        advanceUntilIdle()
        val updatedState = viewModel.uiState.first()
        assertNotSame(initialState.currentJoke, updatedState.currentJoke)
    }

    @Test
    fun `toggleSound updates isSoundOn`() = runTest {
        val initialState = viewModel.uiState.first()
        assertTrue(initialState.isSoundOn)
        viewModel.toggleSound(true)
        advanceUntilIdle()
        val updatedState = viewModel.uiState.first()
        assertFalse(updatedState.isSoundOn)
    }
}
