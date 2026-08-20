package com.example.data.repository

import com.example.data.api.GeocodingResult
import com.example.data.api.OpenMeteoService
import com.example.data.api.WeatherResponse
import com.example.data.db.LocationDao
import com.example.data.db.SavedLocationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class WeatherRepository(private val locationDao: LocationDao) {

    private val apiService: OpenMeteoService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(OpenMeteoService::class.java)
    }

    val savedLocations: Flow<List<SavedLocationEntity>> = locationDao.getAllSavedLocations()

    suspend fun searchCity(query: String): List<GeocodingResult> = withContext(Dispatchers.IO) {
        if (query.trim().length < 2) return@withContext emptyList()
        try {
            val response = apiService.searchCity(query = query.trim())
            response.results ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getWeatherForecast(lat: Double, lon: Double): Result<WeatherResponse> = withContext(Dispatchers.IO) {
        try {
            val result = apiService.getWeatherForecast(latitude = lat, longitude = lon)
            Result.success(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun saveLocation(location: SavedLocationEntity) = withContext(Dispatchers.IO) {
        locationDao.insertLocation(location)
    }

    suspend fun deleteLocation(id: Long) = withContext(Dispatchers.IO) {
        locationDao.deleteLocationById(id)
    }

    suspend fun setDefaultLocation(id: Long) = withContext(Dispatchers.IO) {
        locationDao.setDefaultLocation(id)
    }

    suspend fun getDefaultLocation(): SavedLocationEntity? = withContext(Dispatchers.IO) {
        locationDao.getDefaultLocation()
    }
}
