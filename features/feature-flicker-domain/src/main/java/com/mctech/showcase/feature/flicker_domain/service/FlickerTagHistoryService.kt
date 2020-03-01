package com.mctech.showcase.feature.flicker_domain.service

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
interface FlickerTagHistoryService {
    suspend fun saveTag(tag: String)
    suspend fun loadAllTags(): List<String>
}