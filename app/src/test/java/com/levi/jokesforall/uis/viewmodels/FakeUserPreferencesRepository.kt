package com.levi.jokesforall.uis.viewmodels

import com.levi.jokesforall.domains.model.UserPreferences
import com.levi.jokesforall.domains.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserPreferencesRepository(hasSeenIntro: Boolean = true) : PreferencesRepository {
    private var inMemoryPreferences: UserPreferences = UserPreferences(
        hasSeenIntro = hasSeenIntro,
        isSoundOn = true
    )

    override val observeUserPreferences: Flow<UserPreferences> = flow {
        emit(inMemoryPreferences)
    }

    override suspend fun setHasSeenIntro() {
        inMemoryPreferences = inMemoryPreferences.copy(hasSeenIntro = true)
    }

    override suspend fun setSoundOn(isSoundOn: Boolean) {
        inMemoryPreferences = inMemoryPreferences.copy(isSoundOn = isSoundOn)
    }
}
