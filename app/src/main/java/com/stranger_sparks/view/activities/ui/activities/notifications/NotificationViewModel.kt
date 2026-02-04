package com.stranger_sparks.view.activities.ui.activities.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.NotificationsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class NotificationViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _notificationsLiveData = MutableLiveData<NotificationsResponse?>()
    val notificationsLiveData: LiveData<NotificationsResponse?> get() = _notificationsLiveData



    fun notificationsLiveData(user_id: String,per_page: String,page_number: Int) {
        strangerSparksRepository.notificationList(user_id, per_page, page_number).enqueue(object :
            Callback<NotificationsResponse> {
            override fun onResponse(call: Call<NotificationsResponse>, response: Response<NotificationsResponse>) {
                if (response.isSuccessful) {
                    _notificationsLiveData.value = response.body()
                } else {
                    _notificationsLiveData.value =null
                }
            }

            override fun onFailure(call: Call<NotificationsResponse>, t: Throwable) {
                _notificationsLiveData.value = null
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