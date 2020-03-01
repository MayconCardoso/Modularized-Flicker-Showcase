package com.mctech.showcase.feature.flicker_data.tag_history.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "flicker_tag_history"
)
data class RoomTagHistoryEntity(
    @PrimaryKey
    val tag: String,
    val date: Long
)