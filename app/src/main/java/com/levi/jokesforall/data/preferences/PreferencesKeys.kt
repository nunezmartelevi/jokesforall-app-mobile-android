package com.levi.jokesforall.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val HAS_SEEN_INTRO = booleanPreferencesKey("has_seen_intro")
    val IS_SOUND_ON = booleanPreferencesKey("is_sound_on")
}
