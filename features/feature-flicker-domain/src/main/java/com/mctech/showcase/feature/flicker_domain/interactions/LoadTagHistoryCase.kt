package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.showcase.feature.flicker_domain.service.FlickerTagHistoryService

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
class LoadTagHistoryCase(
    private val service: FlickerTagHistoryService
) {
    suspend fun execute(): Result<List<String>> = try {
        Result.Success(service.loadAllTags())
    } catch (exception: Exception) {
        Result.Failure(exception)
    }
}