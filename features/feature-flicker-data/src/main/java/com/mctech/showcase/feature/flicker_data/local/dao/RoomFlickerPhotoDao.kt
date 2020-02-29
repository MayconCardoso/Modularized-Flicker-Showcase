package com.mctech.showcase.feature.flicker_data.local.dao

import androidx.room.*
import com.mctech.showcase.feature.flicker_data.local.entity.RoomFlickerPhotoEntity

@Dao
interface RoomFlickerPhotoDao {
    @Transaction
    @Query("SELECT * FROM flicker_photo WHERE tag = :tag AND page = :page")
    suspend fun loadPhotosByTagAndPage(tag: String, page: Int): List<RoomFlickerPhotoEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePhotos(photos: List<RoomFlickerPhotoEntity>)

    @Transaction
    @Query("DELETE FROM flicker_photo WHERE tag = :tag")
    suspend fun delete(tag: String)
}