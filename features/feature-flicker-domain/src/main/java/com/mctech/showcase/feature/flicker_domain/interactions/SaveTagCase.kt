package com.mctech.showcase.feature.flicker_domain.interactions

import com.mctech.showcase.feature.flicker_domain.service.FlickerTagHistoryService

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
class SaveTagCase(
    private val service: FlickerTagHistoryService
){
    suspend fun execute(tag : String){
        service.saveTag(tag)
    }
}