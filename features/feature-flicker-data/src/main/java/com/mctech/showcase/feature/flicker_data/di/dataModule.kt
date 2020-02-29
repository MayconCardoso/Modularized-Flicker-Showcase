package com.mctech.showcase.feature.flicker_data.di

import com.mctech.showcase.feature.flicker_data.FlickerStorableService
import com.mctech.showcase.feature.flicker_data.FlickerStrategyRepository
import com.mctech.showcase.feature.flicker_data.cache.FlickerCacheDataSource
import com.mctech.showcase.feature.flicker_data.local.FlickerLocalDataSource
import com.mctech.showcase.feature.flicker_data.remote.FlickerRemoteDataSorce
import com.mctech.showcase.feature.flicker_data.remote.FlickerRemoteDataSourceImpl
import com.mctech.showcase.feature.flicker_domain.service.FlickerService
import org.koin.core.qualifier.named
import org.koin.dsl.module

val flickerDataModule = module {
    // Provide remote DataSource
    single {
        FlickerRemoteDataSourceImpl(
            api = get()
        ) as FlickerRemoteDataSorce
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
            remoteDataSource = get(),
            logger = get()
        ) as FlickerService
    }
}
