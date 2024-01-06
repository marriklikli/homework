package com.example.homework_18_8

import androidx.lifecycle.ViewModel

class MainActivityViewModel(): ViewModel() {
    private val dao: MediaStorageDao = App.db.mediaStorageDao()
    suspend fun addToMediaStorage(dataPhoto: DataPhoto){
        dao.insert(dataPhoto)
    }
}