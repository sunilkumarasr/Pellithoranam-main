package com.stranger_sparks.view.activities.ui.activities.my_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.CitysListResponse
import com.stranger_sparks.data_model.SuggestionCityResponse
import com.stranger_sparks.data_model.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MyAccountViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _citysListData = MutableLiveData<CitysListResponse?>()
    val cityListLiveData: LiveData<CitysListResponse?> get() = _citysListData


    private val _searchUserByLocationLiveData = MutableLiveData<UserProfileResponse?>()
    val searchUserByLocationLiveData: LiveData<UserProfileResponse?> get() = _searchUserByLocationLiveData

    //last time used api send three carecters of city name after that showing city name
    private val _citiesLiveData = MutableLiveData<SuggestionCityResponse?>()
    val citiesLiveData: LiveData<SuggestionCityResponse?> get() = _citiesLiveData
    fun citiesLiveData(cityName: String) {
        strangerSparksRepository.citySuggestion(cityName).enqueue(object :
            Callback<SuggestionCityResponse> {
            override fun onResponse(call: Call<SuggestionCityResponse>, response: Response<SuggestionCityResponse>) {
                if (response.isSuccessful) {
                    _citiesLiveData.value = response.body()
                } else {
                    _citiesLiveData.value =null
                }
            }

            override fun onFailure(call: Call<SuggestionCityResponse>, t: Throwable) {
                _citiesLiveData.value = null
            }
        })
    }

    fun searchUserByLocationLiveData(cityName: String, user_id: String) {
        strangerSparksRepository.searchUserByLocation(cityName, user_id).enqueue(object :
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

    fun getCitysList() {
        strangerSparksRepository.getCitysList().enqueue(object :
            Callback<CitysListResponse> {
            override fun onResponse(call: Call<CitysListResponse>, response: Response<CitysListResponse>) {
                if (response.isSuccessful) {
                    _citysListData.value = response.body()
                } else {
                    _citysListData.value =null
                }
            }
            override fun onFailure(call: Call<CitysListResponse>, t: Throwable) {
                _citysListData.value = null
            }
        })
    }


}