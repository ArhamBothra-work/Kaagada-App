package com.example.kaagada.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kaagada.data.local.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val preferenceManager = PreferenceManager(application)

    private val _isLoggedIn = MutableStateFlow(preferenceManager.isLoggedIn)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _username = MutableStateFlow(preferenceManager.username ?: "Guest")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow(preferenceManager.email ?: "")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phone = MutableStateFlow(preferenceManager.phone ?: "")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _hasCompletedOnboarding = MutableStateFlow(preferenceManager.hasCompletedOnboarding)
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    private val _alphabetsLearned = MutableStateFlow(preferenceManager.alphabetsLearnedCount)
    val alphabetsLearned: StateFlow<Int> = _alphabetsLearned.asStateFlow()

    private val _phrasesLearned = MutableStateFlow(preferenceManager.phrasesLearnedCount)
    val phrasesLearned: StateFlow<Int> = _phrasesLearned.asStateFlow()

    fun signup(username: String, password: String, email: String = "", phone: String = ""): Boolean {
        if (username.isBlank() || password.isBlank()) return false
        preferenceManager.username = username
        _username.value = username
        preferenceManager.password = password
        preferenceManager.email = email
        _email.value = email
        preferenceManager.phone = phone
        _phone.value = phone
        preferenceManager.isLoggedIn = true
        _isLoggedIn.value = true
        return true
    }

    fun updateProfile(username: String, email: String, phone: String) {
        preferenceManager.username = username
        _username.value = username
        preferenceManager.email = email
        _email.value = email
        preferenceManager.phone = phone
        _phone.value = phone
    }

    fun login(username: String, password: String): Boolean {
        if (username == preferenceManager.username && password == preferenceManager.password) {
            _username.value = username
            preferenceManager.isLoggedIn = true
            _isLoggedIn.value = true
            return true
        }
        return false
    }

    fun completeOnboarding() {
        preferenceManager.hasCompletedOnboarding = true
        _hasCompletedOnboarding.value = true
    }

    fun logout() {
        preferenceManager.isLoggedIn = false
        _isLoggedIn.value = false
    }

    fun updateAlphabetProgress(count: Int) {
        preferenceManager.alphabetsLearnedCount = count
        _alphabetsLearned.value = count
    }

    fun updatePhraseProgress(count: Int) {
        preferenceManager.phrasesLearnedCount = count
        _phrasesLearned.value = count
    }

    // Add this function to AuthViewModel.kt
    fun verifyAndChangePassword(currentInput: String, newPassword: String): Boolean {
        // Check if the input matches the stored password
        return if (currentInput == preferenceManager.password) {
            preferenceManager.password = newPassword
            true // Success
        } else {
            false // Failed verification
        }
    }

    // Fixed updatePassword function
    fun updatePassword(password: String) {
        viewModelScope.launch {
            try {
                // Use your existing preferenceManager variable
                // This matches the style of your signup and updateProfile functions
                preferenceManager.password = password
                println("Password successfully updated")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}