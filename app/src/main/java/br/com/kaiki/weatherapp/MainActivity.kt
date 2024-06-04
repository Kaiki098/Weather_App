package br.com.kaiki.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import br.com.kaiki.weatherapp.ui.screen.HomeScreen
import br.com.kaiki.weatherapp.ui.theme.WeatherAppTheme
import br.com.kaiki.weatherapp.utils.LocationUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    HomeScreen(context = context, locationUtils = locationUtils)
}