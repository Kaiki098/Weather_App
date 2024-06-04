package br.com.kaiki.weatherapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.kaiki.weatherapp.ui.theme.GradientBlueWhite
import br.com.kaiki.weatherapp.ui.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val viewModel: HomeScreenViewModel = viewModel()
    viewModel.loadLocationWeather("-21.018302879006484", "-46.74536054821403")
    val uiState = viewModel.uiState.collectAsState()

    Scaffold (
        modifier = modifier
    ) { innerpadding ->
        Column (
            modifier = Modifier
                .padding(innerpadding)
                .background(Brush.linearGradient(GradientBlueWhite))
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.value.isLoading) {
                Text(text = "Is loading")
            }
            Card {
                Text(text = "Current Temperature: ${uiState.value.temperature}")
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}