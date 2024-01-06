package com.example.homework_18_8

import android.media.MediaSession2Service.MediaNotification
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaStorageDao {
    @Query("SELECT * FROM photo")
    fun getAllMedia() : Flow<List<DataPhoto>>
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insert(dataPhoto: DataPhoto)
}