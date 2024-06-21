package br.com.kaiki.weatherapp.network.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather (
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("relative_humidity_2m")
    val humidity: Int,
    @SerializedName("apparent_temperature")
    val apparentTemperature: Double,
    @SerializedName("wind_speed_10m")
    val windSpeed: Double,
    @SerializedName("weather_code")
    val weather: Int,
    @SerializedName("is_day")
    val isDay: Int
)



