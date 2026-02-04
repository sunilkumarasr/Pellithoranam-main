package com.stranger_sparks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.SuggestionCityResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class HomeActivityViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

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
}