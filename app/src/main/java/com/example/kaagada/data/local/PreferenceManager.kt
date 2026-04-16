package com.example.kaagada.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("kaagada_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_PASSWORD = "password"
        private const val KEY_HAS_COMPLETED_ONBOARDING = "has_completed_onboarding"
        private const val KEY_ALPHABETS_LEARNED = "alphabets_learned"
        private const val KEY_PHRASES_LEARNED = "phrases_learned"
    }

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = sharedPreferences.edit { putBoolean(KEY_IS_LOGGED_IN, value) }

    var username: String?
        get() = sharedPreferences.getString(KEY_USERNAME, null)
        set(value) = sharedPreferences.edit { putString(KEY_USERNAME, value) }

    var email: String?
        get() = sharedPreferences.getString(KEY_EMAIL, null)
        set(value) = sharedPreferences.edit { putString(KEY_EMAIL, value) }

    var phone: String?
        get() = sharedPreferences.getString(KEY_PHONE, null)
        set(value) = sharedPreferences.edit { putString(KEY_PHONE, value) }

    var password: String?
        get() = sharedPreferences.getString(KEY_PASSWORD, null)
        set(value) = sharedPreferences.edit { putString(KEY_PASSWORD, value) }

    var hasCompletedOnboarding: Boolean
        get() = sharedPreferences.getBoolean(KEY_HAS_COMPLETED_ONBOARDING, false)
        set(value) = sharedPreferences.edit { putBoolean(KEY_HAS_COMPLETED_ONBOARDING, value) }

    var alphabetsLearnedCount: Int
        get() = sharedPreferences.getInt(KEY_ALPHABETS_LEARNED, 0)
        set(value) = sharedPreferences.edit { putInt(KEY_ALPHABETS_LEARNED, value) }

    var phrasesLearnedCount: Int
        get() = sharedPreferences.getInt(KEY_PHRASES_LEARNED, 0)
        set(value) = sharedPreferences.edit { putInt(KEY_PHRASES_LEARNED, value) }

    fun clearSession() {
        sharedPreferences.edit { remove(KEY_IS_LOGGED_IN) }
    }
}
