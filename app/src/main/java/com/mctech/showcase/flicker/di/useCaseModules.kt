package com.mctech.showcase.flicker.di

import com.mctech.showcase.feature.flicker_domain.interactions.*
import org.koin.dsl.module

val useCaseModules = module {
    factory {
        LoadFlickerPhotoCase(
            service = get(),
            logger = get()
        )
    }

    factory {
        LoadNextPageOfFlickerPhotosCase(
            service = get(),
            logger = get()
        )
    }

    factory {
        CleanFlickerPhotosCacheCase(
            service = get(),
            logger = get()
        )
    }

    factory {
        SaveTagCase(
            service = get(),
            logger = get()
        )
    }

    factory {
        LoadTagHistoryCase(
            service = get()
        )
    }
}