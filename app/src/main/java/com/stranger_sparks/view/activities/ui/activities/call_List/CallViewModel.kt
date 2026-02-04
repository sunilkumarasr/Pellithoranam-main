package com.stranger_sparks.view.activities.ui.activities.call_List

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.CallHistoryDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class CallViewModel @Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {

    private val _callHistory = MutableLiveData<CallHistoryDataResponse?>()
    val callHistory: LiveData<CallHistoryDataResponse?> get() = _callHistory



    fun getCallHistory(userId: String) {
        strangerSparksRepository.getCallHistory(userId).enqueue(object :
            Callback<CallHistoryDataResponse> {
            override fun onResponse(call: Call<CallHistoryDataResponse>, response: Response<CallHistoryDataResponse>) {
                if (response.isSuccessful) {
                    _callHistory.value = response.body()
                } else {
                    _callHistory.value =null
                }
            }

            override fun onFailure(call: Call<CallHistoryDataResponse>, t: Throwable) {
                _callHistory.value = null
            }
        })
    }

}