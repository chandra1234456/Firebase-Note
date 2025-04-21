package com.example.practice.notesfirebase

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.*

class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var geocoder: Geocoder

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("LocationService", "Location: $latitude, $longitude")
                    val address = getAddress(latitude, longitude)
                    Log.d("LocationService", "Address: $address")
                    sendLocationUpdate(address, latitude, longitude)
                }
            }
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val address = addresses.get(0)
                val addressLines = (0..address.maxAddressLineIndex).joinToString(", ") { address.getAddressLine(it) }
                addressLines
            } else {
                "Address not found"
            }
        } catch (e: Exception) {
            Log.e("LocationService", "Geocoder not available: ${e.message}")
            "Address not available"
        }
    }

    private fun sendLocationUpdate(address: String, latitude: Double, longitude: Double) {
        val intent = Intent("LOCATION_UPDATE")
        intent.putExtra("address", address)
        intent.putExtra("latitude", latitude)
        intent.putExtra("longitude", longitude)
        sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                    this ,
                    Manifest.permission.ACCESS_FINE_LOCATION
                                              ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this ,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                                                                                                                          ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
