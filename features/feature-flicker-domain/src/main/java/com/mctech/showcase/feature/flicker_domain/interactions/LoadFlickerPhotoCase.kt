package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.library.logger.Logger
import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto
import com.mctech.showcase.feature.flicker_domain.error.FlickerPhotoError
import com.mctech.showcase.feature.flicker_domain.error.NetworkException
import com.mctech.showcase.feature.flicker_domain.service.FlickerService

class LoadFlickerPhotoCase(
    private val service: FlickerService,
    private val logger: Logger
) {
    suspend fun execute(tag: String): Result<List<FlickerPhoto>> = try {
        Result.Success(service.loadFirstPageOfPhotos(tag))
    } catch (error: Exception) {
        logger.e(e = error)

        Result.Failure(
            if (error is NetworkException) error
            else FlickerPhotoError.UnknownException
        )
    }
}