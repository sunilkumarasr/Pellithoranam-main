package com.stranger_sparks.view.activities.ui.activities.webview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.GetAboutTermsPrivacyDTO
import com.stranger_sparks.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WebViewUrlViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()

    val inputSignal: LiveData<String?> get() = _inputSignal
    fun backPressedClick() {
        _inputSignal.value = Constants.ObserverEvents.BACK_PRESS.toString()
    }
    fun closeWebView(){
        _inputSignal.value = Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()
    }


    private var _getAboutTermsPrivacyLiveData = MutableLiveData<GetAboutTermsPrivacyDTO?>()
    val getAboutTermsPrivacyLiveData: LiveData<GetAboutTermsPrivacyDTO?> get() = _getAboutTermsPrivacyLiveData

    fun getAboutTermsPrivacy(pageNumber: String) {

        strangerSparksRepository.getStaticData(pageNumber).enqueue(object :
            Callback<GetAboutTermsPrivacyDTO> {
            override fun onResponse(call: Call<GetAboutTermsPrivacyDTO>, response: Response<GetAboutTermsPrivacyDTO>) {
                if (response.isSuccessful) {
                    _getAboutTermsPrivacyLiveData.value = response.body()
                } else {
                    _getAboutTermsPrivacyLiveData.value =null
                }
            }

            override fun onFailure(call: Call<GetAboutTermsPrivacyDTO>, t: Throwable) {
                //_zoLabAnalysisReportLiveData = null
            }
        })
    }
}