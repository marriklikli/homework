package com.example.homework_18_8

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import androidx.room.RoomDatabase

class App: Application()  {

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "db"
        ).build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val name = "Первое уведомление"
        val descriptionText = "Первый текст уведомления"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel (NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "тестовое id"
        lateinit var db: AppDataBase
    }
}