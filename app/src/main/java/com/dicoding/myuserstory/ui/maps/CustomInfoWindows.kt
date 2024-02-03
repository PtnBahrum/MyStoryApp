package com.dicoding.myuserstory.ui.maps

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.myuserstory.R
import com.dicoding.myuserstory.model.Story
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindows(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val view = (context as AppCompatActivity)
            .layoutInflater
            .inflate(R.layout.item_info, null)


        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvDescription = view.findViewById<TextView>(R.id.tv_description)

        with(marker.tag as Story) {
            tvName.text = name
            tvDescription.text = description
        }

        return view
    }
}