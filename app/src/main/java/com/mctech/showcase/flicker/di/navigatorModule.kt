package com.mctech.showcase.flicker.di

import com.mctech.showcase.feature.flicker_presentation.PhotosNavigation
import com.mctech.showcase.flicker.navigation.AppNavigatorHandler
import org.koin.dsl.module

val navigatorModule = module {

    single <PhotosNavigation> {
        AppNavigatorHandler()
    }

}