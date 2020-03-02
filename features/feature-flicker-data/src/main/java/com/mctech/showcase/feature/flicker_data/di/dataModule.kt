package com.mctech.showcase.feature.flicker_data.di

import com.mctech.showcase.feature.flicker_data.photo.FlickerStorableService
import com.mctech.showcase.feature.flicker_data.photo.FlickerStrategyRepository
import com.mctech.showcase.feature.flicker_data.photo.cache.FlickerCacheDataSource
import com.mctech.showcase.feature.flicker_data.photo.local.FlickerLocalDataSource
import com.mctech.showcase.feature.flicker_data.photo.remote.FlickerRemoteDataSource
import com.mctech.showcase.feature.flicker_data.photo.remote.FlickerRemoteDataSourceImpl
import com.mctech.showcase.feature.flicker_data.tag_history.FlickerTagHistoryDataSource
import com.mctech.showcase.feature.flicker_data.tag_history.FlickerTagHistoryRepository
import com.mctech.showcase.feature.flicker_data.tag_history.local.FlickerLocalTagHistoryDataSource
import com.mctech.showcase.feature.flicker_domain.service.FlickerService
import com.mctech.showcase.feature.flicker_domain.service.FlickerTagHistoryService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val flickerDataModule = module {
    // Provide remote DataSource
    single {
        FlickerRemoteDataSourceImpl(
            api = get()
        ) as FlickerRemoteDataSource
    }


    // Provide local DataSource
    single(named("local")) {
        FlickerLocalDataSource(
            flickerPhotoDao = get()
        ) as FlickerStorableService
    }


    // Provide cache DataSource
    single(named("cache")) {
        FlickerCacheDataSource() as FlickerStorableService
    }


    // Provide repository
    single {
        FlickerStrategyRepository(
            cacheDataSource = get(named("cache")),
            localDataSource = get(named("local")),
            remoteDataSource = get()
        ) as FlickerService
    }

    // Tag DataSource
    single {
        FlickerLocalTagHistoryDataSource(
            flickerHistoryDao = get()
        ) as FlickerTagHistoryDataSource
    }

    // Tag Repository
    single {
        FlickerTagHistoryRepository(
            dataSource = get()
        ) as FlickerTagHistoryService
    }
}
