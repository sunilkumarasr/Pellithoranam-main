package com.stranger_sparks.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.stranger_sparks.data_model.LoginResponse

class SharedPreferenceManager(val context: Context) {

    private val PREFS_NAME = "stranger_sparks"
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveLoginResponse(user: LoginResponse) {
        sharedPref.edit().putString("user_login_data", Gson().toJson(user)).apply()
    }

    fun getSavedLoginResponseUser(): LoginResponse? {
        val data = sharedPref.getString("user_login_data", null) ?: return null
        return Gson().fromJson(data, LoginResponse::class.java)
    }

    fun saveCallStatusValue(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    fun getCallStatusValue(key: String): String? {
        return sharedPref.getString(key, null)
    }

    fun saveCallUserIdValue(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }
    fun getCallUserIdValue(key: String): String? {
        return sharedPref.getString(key, null)
    }

    fun clearAllData(): Boolean{
        val editor=sharedPref.edit()
        editor.clear()
        editor.apply()
        return true
    }

}