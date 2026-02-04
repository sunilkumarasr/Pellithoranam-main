package com.stranger_sparks.view.activities.ui.activities.chat.chat_room

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.ChatResponse
import com.stranger_sparks.data_model.LoginResponse
import com.stranger_sparks.data_model.NotificationsResponse
import com.stranger_sparks.data_model.StatusMessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ChatRoomViewModel @Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) :
    ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _sendMessageLiveData = MutableLiveData<StatusMessageResponse?>()
    val sendMessageLiveData: LiveData<StatusMessageResponse?> get() = _sendMessageLiveData

    private val _getMessageLiveData = MutableLiveData<ChatResponse?>()
    val getMessageLiveData: LiveData<ChatResponse?> get() = _getMessageLiveData

    private val _searchMessageLiveData = MutableLiveData<ChatResponse?>()
    val searchMessageLiveData: LiveData<ChatResponse?> get() = _searchMessageLiveData

    private val _deleteMessageLiveData = MutableLiveData<StatusMessageResponse?>()
    val deleteMessageLiveData: LiveData<StatusMessageResponse?> get() = _deleteMessageLiveData

    private val _deleteMessageBothData = MutableLiveData<StatusMessageResponse?>()
    val deleteMessageBothData: LiveData<StatusMessageResponse?> get() = _deleteMessageBothData

    private val _sendChartImageLiveData = MutableLiveData<StatusMessageResponse?>()
    val sendChartImageLiveData: LiveData<StatusMessageResponse?> get() = _sendChartImageLiveData

    fun sendChartImage(sender_id: RequestBody,
                       receiver_id: RequestBody,
                       image: MultipartBody.Part) {
        strangerSparksRepository.sendChartImage(sender_id, receiver_id, image).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(call: Call<StatusMessageResponse>, response: Response<StatusMessageResponse>) {
                if (response.isSuccessful) {
                    _sendChartImageLiveData.value = response.body()
                } else {
                    _sendChartImageLiveData.value =null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _sendChartImageLiveData.value = null
            }
        })
    }

    fun sendMessageLiveData(
        sender_id: String,
        receiver_id: String,
        message: String
    ) {
        strangerSparksRepository.sendMessage(sender_id, receiver_id, message).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(
                call: Call<StatusMessageResponse>,
                response: Response<StatusMessageResponse>
            ) {
                if (response.isSuccessful) {
                    _sendMessageLiveData.value = response.body()
                } else {
                    _sendMessageLiveData.value = null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _sendMessageLiveData.value = null
            }
        })
    }

    fun getMessageLiveData(
        sender_id: String,
        receiver_id: String
    ) {
        strangerSparksRepository.getMessage(sender_id, receiver_id).enqueue(object :
            Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    _getMessageLiveData.value = response.body()
                } else {
                    _getMessageLiveData.value = null
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                _getMessageLiveData.value = null
            }
        })
    }

    fun searchMessageLiveData(
        sender_id: String,
        receiver_id: String,
        message: String
    ) {
        strangerSparksRepository.searchChat(sender_id, receiver_id, message).enqueue(object :
            Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    _getMessageLiveData.value = response.body()
                } else {
                    _getMessageLiveData.value = null
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                _getMessageLiveData.value = null
            }
        })
    }

    fun delete_only_for_you_post(
        sender_id: String,
        id: String
    ) {
        strangerSparksRepository.delete_only_for_you_post(sender_id, id).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(
                call: Call<StatusMessageResponse>,
                response: Response<StatusMessageResponse>
            ) {
                if (response.isSuccessful) {
                    _deleteMessageLiveData.value = response.body()
                } else {
                    _deleteMessageLiveData.value = null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _deleteMessageLiveData.value = null
            }
        })
    }

    fun delete_only_for_both_post(
        sender_id: String,
        id: String
    ) {
        strangerSparksRepository.delete_only_for_both_post(sender_id, id).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(
                call: Call<StatusMessageResponse>,
                response: Response<StatusMessageResponse>
            ) {
                if (response.isSuccessful) {
                    _deleteMessageBothData.value = response.body()
                } else {
                    _deleteMessageBothData.value = null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _deleteMessageBothData.value = null
            }
        })
    }


}