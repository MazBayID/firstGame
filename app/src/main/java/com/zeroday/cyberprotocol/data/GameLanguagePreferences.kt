package com.zeroday.cyberprotocol.data

import android.content.Context
import com.zeroday.cyberprotocol.core.model.GameLanguage

/** Language preference is intentionally stored outside the campaign save. */
object GameLanguagePreferences {
    private const val STORE_NAME = "cp_language_preferences_v1"
    private const val KEY_LANGUAGE = "selected_language"

    fun load(context: Context): GameLanguage {
        val value = context.applicationContext
            .getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, GameLanguage.ENGLISH.localeTag)
        return GameLanguage.fromLocaleTag(value)
    }

    fun save(context: Context, language: GameLanguage) {
        context.applicationContext
            .getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_LANGUAGE, language.localeTag).apply()
    }
}
