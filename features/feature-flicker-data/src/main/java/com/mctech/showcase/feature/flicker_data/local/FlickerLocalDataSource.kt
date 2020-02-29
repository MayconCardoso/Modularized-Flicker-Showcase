package com.mctech.showcase.feature.flicker_data.local

import com.mctech.showcase.feature.flicker_data.FlickerStorableService
import com.mctech.showcase.feature.flicker_data.local.dao.RoomFlickerPhotoDao
import com.mctech.showcase.feature.flicker_data.local.entity.RoomFlickerPhotoEntity
import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
class FlickerLocalDataSource(
    private val flickerPhotoDao: RoomFlickerPhotoDao
) : FlickerStorableService {
    override suspend fun save(tag: String, page: Int, photos: List<FlickerPhoto>) {
        flickerPhotoDao.savePhotos(
            photos.map {
                RoomFlickerPhotoEntity(
                    id = it.id,
                    tag = tag,
                    page = page,
                    title = it.title,
                    sourceUrl = it.sourceUrl
                )
            }
        )
    }

    override suspend fun restore(tag: String, page: Int): List<FlickerPhoto>? {
        return flickerPhotoDao.loadPhotosByTagAndPage(tag, page).map {
            FlickerPhoto(
                id = it.id,
                tag = it.tag,
                title = it.title,
                sourceUrl = it.sourceUrl
            )
        }
    }

    override suspend fun deleteAllByTag(tag: String) {
        flickerPhotoDao.delete(tag)
    }
}