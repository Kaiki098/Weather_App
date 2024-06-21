package br.com.kaiki.weatherapp.network.repositories

import android.util.Log
import br.com.kaiki.weatherapp.R
import br.com.kaiki.weatherapp.network.models.WeatherResponse
import br.com.kaiki.weatherapp.network.services.WeatherService
import br.com.kaiki.weatherapp.ui.viewmodels.HomeScreenUiState

class WeatherRepository (
    private val service: WeatherService
) {
    private val weatherCodeToImageMap = mapOf(
        null to R.drawable.error_404_picture_not_found,
        1 to R.drawable.sunny,
        2 to R.drawable.sunny,
        3 to R.drawable.partlycloudy,
        45 to R.drawable.partlycloudy,
        48 to R.drawable.partlycloudy,
        51 to R.drawable.rainy,
        53 to R.drawable.rainy,
        55 to R.drawable.rainy,
        56 to R.drawable.rainy,
        57 to R.drawable.rainy,
        61 to R.drawable.rainthunder,
        63 to R.drawable.rainthunder,
        65 to R.drawable.rainthunder,
        66 to R.drawable.rainthunder,
        67 to R.drawable.rainthunder,
        71 to R.drawable.snowy,
        73 to R.drawable.snowy,
        75 to R.drawable.snowy,
        77 to R.drawable.snowy,
        80 to R.drawable.rainthunder,
        81 to R.drawable.rainthunder,
        82 to R.drawable.rainthunder,
        85 to R.drawable.snowy,
        86 to R.drawable.snowy,
        95 to R.drawable.rainthunder,
        96 to R.drawable.rainthunder,
        99 to R.drawable.rainthunder,
    )

    suspend fun getLocationWeather(latitude: String, longitude: String): HomeScreenUiState {
        try {
            val weather = service.getCurrentWeather(latitude = latitude, longitude = longitude)
            return weather.toHomeScreenUiState()
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Failed to getLocationWeather", e)
            return HomeScreenUiState()
        }
    }

    private fun WeatherResponse.toHomeScreenUiState() = HomeScreenUiState(
        temperature = current.temperature.toString(),
        humidity = current.humidity.toString(),
        apparentTemperature = current.apparentTemperature.toString(),
        windSpeed = current.windSpeed.toString(),
        weatherImage = weatherCodeToImageMap[current.weather],
        isDay = current.isDay != 0
    )// acho que deveria ser no view model...
}