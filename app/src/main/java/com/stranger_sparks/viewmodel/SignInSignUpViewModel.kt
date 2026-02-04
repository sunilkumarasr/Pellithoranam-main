package com.stranger_sparks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.GetAboutTermsPrivacyDTO
import com.stranger_sparks.data_model.StatusMessageResponse
import com.stranger_sparks.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SignInSignUpViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {

    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

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


    private val _loginLiveData = MutableLiveData<StatusMessageResponse?>()
    val loginLiveData: LiveData<StatusMessageResponse?> get() = _loginLiveData

    fun getUserLogin(phoneNumber: String) {
        strangerSparksRepository.getUserLogin(phoneNumber).enqueue(object : Callback<StatusMessageResponse> {
            override fun onResponse(call: Call<StatusMessageResponse>, response: Response<StatusMessageResponse>) {
                if (response.isSuccessful) {
                    _loginLiveData.value = response.body()
                } else {
                    _loginLiveData.value =null
                }
            }
            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _loginLiveData.value = null
            }
        })
    }










    fun backPressedClick() {
        _inputSignal.value = Constants.ObserverEvents.BACK_PRESS.toString()
    }
    fun gotoOtpScreen(){
        _inputSignal.value = Constants.ObserverEvents.GOTO_OTP.toString()
    }
    fun gotoSignupScreen(){
        _inputSignal.value = Constants.ObserverEvents.GOTO_SIGN_UP.toString()
    }fun gotoSignInScreen(){
        _inputSignal.value = Constants.ObserverEvents.GOTO_SIGN_IN.toString()
    }


}