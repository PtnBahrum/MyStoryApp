package com.dicoding.myuserstory.ui.maps

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.dicoding.myuserstory.R
import com.dicoding.myuserstory.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isCompassEnabled = true
            isIndoorLevelPickerEnabled = true
            isZoomControlsEnabled = true
            isMapToolbarEnabled = true
        }

        val indonesia = LatLng(-5.00000000, 120.00000000)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 4f))

        setMapStyle()

        getStories()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("map", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("map", "Can't find style. Error: ", exception)
        }
    }

    private fun getStories() {
        viewModel.getStories(this)
        viewModel.stories.observe(this) {
            for(i in it.indices) {
                val location = LatLng(it[i].lat.toDouble(), it[i].lon.toDouble())

                val window = CustomInfoWindows(this)
                mMap.setInfoWindowAdapter(window)

                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(it[i].name)
                )

                marker?.tag = it[i]
                marker?.showInfoWindow()

            }
        }
    }
}