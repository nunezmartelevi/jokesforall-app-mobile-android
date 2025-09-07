package com.levi.jokesforall.domain.repository

import com.levi.jokesforall.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing user preferences.
 * This interface provides methods to observe and update user-specific settings,
 * such as whether the intro has been seen or if the sound is enabled.
 */
interface PreferencesRepository {
    val observeUserPreferences: Flow<UserPreferences>
    suspend fun setHasSeenIntro()
    suspend fun setSoundOn(isSoundOn: Boolean)
}
