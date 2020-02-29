package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.showcase.feature.flicker_domain.service.FlickerService

class CleanFlickerPhotosCacheCase(private val service: FlickerService) {
    suspend fun execute(tag: String) = service.cleanCache(tag)
}