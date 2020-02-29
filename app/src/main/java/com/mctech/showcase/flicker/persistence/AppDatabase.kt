package com.mctech.showcase.flicker.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mctech.showcase.feature.flicker_data.local.dao.RoomFlickerPhotoDao
import com.mctech.showcase.feature.flicker_data.local.entity.RoomFlickerPhotoEntity

@Database(
    version = 1,
    entities = [
        RoomFlickerPhotoEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flickerPhotoDao(): RoomFlickerPhotoDao
}