package com.example.homework_18_8

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DataPhoto::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun mediaStorageDao(): MediaStorageDao
}