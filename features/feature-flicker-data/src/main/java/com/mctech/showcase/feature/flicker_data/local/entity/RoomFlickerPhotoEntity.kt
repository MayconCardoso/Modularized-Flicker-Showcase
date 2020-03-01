package com.mctech.showcase.feature.flicker_data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "flicker_photo",
    indices = [
        Index(
            value = ["tag", "page"],
            name = "index_photos_per_tag_and_page"
        )
    ]
)
data class RoomFlickerPhotoEntity(
    @PrimaryKey
    val id: Long,
    val tag: String,
    val sort : Int,
    val page: Int,
    val title: String,
    val sourceUrl: String,
    val thumbnailUrl : String
)