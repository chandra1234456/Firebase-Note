package com.example.practice.notesfirebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class LocationFragment : Fragment() {
    private lateinit var addressTextView: TextView
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val address = intent?.getStringExtra("address")
            val latitude = intent?.getDoubleExtra("latitude", 0.0)
            val longitude = intent?.getDoubleExtra("longitude", 0.0)

            addressTextView.text = address
            latitudeTextView.text = latitude.toString()
            longitudeTextView.text = longitude.toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       /* val view = inflater.inflate(R.layout.fragment_location, container, false)
        addressTextView = view.findViewById(R.id.addressTextView)
        latitudeTextView = view.findViewById(R.id.latitudeTextView)
        longitudeTextView = view.findViewById(R.id.longitudeTextView)*/
        return view
    }

    override fun onStart() {
        super.onStart()
        // Register the receiver
        requireActivity().registerReceiver(locationReceiver, IntentFilter("LOCATION_UPDATE"))
    }

    override fun onStop() {
        super.onStop()
        // Unregister the receiver
        requireActivity().unregisterReceiver(locationReceiver)
    }
}
