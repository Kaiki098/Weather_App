package br.com.kaiki.weatherapp.network.services

import br.com.kaiki.weatherapp.network.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast?current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weather_code,wind_speed_10m")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): WeatherResponse
}