package com.mctech.showcase.feature.flicker_data.photo.remote

import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
interface FlickerRemoteDataSorce {
    suspend fun loadPhotosByPage(tag : String, page : Int) : List<FlickerPhoto>
}