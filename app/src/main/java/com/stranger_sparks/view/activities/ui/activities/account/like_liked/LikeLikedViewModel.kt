package com.stranger_sparks.view.activities.ui.activities.account.like_liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.LikeLikedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LikeLikedViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _likeLikedLiveData = MutableLiveData<LikeLikedResponse?>()
    val likeLikedLiveData: LiveData<LikeLikedResponse?> get() = _likeLikedLiveData



    fun likeLikedLiveData(user_id: String, type: String) {
        strangerSparksRepository.datingMatches(user_id, type).enqueue(object :
            Callback<LikeLikedResponse> {
            override fun onResponse(call: Call<LikeLikedResponse>, response: Response<LikeLikedResponse>) {
                if (response.isSuccessful) {
                    _likeLikedLiveData.value = response.body()
                } else {
                    _likeLikedLiveData.value =null
                }
            }

            override fun onFailure(call: Call<LikeLikedResponse>, t: Throwable) {
                _likeLikedLiveData.value = null
            }
        })
    }



}



