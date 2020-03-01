package com.mctech.showcase.flicker.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mctech.showcase.feature.flicker_data.photo.local.dao.RoomFlickerPhotoDao
import com.mctech.showcase.feature.flicker_data.photo.local.entity.RoomFlickerPhotoEntity
import com.mctech.showcase.feature.flicker_data.tag_history.dao.RoomTagHistoryDao
import com.mctech.showcase.feature.flicker_data.tag_history.entity.RoomTagHistoryEntity

@Database(
    version = 1,
    entities = [
        RoomFlickerPhotoEntity::class,
        RoomTagHistoryEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flickerPhotoDao(): RoomFlickerPhotoDao
    abstract fun flickerTagHistoryDao(): RoomTagHistoryDao
}