package com.stranger_sparks.view.activities.ui.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _searchUserByLocationLiveData = MutableLiveData<UserProfileResponse?>()
    private val _searchUserByFilterLiveData = MutableLiveData<UserProfileResponse?>()
    val searchUserByLocationLiveData: LiveData<UserProfileResponse?> get() = _searchUserByLocationLiveData
    val searchUserByFilterLiveData: LiveData<UserProfileResponse?> get() = _searchUserByFilterLiveData



    fun searchUserByLocationLiveData(cityName: String, user_id: String) {
        strangerSparksRepository.searchUserByLocationHome("", user_id).enqueue(object :
            Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful) {
                    _searchUserByLocationLiveData.value = response.body()
                } else {
                    _searchUserByLocationLiveData.value =null
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                _searchUserByLocationLiveData.value = null
            }
        })
    }

    fun searchUserByFiltersLiveData(searchName: String,gender: String,age: String,religion: String, address: String, user_id: String) {
        strangerSparksRepository.searchUserByFiltersHome(searchName,gender,age,religion,address, user_id).enqueue(object :
            Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
              Log.e("response Code","Response Code ${response.code()}")
                if (response.isSuccessful) {
                    _searchUserByFilterLiveData.value = response.body()
                } else {
                    _searchUserByFilterLiveData.value =null
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e("response Code","Response Code ${toString()}")

                _searchUserByFilterLiveData.value = null
            }
        })
    }



}