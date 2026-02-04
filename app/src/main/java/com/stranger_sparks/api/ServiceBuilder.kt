package bfer.fmis.ap.gov.api

import com.stranger_sparks.api.BASE_URL_API
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(logging)
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient.build())
    private val retrofit = retrofitBuilder.build()

    fun <T> builderService(serviceType: Class<T>): T{
        return retrofit.create(serviceType)
    }
}