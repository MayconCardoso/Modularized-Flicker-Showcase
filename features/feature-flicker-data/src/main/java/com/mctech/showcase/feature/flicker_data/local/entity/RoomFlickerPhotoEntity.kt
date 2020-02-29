package com.mctech.showcase.feature.flicker_data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "flicker_photo",
    primaryKeys = ["tag", "page"],
    indices = [
        Index(
            value = ["tag", "page"],
            unique = true,
            name = "index_photos_per_tag_and_page"
        )
    ]
)
data class RoomFlickerPhotoEntity(
    val id: Long,
    val tag: String,
    val page: Int,
    val title: String,
    val sourceUrl: String
)