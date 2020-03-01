package com.mctech.showcase.feature.flicker_data.photo.remote

import com.mctech.showcase.feature.flicker_data.photo.remote.api.FlickerPhotoApi
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
        val result = api.getPaginatedPhotos(tag, page).photos.also {

            // Every photo has been loaded. There is no pages anymore.
            if (it.page == it.pages) {
                hasLoadedAll = true
            }
        }

        // Map result to domain entity.
        return result.photo.filter {
            it.thumbnailUtl?.isNotBlank() ?: false &&
            it.originalPhotoUrl?.isNotBlank() ?: false
        }.map {
            FlickerPhoto(
                id = it.id,
                tag = tag,
                title = it.title,
                sourceUrl = it.originalPhotoUrl.orEmpty(),
                thumbnailUrl = it.thumbnailUtl.orEmpty()
            )
        }
    }
}