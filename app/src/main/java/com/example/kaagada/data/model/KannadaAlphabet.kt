package com.example.kaagada.data.model

data class KannadaAlphabet(
    val char: String,
    val pronunciation: String,
    val type: AlphabetType
)

enum class AlphabetType {
    SWARA, VYANJANA
}

object AlphabetProvider {
    val swaras = listOf(
        KannadaAlphabet("ಅ", "a", AlphabetType.SWARA),
        KannadaAlphabet("ಆ", "aa", AlphabetType.SWARA),
        KannadaAlphabet("ಇ", "i", AlphabetType.SWARA),
        KannadaAlphabet("ಈ", "ee", AlphabetType.SWARA),
        KannadaAlphabet("ಉ", "u", AlphabetType.SWARA),
        KannadaAlphabet("ಊ", "oo", AlphabetType.SWARA),
        KannadaAlphabet("ಋ", "ru", AlphabetType.SWARA),
        KannadaAlphabet("ಎ", "e", AlphabetType.SWARA),
        KannadaAlphabet("ಏ", "ae", AlphabetType.SWARA),
        KannadaAlphabet("ಐ", "ai", AlphabetType.SWARA),
        KannadaAlphabet("ಒ", "o", AlphabetType.SWARA),
        KannadaAlphabet("ಓ", "oo", AlphabetType.SWARA),
        KannadaAlphabet("ಔ", "au", AlphabetType.SWARA),
        KannadaAlphabet("ಅಂ", "am", AlphabetType.SWARA),
        KannadaAlphabet("ಅಃ", "aha", AlphabetType.SWARA)
    )

    val vyanjanas = listOf(
        KannadaAlphabet("ಕ", "ka", AlphabetType.VYANJANA),
        KannadaAlphabet("ಖ", "kha", AlphabetType.VYANJANA),
        KannadaAlphabet("ಗ", "ga", AlphabetType.VYANJANA),
        KannadaAlphabet("ಘ", "gha", AlphabetType.VYANJANA),
        KannadaAlphabet("ಙ", "nga", AlphabetType.VYANJANA),
        KannadaAlphabet("ಚ", "cha", AlphabetType.VYANJANA),
        KannadaAlphabet("ಛ", "chha", AlphabetType.VYANJANA),
        KannadaAlphabet("ಜ", "ja", AlphabetType.VYANJANA),
        KannadaAlphabet("ಝ", "jha", AlphabetType.VYANJANA),
        KannadaAlphabet("ಞ", "nya", AlphabetType.VYANJANA),
        KannadaAlphabet("ಟ", "ta", AlphabetType.VYANJANA),
        KannadaAlphabet("ಠ", "tha", AlphabetType.VYANJANA),
        KannadaAlphabet("ಡ", "da", AlphabetType.VYANJANA),
        KannadaAlphabet("ಢ", "dha", AlphabetType.VYANJANA),
        KannadaAlphabet("ಣ", "na", AlphabetType.VYANJANA)
    )

    val allAlphabets = swaras + vyanjanas
}
