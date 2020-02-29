package com.mctech.showcase.feature.flicker_data.remote.entity

import com.google.gson.annotations.SerializedName

data class PhotoSizeResponse(
    @SerializedName("sizes")
    val response: SizesResponse
)

data class PhotoResponse(
    @SerializedName("label")
    val size: String,
    val media: String,
    val source: String
)

data class SizesResponse(
    @SerializedName("size")
    val photos: List<PhotoResponse>
)