package com.mctech.showcase.flicker

import android.app.Application
import com.mctech.showcase.feature.flicker_data.di.flickerDataModule
import com.mctech.showcase.feature.flicker_data.di.flickerNetworkingModule
import com.mctech.showcase.feature.flicker_presentation.di.flickerPhotosViewModule
import com.mctech.showcase.flicker.di.loggingModule
import com.mctech.showcase.flicker.di.navigatorModule
import com.mctech.showcase.flicker.di.persistenceModule
import com.mctech.showcase.flicker.di.useCaseModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initDependencyInjection()
    }

    private fun initDependencyInjection() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                listOf(
                    // Platform
                    useCaseModules,

                    // Libraries
                    loggingModule,
                    persistenceModule,
                    navigatorModule,

                    // Features
                    flickerNetworkingModule,
                    flickerDataModule,
                    flickerPhotosViewModule
                )
            )
        }
    }
}
