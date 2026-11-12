package com.zeroday.cyberprotocol.data

import android.content.Context
import com.zeroday.cyberprotocol.core.model.GameLanguage
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class GameLanguagePreferencesTest {
    @Test fun freshInstallDefaultsToEnglish() {
        val context = RuntimeEnvironment.getApplication() as Context
        context.getSharedPreferences("cp_language_preferences_v1", Context.MODE_PRIVATE).edit().clear().commit()
        assertEquals(GameLanguage.ENGLISH, GameLanguagePreferences.load(context))
    }

    @Test fun languageChoicePersistsOutsideCampaignPreferences() {
        val context = RuntimeEnvironment.getApplication() as Context
        val campaign = context.getSharedPreferences("cp_game_state_v1", Context.MODE_PRIVATE)
        campaign.edit().putString("snapshot", "campaign-save").commit()
        GameLanguagePreferences.save(context, GameLanguage.INDONESIAN)
        assertEquals(GameLanguage.INDONESIAN, GameLanguagePreferences.load(context))
        assertEquals("campaign-save", campaign.getString("snapshot", null))
    }
}
