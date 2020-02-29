package com.mctech.showcase.feature.flicker_data.cache

import com.mctech.showcase.feature.flicker_data.FlickerStorableService
import com.mctech.showcase.feature.flicker_domain.entity.FlickerPhoto

/**
 * @author MAYCON CARDOSO on 2020-02-28.
 */
class FlickerCacheDataSource : FlickerStorableService {
    private val memoryCache = hashMapOf<String, List<FlickerPhoto>>()

    override suspend fun save(tag: String, page: Int, photos: List<FlickerPhoto>) = synchronized(memoryCache){
        memoryCache["$tag-$page"] = photos
    }

    override suspend fun restore(tag: String, page: Int) = synchronized(memoryCache){
        memoryCache["$tag-$page"]
    }

    override suspend fun deleteAllByTag(tag : String) = synchronized(memoryCache){
        memoryCache.forEach {
            // Remove all pages.
            if(it.key.startsWith(tag)){
                memoryCache.remove(it.key)
            }
        }
    }
}