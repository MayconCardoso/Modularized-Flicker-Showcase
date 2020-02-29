package com.mctech.showcase.feature.flicker_data.remote

import com.mctech.showcase.feature.flicker_data.remote.api.FlickerPhotoApi
import com.mctech.showcase.feature.flicker_data.remote.entity.SearchPhotoResponse
import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
class FlickerRemoteDataSourceImpl(
    private val api: FlickerPhotoApi
) : FlickerRemoteDataSorce {

    private var hasLoadedAll = false

    override suspend fun loadPhotosByPage(tag: String, page: Int): List<FlickerPhoto> {
        // Reset state.
        if (page == 1) hasLoadedAll = false

        // It's done. There is no pages anymore.
        // So in order to avoid unnecessary requests, we cancel the flow.
        if (hasLoadedAll) return listOf()


        // Call api to get results.
        val result = api.getPaginatedPhotos(tag, page).also {

            // Every photo has been loaded. There is no pages anymore.
            if (it.page == it.pages) {
                hasLoadedAll = true
            }
        }


        // Photos result.
        val flickerPhoto = arrayListOf<FlickerPhoto>()

        // Load photos from result.
        result.photo.forEach { photo ->

            // Try to load photo url.
            // If it exists so I create a flicker photo instance add it on the response list.
            loadLargeSquarePhotoUrl(photo.id)?.let { photoUrl ->
                flickerPhoto.add(photo.toFlickerPhoto(tag, photoUrl))
            }
        }

        return flickerPhoto
    }

    private suspend fun loadLargeSquarePhotoUrl(photoId: Long): String? {
        return api.getPhotoSizes(photoId).response.photos.find {
            it.size == "Large Square"
        }?.source
    }

}

private fun SearchPhotoResponse.toFlickerPhoto(tag: String, photoUrl: String): FlickerPhoto {
    return FlickerPhoto(
        id = this.id,
        tag = tag,
        title = this.title,
        sourceUrl = photoUrl
    )
}