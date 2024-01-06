package com.example.homework_18_8

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.preference.PreferenceManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.Priority
import com.example.homework_18_8.databinding.ActivityMainBinding
import com.example.homework_18_8.databinding.ContentMapBinding
import com.google.android.gms.common.internal.LibraryVersion.getInstance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView



class MapActivity : AppCompatActivity() {
    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.isNotEmpty() && map.values.all { it }) {

        }}
        private lateinit var map: MapView
        private lateinit var binding: ContentMapBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            MapKitFactory.setApiKey("54d6a1cc-ca20-42a1-b0b1-d79e695e6b0a")
            binding = ContentMapBinding.inflate(layoutInflater)
            setContentView(binding.root)
            map = binding.mapview
            map.map.move(
                CameraPosition(Point(57.928923, 33.255336), 11.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0F),
                null
            )
            binding.back.setOnClickListener {
               // val intent = Intent(this, MainActivity::class.java)
               // startActivity(intent)
                throw Exception ("Специальная ошибка")
            }
            binding.plus.setOnClickListener {
                val currentPosition = map.mapWindow.map.cameraPosition
                if (currentPosition.zoom < map.map.maxZoom) {
                    map.map.move(
                        CameraPosition(
                            currentPosition.target, currentPosition.zoom + 1.0f,
                            currentPosition.azimuth, currentPosition.tilt
                        ),
                        Animation(Animation.Type.SMOOTH, 0F),
                        null
                    )
                }
            }
            binding.minus.setOnClickListener {
                val currentPosition = map.mapWindow.map.cameraPosition
                if (currentPosition.zoom > map.map.minZoom) {
                    map.map.move(
                        CameraPosition(
                            currentPosition.target, currentPosition.zoom - 1.0f,
                            currentPosition.azimuth, currentPosition.tilt
                        ),
                        Animation(Animation.Type.SMOOTH, 0F),
                        null
                    )
                }
            }
        }
            override fun onStart() {
                super.onStart()
                checkPermission2()
                map.onStart()
                MapKitFactory.getInstance().onStart()
            }

            override fun onStop() {
                map.onStop()
                MapKitFactory.getInstance().onStop()
                super.onStop()

            }

            private fun checkPermission2() {
                val isAllGranted = MapActivity.REQUEST_PERMISSIONS_MAP.all { permission ->
                    ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED

                }
                if (isAllGranted) {
                } else launcher.launch(MapActivity.REQUEST_PERMISSIONS_MAP)
            }


            companion object {
                private val REQUEST_PERMISSIONS_MAP: Array<String> = arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            }
        }
