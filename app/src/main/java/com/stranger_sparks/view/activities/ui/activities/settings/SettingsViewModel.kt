package com.stranger_sparks.view.activities.ui.activities.settings

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.GetUserProfileResponse
import com.stranger_sparks.data_model.StatusMessageResponse
import com.stranger_sparks.databinding.ActivityNotifications2Binding
import com.stranger_sparks.databinding.ActivitySettingsBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) :
    ViewModel() {
    var binding: ActivitySettingsBinding? = null


    private val _logoutLiveData = MutableLiveData<StatusMessageResponse?>()
    val logoutLiveData: LiveData<StatusMessageResponse?> get() = _logoutLiveData

    fun logoutLiveData(user_id: String) {
        strangerSparksRepository.logout(user_id).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(
                call: Call<StatusMessageResponse>,
                response: Response<StatusMessageResponse>
            ) {
                if (response.isSuccessful) {
                    _logoutLiveData.value = response.body()
                } else {
                    _logoutLiveData.value = null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _logoutLiveData.value = null
            }
        })
    }
}