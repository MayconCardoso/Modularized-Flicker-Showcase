package com.mctech.showcase.feature.flicker_data.remote.entity

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val photos: SearchPaginationResponse
)

data class SearchPaginationResponse(
    val page: Int,
    val pages: Int,
    val total: Int,
    val photo: List<SearchPhotoResponse>
)

data class SearchPhotoResponse(
    val id: Long,
    val title: String,
    @SerializedName("url_q")
    val thumbnailUtl: String?,
    @SerializedName("url_l")
    val originalPhotoUrl: String?
)