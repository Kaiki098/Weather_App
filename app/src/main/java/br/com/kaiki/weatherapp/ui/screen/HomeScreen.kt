package br.com.kaiki.weatherapp.ui.screen

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.kaiki.weatherapp.R
import br.com.kaiki.weatherapp.ui.theme.GradientBlueWhite
import br.com.kaiki.weatherapp.ui.theme.WeatherAppTheme
import br.com.kaiki.weatherapp.ui.viewmodels.HomeScreenUiState
import br.com.kaiki.weatherapp.ui.viewmodels.HomeScreenViewModel
import br.com.kaiki.weatherapp.utils.LocationUtils
import br.com.kaiki.weatherapp.utils.StringUtils
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = koinViewModel(),
    locationUtils: LocationUtils = koinInject(),
    stringUtils: StringUtils = koinInject(),
    context: Context = koinInject()
) {
    if (locationUtils.hasLocationPermission(context)) {
        // Permission already granted update the location
        locationUtils.requestLocation(viewModel)
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {
                locationUtils.requestLocation(viewModel)
            } else {
                locationUtils.showLocationPermissionRequiredMessage(context)
            }
        }
    )

    val location by viewModel.location.collectAsState(null)
    viewModel.loadLocationWeather(
        location?.latitude.toString(),
        location?.latitude.toString()
    )
    val textAddress = location?.let {
        locationUtils.reverseGeocodeLocation(it)
    } ?: ""
    val uiState by viewModel.uiState.collectAsState()
    val currentDate = LocalDate.now()
    val dayOfWeek = remember {
        stringUtils.capitalize(
            currentDate.dayOfWeek.getDisplayName(
                TextStyle.FULL,
                Locale("pt", "BR")
            )
        )
    } // Cherando a POO
    val month = remember {
        stringUtils.capitalize(
            currentDate.month.getDisplayName(
                TextStyle.FULL,
                Locale("pt", "BR")
            )
        )// Cherando a POO
    }
    val onLocationClick = {
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
    } // talvez seja recomposto varias vezes

    HomeScreenContent(
        uiState = uiState,
        dayOfWeek = dayOfWeek,
        textAddress = textAddress,
        onLocationClick = onLocationClick,
        dayOfMonth = currentDate.dayOfMonth,
        month = month
    )
}

@Composable
fun HomeScreenContent(
    uiState: HomeScreenUiState,
    dayOfWeek: String,
    textAddress: String,
    onLocationClick: () -> Unit,
    dayOfMonth: Int,
    month: String
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(GradientBlueWhite))
    ) {
        Column(
            Modifier.background(Brush.linearGradient(GradientBlueWhite))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AnimatedVisibility(
                    visible = textAddress != "",
                    enter = slideIn { fullSize -> IntOffset(fullSize.width, 0) }
                ) {
                    Text(
                        text = textAddress,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                IconButton(
                    onClick = { onLocationClick() }) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Ícone de localização"
                    )
                }

            }

            Column(
                Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.padding(40.dp),
                    painter = painterResource(uiState.weatherImage ?: R.drawable.sunny),
                    contentDescription = "Ícone de dia ensolarado"
                )
                Text(
                    text = "$dayOfWeek | $dayOfMonth de $month"
                )
                AnimatedVisibility(visible = uiState.temperature != null, enter = fadeIn()) {
                    Text(
                        text = uiState.temperature?.let { "$it°C" } ?: "",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 64.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    WeatherAppTheme {
        HomeScreenContent(
            uiState = HomeScreenUiState(temperature = "25"),
            dayOfWeek = "Segunda-feira",
            textAddress = "São Paulo, SP",
            onLocationClick = {},
            dayOfMonth = 1,
            month = "Janeiro"
        )
    }
}
