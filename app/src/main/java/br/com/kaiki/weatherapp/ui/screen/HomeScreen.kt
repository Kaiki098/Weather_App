package br.com.kaiki.weatherapp.ui.screen

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.kaiki.weatherapp.MainActivity
import br.com.kaiki.weatherapp.ui.theme.GradientBlueWhite
import br.com.kaiki.weatherapp.ui.viewmodels.HomeScreenViewModel
import br.com.kaiki.weatherapp.utils.LocationUtils

@Composable
fun HomeScreen(
    locationUtils: LocationUtils,
    context: Context,
    modifier: Modifier = Modifier
) {
    val viewModel: HomeScreenViewModel = viewModel()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {

                locationUtils.requestLocation(viewModel)

            } else {
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (rationaleRequired) {
                    Toast.makeText(
                        context,
                        "Location Permission is required for this feature to work",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Location Permission is required. Please enable it in the Android Settings ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    )
    val location = viewModel.location.collectAsState(null)
    viewModel.loadLocationWeather(location.value?.latitude.toString(), location.value?.latitude.toString())
    val uiState = viewModel.uiState.collectAsState()
    val address = location.value?.let {
        locationUtils.reverseGeocodeLocation(it)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(GradientBlueWhite))
    ) {
        Row (
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = "$address")
            Button(onClick = {
                if (locationUtils.hasLocationPermission(context)) {
                    // Permission already granted update the location
                    locationUtils.requestLocation(viewModel)
                } else {
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }) {
                Text(text = "Get Location")
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.value.isLoading) {
                Text(text = "Is loading")
            }
            Text(text = "Current Temperature: ${uiState.value.temperature}")
        }
    }
}


@Preview
@Composable
private fun HomeScreenPreview() {
    //HomeScreen(context = Context,)
}