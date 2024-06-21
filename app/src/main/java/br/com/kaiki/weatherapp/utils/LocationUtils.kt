package br.com.kaiki.weatherapp.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.kaiki.weatherapp.MainActivity
import br.com.kaiki.weatherapp.models.LocationData
import br.com.kaiki.weatherapp.ui.viewmodels.HomeScreenViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.util.Locale


class LocationUtils(val context: Context) {

    private val _fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    //verifica se tem as permissões
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }


    //pega a localização
    @SuppressLint("MissingPermission")
    fun requestLocation(viewModel: HomeScreenViewModel) {
        _fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    viewModel.updateLocation(LocationData(location.latitude, location.longitude))
                }
            }
    }

    //converte coordenadas para endereços
    fun reverseGeocodeLocation(location: LocationData): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinate = LatLng(location.latitude, location.longitude)
        val addresses: MutableList<Address>? =
            geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)

        return if (addresses?.isNotEmpty() == true) {
            "${addresses[0].subAdminArea} - ${addresses[0].adminArea ?: ""}, ${addresses[0].countryName}"
        } else {
            "Address not found"
        }
    }

    fun showLocationPermissionRequiredMessage(context: Context) {
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