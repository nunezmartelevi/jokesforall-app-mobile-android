package com.levi.jokesforall.ui.viewmodels

import com.levi.jokesforall.domain.model.JokeType
import com.levi.jokesforall.domain.repository.JokesRepository
import com.levi.jokesforall.jokes
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test


class JokesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    lateinit var repository: JokesRepository
    lateinit var viewModel: JokesViewModel

    @Test
    fun `nextJoke updates the UI state to SinglePartJoke`() = runTest {
        repository = FakeJokesRepository(type = JokeType.SINGLE)
        viewModel = JokesViewModel(repository)
        assert(viewModel.uiState.value is JokesScreenUiState.SinglePartJoke)
    }

    @Test
    fun `nextJoke updates the UI state to TwoPartJoke`() = runTest {
        repository = FakeJokesRepository(type = JokeType.TWOPART)
        viewModel = JokesViewModel(repository)
        assert(viewModel.uiState.value is JokesScreenUiState.TwoPartJoke)
    }

    @Test
    fun `nextJoke returns an error when repository fails`() = runTest {
        repository = FakeJokesRepository(isSuccessful = false)
        viewModel = JokesViewModel(repository)
        assert(viewModel.uiState.value is JokesScreenUiState.Error)
    }

    @Test
    fun `previousJoke updates the UI state with the previous joke`() = runTest {
        repository = FakeJokesRepository()
        viewModel = JokesViewModel(repository)
        // Advance until the second joke is loaded
        viewModel.nextJoke()
        val secondJokeId = jokes[1].id
        assert((viewModel.uiState.value as JokesScreenUiState.SinglePartJoke).joke.id == secondJokeId)
        // Go back to the first joke
        viewModel.previousJoke()
        val firstJokeId = jokes[0].id
        assert((viewModel.uiState.value as JokesScreenUiState.TwoPartJoke).joke.id == firstJokeId)
    }

    @Test
    fun `showDelivery updates the UI state to show the delivery`() {
        repository = FakeJokesRepository(type = JokeType.TWOPART)
        viewModel = JokesViewModel(repository)
        viewModel.showDelivery()
        assert((viewModel.uiState.value as JokesScreenUiState.TwoPartJoke).canShowDelivery)
    }
}