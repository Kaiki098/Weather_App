package br.com.kaiki.weatherapp.network.repositories

import android.util.Log
import br.com.kaiki.weatherapp.network.RetrofitInt
import br.com.kaiki.weatherapp.network.services.WeatherService
import br.com.kaiki.weatherapp.network.models.toHomeScreenUiState
import br.com.kaiki.weatherapp.ui.viewmodels.HomeScreenUiState

class WeatherRepository (
    private val service: WeatherService = RetrofitInt().weatherService
) {
    suspend fun getLocationWeather(latitude: String, longitude: String): HomeScreenUiState {
        try {
            val weather = service.getCurrentWeather(latitude = latitude, longitude = longitude)
            return weather.toHomeScreenUiState()
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Failed to getLocationWeather", e)
            return HomeScreenUiState()
        }
    }
}