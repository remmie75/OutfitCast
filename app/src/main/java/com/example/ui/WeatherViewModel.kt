package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeocodingResult
import com.example.data.api.WeatherResponse
import com.example.data.db.AppDatabase
import com.example.data.db.SavedLocationEntity
import com.example.data.domain.AdvicePersona
import com.example.data.domain.AppLanguage
import com.example.data.domain.ClothingEngine
import com.example.data.domain.ClothingRecommendation
import com.example.data.preferences.UserPreferencesManager
import com.example.data.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface WeatherUiState {
    object Loading : WeatherUiState
    data class Success(
        val cityName: String,
        val countryName: String,
        val latitude: Double,
        val longitude: Double,
        val weather: WeatherResponse,
        val clothingRecommendation: ClothingRecommendation,
        val isSaved: Boolean
    ) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = WeatherRepository(db.locationDao())
    val preferencesManager = UserPreferencesManager(application)

    val savedLocations: StateFlow<List<SavedLocationEntity>> = repository.savedLocations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    val selectedPersona: StateFlow<AdvicePersona> = preferencesManager.persona
    val isCelsius: StateFlow<Boolean> = preferencesManager.isCelsius
    val selectedLanguage: StateFlow<AppLanguage> = preferencesManager.language
    val selectedIconOption: StateFlow<Int> = preferencesManager.selectedIconOption

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<GeocodingResult>>(emptyList())
    val searchResults: StateFlow<List<GeocodingResult>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            // Check saved last location or saved default location
            val lastLoc = preferencesManager.getLastLocation()
            val defaultLoc = repository.getDefaultLocation()

            when {
                lastLoc != null -> {
                    loadWeather(lastLoc.lat, lastLoc.lon, lastLoc.city, lastLoc.country)
                }
                defaultLoc != null -> {
                    loadWeather(defaultLoc.latitude, defaultLoc.longitude, defaultLoc.name, defaultLoc.country)
                }
                else -> {
                    // Default to Amsterdam for great outfit weather
                    loadWeather(52.3676, 4.9041, "Amsterdam", "Netherlands")
                }
            }
        }
    }

    fun loadWeather(lat: Double, lon: Double, cityName: String, countryName: String = "") {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            preferencesManager.saveLastLocation(lat, lon, cityName, countryName)

            val result = repository.getWeatherForecast(lat, lon)
            result.onSuccess { weatherResponse ->
                val current = weatherResponse.current ?: com.example.data.api.CurrentWeather()
                val recommendation = ClothingEngine.generateRecommendation(
                    weather = current,
                    persona = selectedPersona.value,
                    language = selectedLanguage.value,
                    isCelsius = isCelsius.value
                )
                val isSaved = savedLocations.value.any { it.latitude == lat && it.longitude == lon }
                _uiState.value = WeatherUiState.Success(
                    cityName = cityName,
                    countryName = countryName,
                    latitude = lat,
                    longitude = lon,
                    weather = weatherResponse,
                    clothingRecommendation = recommendation,
                    isSaved = isSaved
                )
            }.onFailure { error ->
                _uiState.value = WeatherUiState.Error(
                    error.localizedMessage ?: "Unable to fetch weather. Please check your internet connection."
                )
            }
        }
    }

    fun setPersona(persona: AdvicePersona) {
        preferencesManager.setPersona(persona)
        recalculateRecommendation()
    }

    fun setLanguage(language: AppLanguage) {
        preferencesManager.setLanguage(language)
        recalculateRecommendation()
    }

    fun setIconOption(option: Int) {
        preferencesManager.setSelectedIconOption(option)
    }

    fun toggleTemperatureUnit() {
        preferencesManager.setCelsius(!isCelsius.value)
        recalculateRecommendation()
    }

    fun setTemperatureUnit(celsius: Boolean) {
        preferencesManager.setCelsius(celsius)
        recalculateRecommendation()
    }

    private fun recalculateRecommendation() {
        val currentState = _uiState.value
        if (currentState is WeatherUiState.Success) {
            val current = currentState.weather.current ?: com.example.data.api.CurrentWeather()
            val recommendation = ClothingEngine.generateRecommendation(
                weather = current,
                persona = selectedPersona.value,
                language = selectedLanguage.value,
                isCelsius = isCelsius.value
            )
            _uiState.value = currentState.copy(
                clothingRecommendation = recommendation
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        if (query.trim().length < 2) {
            _searchResults.value = emptyList()
            _isSearching.value = false
            return
        }
        searchJob = viewModelScope.launch {
            _isSearching.value = true
            delay(350) // debounce typing
            val results = repository.searchCity(query)
            _searchResults.value = results
            _isSearching.value = false
        }
    }

    fun selectSearchResult(city: GeocodingResult) {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
        _isSearching.value = false
        val country = city.country ?: city.admin1 ?: ""
        loadWeather(city.latitude, city.longitude, city.name, country)
    }

    fun toggleSaveCurrentLocation() {
        val currentState = _uiState.value
        if (currentState is WeatherUiState.Success) {
            viewModelScope.launch {
                val existing = savedLocations.value.find {
                    it.latitude == currentState.latitude && it.longitude == currentState.longitude
                }
                if (existing != null) {
                    repository.deleteLocation(existing.id)
                } else {
                    val id = System.currentTimeMillis()
                    repository.saveLocation(
                        SavedLocationEntity(
                            id = id,
                            name = currentState.cityName,
                            country = currentState.countryName,
                            latitude = currentState.latitude,
                            longitude = currentState.longitude
                        )
                    )
                }
                _uiState.value = currentState.copy(isSaved = !currentState.isSaved)
            }
        }
    }

    fun removeSavedLocation(id: Long) {
        viewModelScope.launch {
            repository.deleteLocation(id)
        }
    }
}
