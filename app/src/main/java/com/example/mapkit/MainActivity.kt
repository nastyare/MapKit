package com.example.mapkit

import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var toggleBtn: ToggleButton
    private lateinit var myLocation: FloatingActionButton
    private lateinit var mapCollection: MapObjectCollection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("0124477f-8ee9-4fc5-901f-fea07a74717e")
        setContentView(R.layout.activity_main)
        MapKitFactory.initialize(this)
        mapView = findViewById(R.id.mapview)


        var mapKit: MapKit = MapKitFactory.getInstance()

        getLocationPermision()

        //для пробок
        var traffic = mapKit.createTrafficLayer(mapView.mapWindow)
        var trafficIsOn = false
        toggleBtn = findViewById(R.id.toggleBtn)
        toggleBtn.setOnClickListener {
            when(trafficIsOn) {
                false -> {
                    trafficIsOn = true
                    traffic.isTrafficVisible = true
                }
                true -> {
                    trafficIsOn = false
                    traffic.isTrafficVisible = false
                }
            }
        }

        //показ локации текущей
        myLocation = findViewById(R.id.location)
        myLocation.setOnClickListener() {
            var location = mapKit.createUserLocationLayer(mapView.mapWindow)
            location.isVisible = true
            mapView.map.move(
                CameraPosition(Point(55.351735295422394,86.09376724603264), 16f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 2F),
                null
            )
        }

        mapCollection = mapView.map.mapObjects.addCollection()
        mapView.map.addInputListener(inputListener)

    }

    private val inputListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            mark(p1)
        }

        override fun onMapLongTap(p0: Map, p1: Point) {
        }
    }
    //обработка нажатия
    fun mark(point: Point) {
        mapCollection.addPlacemark(point)
    }

    private fun getLocationPermision() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,), 0)
            return
        }
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