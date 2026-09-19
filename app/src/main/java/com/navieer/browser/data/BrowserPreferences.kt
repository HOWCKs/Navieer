package com.navieer.browser.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "navieer_settings")

enum class SearchEngine(val title: String, val searchUrl: String) {
    DUCKDUCKGO("DuckDuckGo", "https://duckduckgo.com/?q=%s"),
    GOOGLE("Google", "https://www.google.com/search?q=%s"),
    BRAVE("Brave Search", "https://search.brave.com/search?q=%s"),
    ECOSIA("Ecosia", "https://www.ecosia.org/search?q=%s"),
    QWANT("Qwant", "https://www.qwant.com/?q=%s"),
    CUSTOM("Personalizado", "")
}

class BrowserPreferences(private val context: Context) {
    companion object {
        val KEY_SEARCH_ENGINE = stringPreferencesKey("search_engine")
        val KEY_CUSTOM_SEARCH_URL = stringPreferencesKey("custom_search_url")
        val KEY_AMOLED_MODE = booleanPreferencesKey("amoled_mode")
        val KEY_ADBLOCK_ENABLED = booleanPreferencesKey("adblock_enabled")
        val KEY_DESKTOP_MODE = booleanPreferencesKey("desktop_mode")
        val KEY_SERVO_EXPERIMENTAL = booleanPreferencesKey("servo_experimental")
        val KEY_SERVO_WEBGPU = booleanPreferencesKey("servo_webgpu")
        val KEY_FORCE_DARK_MODE = booleanPreferencesKey("force_dark_mode")
    }

    val searchEngineFlow: Flow<SearchEngine> = context.dataStore.data.map { prefs ->
        val name = prefs[KEY_SEARCH_ENGINE] ?: SearchEngine.DUCKDUCKGO.name
        try {
            SearchEngine.valueOf(name)
        } catch (e: Exception) {
            SearchEngine.DUCKDUCKGO
        }
    }

    val customSearchUrlFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_CUSTOM_SEARCH_URL] ?: "https://duckduckgo.com/?q=%s"
    }

    val amoledModeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_AMOLED_MODE] ?: true // Default to true for sleek OLED/AMOLED experience
    }

    val adBlockEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_ADBLOCK_ENABLED] ?: true
    }

    val desktopModeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_DESKTOP_MODE] ?: false
    }

    val servoExperimentalFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_SERVO_EXPERIMENTAL] ?: false
    }

    val servoWebGpuFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_SERVO_WEBGPU] ?: false
    }

    val forceDarkModeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_FORCE_DARK_MODE] ?: false
    }

    suspend fun setSearchEngine(engine: SearchEngine) {
        context.dataStore.edit { it[KEY_SEARCH_ENGINE] = engine.name }
    }

    suspend fun setCustomSearchUrl(url: String) {
        context.dataStore.edit { it[KEY_CUSTOM_SEARCH_URL] = url }
    }

    suspend fun setAmoledMode(enabled: Boolean) {
        context.dataStore.edit { it[KEY_AMOLED_MODE] = enabled }
    }

    suspend fun setAdBlockEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_ADBLOCK_ENABLED] = enabled }
    }

    suspend fun setDesktopMode(enabled: Boolean) {
        context.dataStore.edit { it[KEY_DESKTOP_MODE] = enabled }
    }

    suspend fun setServoExperimental(enabled: Boolean) {
        context.dataStore.edit { it[KEY_SERVO_EXPERIMENTAL] = enabled }
    }

    suspend fun setServoWebGpu(enabled: Boolean) {
        context.dataStore.edit { it[KEY_SERVO_WEBGPU] = enabled }
    }

    suspend fun setForceDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[KEY_FORCE_DARK_MODE] = enabled }
    }
}
