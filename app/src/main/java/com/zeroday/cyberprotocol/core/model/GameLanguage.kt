package com.zeroday.cyberprotocol.core.model

enum class GameLanguage(val localeTag: String) {
    ENGLISH("en"), INDONESIAN("id");
    companion object {
        fun fromLocaleTag(value: String?): GameLanguage =
            entries.firstOrNull { it.localeTag == value } ?: ENGLISH
    }
}
