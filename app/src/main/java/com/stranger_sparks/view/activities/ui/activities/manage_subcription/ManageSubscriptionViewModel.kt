package com.stranger_sparks.view.activities.ui.activities.manage_subcription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.LoginResponse
import com.stranger_sparks.data_model.ManageSubscriptionResponse
import com.stranger_sparks.data_model.SubscriptionPlansDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ManageSubscriptionViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _manageSubscriptionLiveData = MutableLiveData<ManageSubscriptionResponse?>()
    val manageSubscriptionLiveData: LiveData<ManageSubscriptionResponse?> get() = _manageSubscriptionLiveData



    fun manageSubscriptions(user_id: String) {
        strangerSparksRepository.manageSubscriptions(user_id).enqueue(object :
            Callback<ManageSubscriptionResponse> {
            override fun onResponse(call: Call<ManageSubscriptionResponse>, response: Response<ManageSubscriptionResponse>) {
                if (response.isSuccessful) {
                    _manageSubscriptionLiveData.value = response.body()
                } else {
                    _manageSubscriptionLiveData.value =null
                }
            }

            override fun onFailure(call: Call<ManageSubscriptionResponse>, t: Throwable) {
                _manageSubscriptionLiveData.value = null
            }
        })
    }

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


    private val _manageSubscriptionV2LiveData = MutableLiveData<ManageSubscriptionResponse?>()
    val manageSubscriptionV2LiveData: LiveData<ManageSubscriptionResponse?> get() = _manageSubscriptionV2LiveData


    fun manageSubscriptionsV2(user_id: String, type: String) {
        strangerSparksRepository.manage_subscriptions_v2(user_id,type).enqueue(object :
            Callback<ManageSubscriptionResponse> {
            override fun onResponse(call: Call<ManageSubscriptionResponse>, response: Response<ManageSubscriptionResponse>) {
                if (response.isSuccessful) {
                    _manageSubscriptionV2LiveData.value = response.body()
                } else {
                    _manageSubscriptionV2LiveData.value =null
                }
            }

            override fun onFailure(call: Call<ManageSubscriptionResponse>, t: Throwable) {
                _manageSubscriptionV2LiveData.value = null
            }
        })
    }


    private var _getSubscriptionV2LiveData = MutableLiveData<SubscriptionPlansDTO?>()
    val getSubscriptionV2LiveData: MutableLiveData<SubscriptionPlansDTO?> get() = _getSubscriptionV2LiveData

    fun getSubscriptionV2List(type: String) {
        strangerSparksRepository.getSubscriptionV2List(type).enqueue(object :
            Callback<SubscriptionPlansDTO> {
            override fun onResponse(call: Call<SubscriptionPlansDTO>, response: Response<SubscriptionPlansDTO>) {
                if (response.isSuccessful) {
                    _getSubscriptionV2LiveData.value = response.body()
                } else {
                    _getSubscriptionV2LiveData.value =null
                }
            }
            override fun onFailure(call: Call<SubscriptionPlansDTO>, t: Throwable) {
                _getSubscriptionV2LiveData.value = null
            }
        })
    }


}
/*(application: Application?) : BaseViewModel(application) {
    var binding: ActivityNotifications2Binding? = null
    fun backPressedClick() {
        observerEvents.setValue(Constants.ObserverEvents.BACK_PRESS)
    }
    fun closeNotificationScree(){
        observerEvents.setValue(Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN)
    }
}*/