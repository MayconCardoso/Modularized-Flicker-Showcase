package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.library.logger.Logger
import com.mctech.showcase.feature.flicker_domain.service.FlickerService

class CleanFlickerPhotosCacheCase(
    private val service: FlickerService,
    private val logger: Logger
) {
    suspend fun execute(tag: String) = try {
        service.cleanCache(tag)
    }catch (exception : Exception){
        logger.e(e = exception)
    }
}