package com.mctech.showcase.feature.flicker_data.tag_history.dao

import androidx.room.*
import com.mctech.showcase.feature.flicker_data.tag_history.entity.RoomTagHistoryEntity

@Dao
interface RoomTagHistoryDao {
    @Transaction
    @Query("SELECT * FROM flicker_tag_history ORDER BY date DESC")
    suspend fun loadAll(): List<RoomTagHistoryEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTag(tag: RoomTagHistoryEntity)
}