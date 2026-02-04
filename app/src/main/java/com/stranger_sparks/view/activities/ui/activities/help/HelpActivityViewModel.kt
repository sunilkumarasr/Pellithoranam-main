package com.stranger_sparks.view.activities.ui.activities.help

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.StatusMessageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HelpActivityViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _helpLiveData = MutableLiveData<StatusMessageResponse?>()
    val helpLiveData: LiveData<StatusMessageResponse?> get() = _helpLiveData

    fun contactUs(name: String,
                      email: String,
                      phone: String,
                      subject: String,
                      message: String,) {
        strangerSparksRepository.contactUs(name, email, phone,subject, message).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(call: Call<StatusMessageResponse>, response: Response<StatusMessageResponse>) {
                if (response.isSuccessful) {
                    _helpLiveData.value = response.body()
                } else {
                    _helpLiveData.value =null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _helpLiveData.value = null
            }
        })
    }
}