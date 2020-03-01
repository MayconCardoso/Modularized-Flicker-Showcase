package com.mctech.showcase.feature.flicker_domain.service

import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
interface FlickerService {
    suspend fun loadFirstPageOfPhotos(tag: String): List<FlickerPhoto>
    suspend fun loadNextPageOfPhotos(tag: String): List<FlickerPhoto>
    suspend fun cleanCache(tag: String)
}