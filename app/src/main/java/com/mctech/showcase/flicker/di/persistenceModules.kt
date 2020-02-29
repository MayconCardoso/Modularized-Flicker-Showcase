package com.mctech.showcase.flicker.di

import androidx.room.Room
import com.mctech.showcase.flicker.persistence.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val persistenceModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "flicker-database"
        ).build()
    }

    single {
        val database : AppDatabase = get()
        database.flickerPhotoDao()
    }
}