package com.stranger_sparks.api_dragger_flow.di

import com.stranger_sparks.BuildConfig
import com.stranger_sparks.api_dragger_flow.retrofit.StrangerSparksApiInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApplicationModule {
    var loggingInterceptor = HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }
   // val basic = "Basic " + Base64.encodeToString(BuildConfig..toByteArray(), Base64.NO_WRAP)
    private var okHttpClient =  OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val originRequest = chain.request()

            val newRquest = originRequest.newBuilder()
                // .addHeader("Accept", "Application/JSON")
                .header("Authorization", "basic")
                .header(
                    "x-api-key",
                    BuildConfig.SITE_KEY
                )
                .header("Cache-Control","no-cache")
                //   .header("Access-Token", "${}")
                .build()
            chain.proceed(newRquest)
        })
        .cache(null)
        .addInterceptor(loggingInterceptor)
        .callTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()


    /*fun getRetroServiceInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }*/


    fun getRetroFitInstance(): Retrofit {
        return Retrofit.Builder()
            //.baseUrl(ConfigData.BASE_URL)
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): StrangerSparksApiInterface {
        return getRetroFitInstance().create(StrangerSparksApiInterface::class.java)
    }

}