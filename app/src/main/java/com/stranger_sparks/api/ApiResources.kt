package bfer.fmis.ap.gov.api

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET

interface ApiResources {
    @GET("all")
    fun getCountries(
    ): Call<JsonArray>
}