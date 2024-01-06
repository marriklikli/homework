package com.example.homework_18_8

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
   private val dao: MediaStorageDao = App.db.mediaStorageDao()

   val allFiles = dao.getAllMedia()
       .stateIn(
           scope = viewModelScope,
           started = SharingStarted.WhileSubscribed(5000),
           initialValue = emptyList()
       )
fun getListOfFiles(): List<DataPhoto> {
    return allFiles.value.map { it }
}

}