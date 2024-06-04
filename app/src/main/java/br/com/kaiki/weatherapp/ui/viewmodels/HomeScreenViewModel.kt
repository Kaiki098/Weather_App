package br.com.kaiki.weatherapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.kaiki.weatherapp.models.LocationData
import br.com.kaiki.weatherapp.network.repositories.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeScreenUiState(
    val temperature: String = "Not found",
    val humidity: String = "Not found",
    val apparentTemperature: String = "Not found",
    val windSpeed: String = "Not found",
    val weather: String = "Not found",
    val isDay: Boolean = true,
    var isLoading: Boolean = true,
    var errorDescription: String? = null
)

class HomeScreenViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _location = MutableStateFlow<LocationData?>(null)
    val location: StateFlow<LocationData?> = _location

    fun updateLocation(newLocation: LocationData) {
        _location.value = newLocation
    }

    fun loadLocationWeather(latitude: String, longitude: String) {
        viewModelScope.launch {
            try {
                val newUiState = repository.getLocationWeather(latitude, longitude)
                _uiState.value = newUiState.copy(isLoading = false)
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Error loadLocationWeather", e)
                _uiState.value.errorDescription = "Erro ao carregar clima pela localização"
            }
        }
    }

}