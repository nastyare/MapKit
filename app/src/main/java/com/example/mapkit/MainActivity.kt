package com.example.mapkit

import android.os.Bundle
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var locBtn: FloatingActionButton
    private lateinit var toggleBtn: ToggleButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("0124477f-8ee9-4fc5-901f-fea07a74717e")
        setContentView(R.layout.activity_main)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)
        mapView = findViewById(R.id.mapview)

        locBtn = findViewById(R.id.location)
        locBtn.setOnClickListener() {
            val (latitude, longitude) = glavPochtamt()
            mapView.map.move(
                CameraPosition(Point(latitude, longitude), 18.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 2F),
                null
            )
        }
        var mapKit: MapKit = MapKitFactory.getInstance()
        var probki = mapKit.createTrafficLayer(mapView.mapWindow)
        var probkiIsOn = false
        toggleBtn = findViewById(R.id.toggleBtn)
        toggleBtn.setOnClickListener {
            when(probkiIsOn) {
                false -> {
                    probkiIsOn = true
                    probki.isTrafficVisible = true
                }
                true -> {
                    probkiIsOn = false
                    probki.isTrafficVisible = false
                }
            }
        }
    }

    fun glavPochtamt(): Pair<Double, Double> {
        return Pair(55.35485859365324, 86.0863204841308)
    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}