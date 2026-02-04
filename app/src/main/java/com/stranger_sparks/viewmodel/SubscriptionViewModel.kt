package com.stranger_sparks.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.GetAboutTermsPrivacyDTO
import com.stranger_sparks.data_model.LoginResponse
import com.stranger_sparks.data_model.SubscriptionPlansDTO
import com.stranger_sparks.databinding.ActivitySignInSignUpBinding
import com.stranger_sparks.databinding.FragmentLoginBinding
import com.stranger_sparks.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SubscriptionViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {


    private val _inputSignal = MutableLiveData<String?>()

    val inputSignal: LiveData<String?> get() = _inputSignal



    private var _getSubscriptionLiveData = MutableLiveData<SubscriptionPlansDTO?>()
    val getSubscriptionLiveData: MutableLiveData<SubscriptionPlansDTO?> get() = _getSubscriptionLiveData

    fun getSubscriptionList() {

        strangerSparksRepository.getSubscriptionList().enqueue(object :
            Callback<SubscriptionPlansDTO> {
            override fun onResponse(call: Call<SubscriptionPlansDTO>, response: Response<SubscriptionPlansDTO>) {
                if (response.isSuccessful) {
                    _getSubscriptionLiveData.value = response.body()
                } else {
                    _getSubscriptionLiveData.value =null
                }
            }

            override fun onFailure(call: Call<SubscriptionPlansDTO>, t: Throwable) {
                //_zoLabAnalysisReportLiveData = null
            }
        })
    }

    private var _addPaymentLiveData = MutableLiveData<LoginResponse?>()
    val addPaymentLiveData: MutableLiveData<LoginResponse?> get() = _addPaymentLiveData

    fun addPaymentLiveData(user_id: String, subscription_id: String) {

        strangerSparksRepository.addPayment(user_id, subscription_id).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _addPaymentLiveData.value = response.body()
                } else {
                    _addPaymentLiveData.value =null
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _addPaymentLiveData.value = null
            }
        })
    }
    var binding: ActivitySignInSignUpBinding? = null
    fun backPressedClick() {
        _inputSignal.value = Constants.ObserverEvents.BACK_PRESS.toString()
    }
    fun gotoOtpScreen(){
        _inputSignal.value = Constants.ObserverEvents.GOTO_OTP.toString()
    }
    fun gotoSuccessMessageScreen(){
        _inputSignal.value = Constants.ObserverEvents.GOTO_SUBSCRIPTION_SUCCESS.toString()
    }fun gotoHomeScreen(){
        _inputSignal.value = Constants.ObserverEvents.GOTO_HOME_SCREEN.toString()
    }
}