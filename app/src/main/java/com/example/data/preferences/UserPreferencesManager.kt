package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.data.domain.AdvicePersona
import com.example.data.domain.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserPreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _language = MutableStateFlow(loadLanguage())
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isCelsius = MutableStateFlow(prefs.getBoolean(KEY_IS_CELSIUS, true))
    val isCelsius: StateFlow<Boolean> = _isCelsius.asStateFlow()

    private val _persona = MutableStateFlow(loadPersona())
    val persona: StateFlow<AdvicePersona> = _persona.asStateFlow()

    private val _selectedIconOption = MutableStateFlow(prefs.getInt(KEY_ICON_OPTION, 1))
    val selectedIconOption: StateFlow<Int> = _selectedIconOption.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
        prefs.edit().putString(KEY_LANGUAGE, lang.name).apply()
    }

    fun setCelsius(celsius: Boolean) {
        _isCelsius.value = celsius
        prefs.edit().putBoolean(KEY_IS_CELSIUS, celsius).apply()
    }

    fun setPersona(persona: AdvicePersona) {
        _persona.value = persona
        prefs.edit().putString(KEY_PERSONA, persona.name).apply()
    }

    fun setSelectedIconOption(option: Int) {
        _selectedIconOption.value = option
        prefs.edit().putInt(KEY_ICON_OPTION, option).apply()
    }

    fun saveLastLocation(lat: Double, lon: Double, city: String, country: String) {
        prefs.edit()
            .putString(KEY_LAST_CITY, city)
            .putString(KEY_LAST_COUNTRY, country)
            .putFloat(KEY_LAST_LAT, lat.toFloat())
            .putFloat(KEY_LAST_LON, lon.toFloat())
            .apply()
    }

    fun getLastLocation(): LocationPref? {
        val city = prefs.getString(KEY_LAST_CITY, null) ?: return null
        val country = prefs.getString(KEY_LAST_COUNTRY, "") ?: ""
        val lat = prefs.getFloat(KEY_LAST_LAT, 0f).toDouble()
        val lon = prefs.getFloat(KEY_LAST_LON, 0f).toDouble()
        return LocationPref(city, country, lat, lon)
    }

    private fun loadLanguage(): AppLanguage {
        val name = prefs.getString(KEY_LANGUAGE, AppLanguage.EN.name) ?: AppLanguage.EN.name
        return try {
            AppLanguage.valueOf(name)
        } catch (e: Exception) {
            AppLanguage.EN
        }
    }

    private fun loadPersona(): AdvicePersona {
        val name = prefs.getString(KEY_PERSONA, AdvicePersona.SASSY.name) ?: AdvicePersona.SASSY.name
        return try {
            AdvicePersona.valueOf(name)
        } catch (e: Exception) {
            AdvicePersona.SASSY
        }
    }

    data class LocationPref(
        val city: String,
        val country: String,
        val lat: Double,
        val lon: Double
    )

    companion object {
        private const val PREFS_NAME = "outfitcast_preferences"
        private const val KEY_LANGUAGE = "key_app_language"
        private const val KEY_IS_CELSIUS = "key_is_celsius"
        private const val KEY_PERSONA = "key_persona"
        private const val KEY_ICON_OPTION = "key_icon_option"
        private const val KEY_LAST_CITY = "key_last_city"
        private const val KEY_LAST_COUNTRY = "key_last_country"
        private const val KEY_LAST_LAT = "key_last_lat"
        private const val KEY_LAST_LON = "key_last_lon"
    }
}
