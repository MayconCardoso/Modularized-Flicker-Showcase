package com.mctech.showcase.feature.flicker_data.tag_history.local

import com.mctech.showcase.feature.flicker_data.tag_history.FlickerTagHistoryDataSource
import com.mctech.showcase.feature.flicker_data.tag_history.dao.RoomTagHistoryDao
import com.mctech.showcase.feature.flicker_data.tag_history.entity.RoomTagHistoryEntity

/**
 * @author MAYCON CARDOSO on 2020-03-01.
 */
class FlickerLocalTagHistoryDataSource(
    private val flickerHistoryDao: RoomTagHistoryDao
) : FlickerTagHistoryDataSource {
    override suspend fun saveTag(tag: String) {
        flickerHistoryDao.saveTag(
            RoomTagHistoryEntity(
                tag = tag,
                date = System.currentTimeMillis()
            )
        )
    }

    override suspend fun loadAllTags(): List<String> {
        return flickerHistoryDao.loadAll().map {
            it.tag
        }
    }
}