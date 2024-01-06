package com.example.homework_18_8

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.media.ImageReader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.example.homework_18_8.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import java.util.*
import java.util.concurrent.Executor
private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"
class MainActivity : AppCompatActivity() {

    private lateinit var fusedClient: FusedLocationProviderClient
    private val viewModel: MainActivityViewModel by viewModels()
    private  var imageCapture:ImageCapture? = null
    private lateinit var  executor:Executor
    private lateinit var binding: ActivityMainBinding

    private val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

    private val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {map->
      if (map.values.all { it }) {
          startCamera()
      } else {
          Toast.makeText(this, "Разрешение не получено", Toast.LENGTH_SHORT).show()
      }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        executor = ContextCompat.getMainExecutor(this)
        checkPermission()
        binding.takePhoto.setOnClickListener {
            takePhoto()
            createNotification()
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            Log.d("пробное сообщение", it.result)
        }
       binding.image.setOnClickListener {
           val intent = Intent(this, MediaFileActivity::class.java)
           startActivity(intent)

       }
        binding.mapButton.setOnClickListener {
            val map = Intent(this, MapActivity::class.java)
            startActivity(map)
        }
    }
    private fun takePhoto() {
val imageCapture = imageCapture ?: return
      val contentValues =  ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
      val outPut = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
        imageCapture.takePicture(
            outPut,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    lifecycleScope.launch {
                        viewModel.addToMediaStorage(DataPhoto(outputFileResults.savedUri.toString(), name))
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@MainActivity, "Ошибка при сохранении: ${exception.message}", Toast.LENGTH_SHORT).show()
               exception.printStackTrace()
                }
            }
        )
    }
    private fun startCamera() {
val camera = ProcessCameraProvider.getInstance(this)
        camera.addListener({
            val cameraProvider = camera.get()
           val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.view.surfaceProvider)
            imageCapture =  ImageCapture.Builder().build()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        }, executor)
    }
    private fun checkPermission() {
       val isAllGranted = REQUEST_PERMISSIONS.all {permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

        }
        if (isAllGranted) {
            startCamera()
        }
        else launcher.launch(REQUEST_PERMISSIONS)
    }
    fun createNotification() {
        val map = Intent(this, MapActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.getActivity(this, 0, map, PendingIntent.FLAG_IMMUTABLE)
        else
            PendingIntent.getActivity(this, 0, map, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_navigation_24)
            .setContentTitle("Мое уведомление")
            .setContentText("Навигация")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        with(NotificationManagerCompat.from(this)) {
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
    companion object{
        private val REQUEST_PERMISSIONS: Array<String> = arrayOf(
                android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        private const val NOTIFICATION_ID = 1000
    }



}
