package br.com.kaiki.weatherapp.di

import br.com.kaiki.weatherapp.network.repositories.WeatherRepository
import br.com.kaiki.weatherapp.network.services.WeatherService
import br.com.kaiki.weatherapp.ui.viewmodels.HomeScreenViewModel
import br.com.kaiki.weatherapp.utils.LocationUtils
import br.com.kaiki.weatherapp.utils.StringUtils
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    //Network
    single { provideWeatherService() }
    singleOf(::WeatherRepository)

    //Utils
    singleOf(::LocationUtils)
    singleOf(::StringUtils)

    //View models
    viewModelOf(::HomeScreenViewModel)
}

private fun provideWeatherService(): WeatherService {
    return Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(WeatherService::class.java)
}
