package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.library.logger.Logger
import com.mctech.showcase.feature.flicker_domain.service.FlickerTagHistoryService

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
class SaveTagCase(
    private val service: FlickerTagHistoryService,
    private val logger: Logger
) {
    suspend fun execute(tag: String) = try{
        service.saveTag(tag)
    } catch (exception : Exception){
        logger.e(e = exception)
    }
}