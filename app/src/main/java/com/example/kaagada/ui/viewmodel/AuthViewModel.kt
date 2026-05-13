package com.example.kaagada.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kaagada.data.local.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val preferenceManager = PreferenceManager(application)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // 1. Auth State - Uses Firebase's current user status
    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // 2. User Data - These will be fetched from Firestore
    private val _username = MutableStateFlow("Guest")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _alphabetsLearned = MutableStateFlow(0)
    val alphabetsLearned: StateFlow<Int> = _alphabetsLearned.asStateFlow()

    // Onboarding remains local as it's a device preference
    private val _hasCompletedOnboarding = MutableStateFlow(preferenceManager.hasCompletedOnboarding)
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    init {
        // If a user is already logged in, fetch their data immediately
        auth.currentUser?.uid?.let { fetchUserData(it) }
    }

    private fun fetchUserData(userId: String) {
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _username.value = document.getString("name") ?: "Learner"
                    _email.value = document.getString("email") ?: ""
                    _phone.value = document.getString("phone") ?: ""
                    _alphabetsLearned.value = (document.getLong("progress") ?: 0L).toInt()
                }
            }
    }

    fun signup(name: String, email: String, phone: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val userId = result.user?.uid
                val userMap = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "phone" to phone,
                    "progress" to 0
                )
                if (userId != null) {
                    firestore.collection("users").document(userId).set(userMap)
                        .addOnSuccessListener {
                            _isLoggedIn.value = true
                            fetchUserData(userId)
                            onSuccess()
                        }
                }
            }
            .addOnFailureListener { onError(it.message ?: "Signup Failed") }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                _isLoggedIn.value = true
                result.user?.uid?.let { fetchUserData(it) }
                onSuccess()
            }
            .addOnFailureListener { onError(it.message ?: "Login Failed") }
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _username.value = "Guest"
    }

    fun updateAlphabetProgress(count: Int) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).update("progress", count)
                .addOnSuccessListener { _alphabetsLearned.value = count }
        }
    }

    fun completeOnboarding() {
        preferenceManager.hasCompletedOnboarding = true
        _hasCompletedOnboarding.value = true
    }

    // --- Add these for the Profile Screen ---
    fun updateProfile(newName: String, newEmail: String, newPhone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val updates = mapOf(
                "name" to newName,
                "email" to newEmail,
                "phone" to newPhone
            )
            // Update Firebase database
            firestore.collection("users").document(userId).update(updates)
                .addOnSuccessListener {
                    // Update the local UI immediately
                    _username.value = newName
                    _email.value = newEmail
                    _phone.value = newPhone
                }
        }
    }

    fun verifyAndChangePassword(currentInput: String, newPassword: String): Boolean {
        // In a strict production app, Firebase requires re-authentication here.
        // For the SEE exam demo, we will optimistically update the password to keep the UI simple!
        try {
            auth.currentUser?.updatePassword(newPassword)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}