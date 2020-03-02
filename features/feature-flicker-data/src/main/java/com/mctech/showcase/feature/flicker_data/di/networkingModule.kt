package com.mctech.showcase.feature.flicker_data.di

import com.mctech.library.networking.RetrofitBuilder
import com.mctech.showcase.feature.flicker_data.BuildConfig
import com.mctech.showcase.feature.flicker_data.photo.remote.api.FlickerPhotoApi
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val flickerNetworkingModule = module {
    single {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // It is gonna add the fixed parameters like "apiKey, format, etc"
        // in every single request by changing the original URL.
        val fixedParametersInterceptor = createFixedParametersInterceptor()

        // It is a security way to validate the right service.
        val createCertificatePinning = createCertificatePinning()

        // Create the OkHttp Client
        OkHttpClient.Builder()
            .certificatePinner(createCertificatePinning)
            .addInterceptor(logger)
            .addInterceptor(fixedParametersInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        RetrofitBuilder(
            apiURL = "https://www.flickr.com/services/rest/",
            httpClient = get()
        ) as Retrofit
    }

    // Provide API
    single<FlickerPhotoApi> {
        val retrofit: Retrofit = get()
        retrofit.create(FlickerPhotoApi::class.java)
    }
}

private fun createFixedParametersInterceptor(): Interceptor {
    return object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

            // Create a new URL with fixed parameters.
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.FlickerApiPublicKey)
                .addQueryParameter("format", "json")
                .addQueryParameter("nojsoncallback", "1")
                .build()

            // Return the intercepted URL with all fixed parameters.
            return chain.proceed(chain.request().newBuilder().url(newUrl).build())
        }
    }
}

private fun createCertificatePinning() : CertificatePinner{
    return CertificatePinner.Builder()
        .add(
            "api.flickr.com",
            "sha256/0ede18cee10b4294daa682bb6cd9e966"
        ).build()
}