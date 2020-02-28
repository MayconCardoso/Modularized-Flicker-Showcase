package com.mctech.showcase.flicker.di

import com.mctech.library.logger.Logger
import com.mctech.library.logger_android.LogcatLogger
import org.koin.dsl.module

val loggingModule = module {
    single<Logger> { LogcatLogger }
}