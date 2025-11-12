package com.levi.jokesforall.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.levi.jokesforall.data.preferences.PreferencesKeys.HAS_SEEN_INTRO
import com.levi.jokesforall.data.preferences.PreferencesKeys.IS_SOUND_ON
import com.levi.jokesforall.domain.model.UserPreferences
import com.levi.jokesforall.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(private val preferences: DataStore<Preferences>) :
    PreferencesRepository {
    override val observeUserPreferences: Flow<UserPreferences> =
        preferences.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            UserPreferences(
                hasSeenIntro = preferences[HAS_SEEN_INTRO] ?: false,
                isSoundOn = preferences[IS_SOUND_ON] ?: true
            )
        }

    override suspend fun setHasSeenIntro() {
        preferences.edit { settings ->
            settings[HAS_SEEN_INTRO] = true
        }
    }

    override suspend fun setSoundOn(isSoundOn: Boolean) {
        preferences.edit { settings ->
            settings[IS_SOUND_ON] = isSoundOn
        }
    }
}
