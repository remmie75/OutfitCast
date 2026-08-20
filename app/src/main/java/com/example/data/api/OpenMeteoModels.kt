package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingResponse(
    @Json(name = "results") val results: List<GeocodingResult>? = null
)

@JsonClass(generateAdapter = true)
data class GeocodingResult(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "country") val country: String? = null,
    @Json(name = "admin1") val admin1: String? = null
)

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double,
    @Json(name = "current") val current: CurrentWeather? = null,
    @Json(name = "hourly") val hourly: HourlyWeather? = null,
    @Json(name = "daily") val daily: DailyWeather? = null
)

@JsonClass(generateAdapter = true)
data class CurrentWeather(
    @Json(name = "temperature_2m") val temperature: Double = 0.0,
    @Json(name = "relative_humidity_2m") val relativeHumidity: Int = 0,
    @Json(name = "apparent_temperature") val apparentTemperature: Double = 0.0,
    @Json(name = "is_day") val isDay: Int = 1,
    @Json(name = "precipitation") val precipitation: Double = 0.0,
    @Json(name = "rain") val rain: Double = 0.0,
    @Json(name = "showers") val showers: Double = 0.0,
    @Json(name = "snowfall") val snowfall: Double = 0.0,
    @Json(name = "weather_code") val weatherCode: Int = 0,
    @Json(name = "cloud_cover") val cloudCover: Int = 0,
    @Json(name = "wind_speed_10m") val windSpeed: Double = 0.0,
    @Json(name = "wind_direction_10m") val windDirection: Int = 0,
    @Json(name = "wind_gusts_10m") val windGusts: Double = 0.0
)

@JsonClass(generateAdapter = true)
data class HourlyWeather(
    @Json(name = "time") val time: List<String> = emptyList(),
    @Json(name = "temperature_2m") val temperature: List<Double> = emptyList(),
    @Json(name = "precipitation_probability") val precipitationProbability: List<Int>? = emptyList(),
    @Json(name = "weather_code") val weatherCode: List<Int> = emptyList(),
    @Json(name = "apparent_temperature") val apparentTemperature: List<Double> = emptyList()
)

@JsonClass(generateAdapter = true)
data class DailyWeather(
    @Json(name = "time") val time: List<String> = emptyList(),
    @Json(name = "weather_code") val weatherCode: List<Int> = emptyList(),
    @Json(name = "temperature_2m_max") val temperatureMax: List<Double> = emptyList(),
    @Json(name = "temperature_2m_min") val temperatureMin: List<Double> = emptyList(),
    @Json(name = "precipitation_sum") val precipitationSum: List<Double>? = emptyList(),
    @Json(name = "precipitation_probability_max") val precipitationProbabilityMax: List<Int>? = emptyList(),
    @Json(name = "wind_speed_10m_max") val windSpeedMax: List<Double>? = emptyList()
)
