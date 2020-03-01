package com.mctech.showcase.feature.flicker_data.photo

import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
interface FlickerStorableService {
    suspend fun save(tag: String, page: Int, photos: List<FlickerPhoto>)
    suspend fun restore(tag: String, page: Int): List<FlickerPhoto>?
    suspend fun deleteAllByTag(tag : String)
}