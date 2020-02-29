package com.mctech.showcase.feature.flicker_data.remote.api

import com.mctech.showcase.feature.flicker_data.remote.entity.PhotoSizeResponse
import com.mctech.showcase.feature.flicker_data.remote.entity.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
interface FlickerPhotoApi{

    @GET("?method=flickr.photos.search")
    suspend fun getPaginatedPhotos(
        @Query("tags") tag: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 10
    ): SearchResponse

    @GET("?method=flickr.photos.getSizes")
    suspend fun getPhotoSizes(
        @Query("photo_id") photoId: Long
    ): PhotoSizeResponse
}