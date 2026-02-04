package com.stranger_sparks.api

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast


const val BASE_URL_API: String = "https://restcountries.com/v3.1/"
const val WEATHER_DATA: String ="data/2.5/onecall?"
const val API_KEY: String ="95a4d2f86f3df5295333debb7d451f05"
const val UNITS: String ="units"
const val SHARED_DB: String ="shred_db"
const val AGORA_APP_ID: String ="409f045d1b02482ab8b3a4af3f567fc3"


class Constants{
    companion object{
        fun hideKeyboard(context: Context, view: View) {
            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}
