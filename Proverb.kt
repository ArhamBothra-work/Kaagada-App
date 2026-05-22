package com.example.kaagada.data.model

// ENHANCED: Added 'meaning' and 'translatedText' fields to support
// both the API response shape and the new AI translation feature.
data class Proverb(
    val text: String,
    val meaning: String = "",          // English meaning/gloss from API
    val translatedText: String = ""    // AI-generated translation (populated at runtime)
)
