package com.stranger_sparks.view.activities.ui.activities.account.add_images

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.StatusMessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AddImagesViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _uploadImagesLiveData = MutableLiveData<StatusMessageResponse?>()
    val uploadImagesLiveData: LiveData<StatusMessageResponse?> get() = _uploadImagesLiveData

    fun uploadImagesLiveData(
        user_id: RequestBody,
        image_outlet: Array<MultipartBody.Part>
    ) {
        strangerSparksRepository.galleryImage(user_id, image_outlet).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(call: Call<StatusMessageResponse>, response: Response<StatusMessageResponse>) {
                if (response.isSuccessful) {
                    _uploadImagesLiveData.value = response.body()
                } else {
                    _uploadImagesLiveData.value =null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _uploadImagesLiveData.value = null
            }
        })
    }
}