package com.stranger_sparks.view.activities.ui.activities.account.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.GalleryImagesResponse
import com.stranger_sparks.data_model.StatusMessageResponse
import com.stranger_sparks.data_model.WalletTransectionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GalleryViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _savedGalleryImages = MutableLiveData<GalleryImagesResponse?>()
    val savedGalleryImages: LiveData<GalleryImagesResponse?> get() = _savedGalleryImages

    private val _deleteGalleryImage = MutableLiveData<StatusMessageResponse?>()
    val deleteGalleryImage: LiveData<StatusMessageResponse?> get() = _deleteGalleryImage



    fun savedGalleryImages(user_id: String) {
        strangerSparksRepository.getGalleryProfile(user_id).enqueue(object :
            Callback<GalleryImagesResponse> {
            override fun onResponse(call: Call<GalleryImagesResponse>, response: Response<GalleryImagesResponse>) {
                if (response.isSuccessful) {
                    _savedGalleryImages.value = response.body()
                } else {
                    _savedGalleryImages.value =null
                }
            }

            override fun onFailure(call: Call<GalleryImagesResponse>, t: Throwable) {
                _savedGalleryImages.value = null
            }
        })
    }
    fun deleteGalleryImage(id: String) {
        strangerSparksRepository.galleryImageDelete(id).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(call: Call<StatusMessageResponse>, response: Response<StatusMessageResponse>) {
                if (response.isSuccessful) {
                    _deleteGalleryImage.value = response.body()
                } else {
                    _deleteGalleryImage.value =null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _deleteGalleryImage.value = null
            }
        })
    }
}