package com.mctech.showcase.feature.flicker_data.photo

import com.mctech.library.logger.Logger
import com.mctech.showcase.feature.flicker_data.photo.remote.FlickerRemoteDataSorce
import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto
import com.mctech.showcase.feature.flicker_domain.service.FlickerService

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
class FlickerStrategyRepository(
    private val cacheDataSource: FlickerStorableService,
    private val localDataSource: FlickerStorableService,
    private val remoteDataSource: FlickerRemoteDataSorce,
    private val logger: Logger
) : FlickerService {

    private var currentPage  = 1

    override suspend fun loadFirstPageOfPhotos(tag: String): List<FlickerPhoto> {
        currentPage = 1
        return internalPhotoLoader(tag, currentPage)
    }

    override suspend fun loadNextPageOfPhotos(tag: String): List<FlickerPhoto> {
        return internalPhotoLoader(tag, currentPage)
    }

    override suspend fun cleanCache(tag: String) {
        cacheDataSource.deleteAllByTag(tag)
        localDataSource.deleteAllByTag(tag)
    }

    private suspend fun internalPhotoLoader(tag : String, page : Int) : List<FlickerPhoto>{
        // Try to load from cache
        val cached = cacheDataSource.restore(tag, page).orEmpty()

        // There are items on cache
        if(cached.isNotEmpty()){
            return cached.apply {
                logger.i("REPOSITORY", "Load from cache")
                currentPage++
            }
        }

        // Try to load from database
        val local = localDataSource.restore(tag, page).orEmpty()

        // There are items on database, so save it on cache
        if(local.isNotEmpty()){
            cacheDataSource.save(tag, page, local)
            return local.apply {
                logger.i("REPOSITORY", "Load from database")
                currentPage++
            }
        }

        // Try to load from flicker server
        val remote = remoteDataSource.loadPhotosByPage(tag, page)

        // Save it on cache and database
        if(remote.isNotEmpty()){
            cacheDataSource.save(tag, page, remote)
            localDataSource.save(tag, page, remote)
        }

        return remote.apply {
            logger.i("REPOSITORY", "Load from network")
            currentPage++
        }
    }
}