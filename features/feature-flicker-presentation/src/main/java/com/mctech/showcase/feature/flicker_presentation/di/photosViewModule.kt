package com.mctech.showcase.feature.flicker_presentation.di

import com.mctech.showcase.feature.flicker_presentation.PhotosViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val flickerPhotosViewModule = module {
    viewModel {
        PhotosViewModel(
            loadFlickerPhotoCase = get(),
            loadNextPageOfFlickerPhotosCase = get(),
            cleanFlickerPhotosCacheCase = get(),
            saveTagCase = get(),
            loadTagHistoryCase = get()
        )
    }
}