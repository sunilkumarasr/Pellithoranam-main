package com.stranger_sparks.view.activities.ui.activities.display_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.GalleryImagesResponse
import com.stranger_sparks.data_model.GetUserProfileResponse
import com.stranger_sparks.data_model.ManageSubscriptionResponse
import com.stranger_sparks.data_model.ProfileViewDetailsResponse
import com.stranger_sparks.data_model.StatusMessageDataResponse
import com.stranger_sparks.data_model.StatusMessageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DisplayUserViewModel @Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) :
    ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _getUserProfileLiveData = MutableLiveData<GetUserProfileResponse?>()
    val getUserProfileLiveData: LiveData<GetUserProfileResponse?> get() = _getUserProfileLiveData

    private val _profileViewDetails = MutableLiveData<ProfileViewDetailsResponse?>()
    val profileViewDetailsLiveData: LiveData<ProfileViewDetailsResponse?> get() = _profileViewDetails


    private val _userstartcall = MutableLiveData<ProfileViewDetailsResponse?>()
    val userstartcallData: LiveData<ProfileViewDetailsResponse?> get() = _userstartcall


    private val _savedGalleryImages = MutableLiveData<GalleryImagesResponse?>()
    val savedGalleryImages: LiveData<GalleryImagesResponse?> get() = _savedGalleryImages

    private val _likeHitChangeLiveData = MutableLiveData<StatusMessageResponse?>()
    val likeHitChangeLiveData: LiveData<StatusMessageResponse?> get() = _likeHitChangeLiveData

    private val _viewProfileLiveData = MutableLiveData<StatusMessageResponse?>()
    val viewProfileLiveData: LiveData<StatusMessageResponse?> get() = _viewProfileLiveData


    private val _manageSubscriptionV2LiveData = MutableLiveData<ManageSubscriptionResponse?>()
    val manageSubscriptionV2LiveData: LiveData<ManageSubscriptionResponse?> get() = _manageSubscriptionV2LiveData


    private val _callUser = MutableLiveData<StatusMessageDataResponse?>()
    val callUser: LiveData<StatusMessageDataResponse?> get() = _callUser

    private val _callProfile = MutableLiveData<StatusMessageDataResponse?>()
    val callProfile: LiveData<StatusMessageDataResponse?> get() = _callProfile

    //    call audio
    private val _userAudioCallLiveData = MutableLiveData<StatusMessageResponse?>()
    val userAudioCallLiveData: LiveData<StatusMessageResponse?> get() = _userAudioCallLiveData

    //    call Video
    private val _userVideoCallLiveData = MutableLiveData<StatusMessageResponse?>()
    val userVideoCallLiveData: LiveData<StatusMessageResponse?> get() = _userVideoCallLiveData

    fun getUserProfileLiveData(user_id: String, profile_id: String) {
        strangerSparksRepository.getUserProfile(user_id, profile_id).enqueue(object :
            Callback<GetUserProfileResponse> {
            override fun onResponse(
                call: Call<GetUserProfileResponse>,
                response: Response<GetUserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    _getUserProfileLiveData.value = response.body()
                } else {
                    _getUserProfileLiveData.value = null
                }
            }

            override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
                _getUserProfileLiveData.value = null
            }
        })
    }

    fun profileViewDetails(user_id: String, profile_id: String) {
        strangerSparksRepository.profileViewDetails(user_id, profile_id).enqueue(object :
            Callback<ProfileViewDetailsResponse> {
            override fun onResponse(
                call: Call<ProfileViewDetailsResponse>,
                response: Response<ProfileViewDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    _profileViewDetails.value = response.body()
                } else {
                    _profileViewDetails.value = null
                }
            }

            override fun onFailure(call: Call<ProfileViewDetailsResponse>, t: Throwable) {
                _profileViewDetails.value = null
            }
        })
    }

    fun userstartcall(user_id: String, profile_id: String, type: String) {
        strangerSparksRepository.userstartcall(user_id, profile_id, type).enqueue(object :
            Callback<ProfileViewDetailsResponse> {
            override fun onResponse(
                call: Call<ProfileViewDetailsResponse>,
                response: Response<ProfileViewDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    _userstartcall.value = response.body()
                } else {
                    _userstartcall.value = null
                }
            }

            override fun onFailure(call: Call<ProfileViewDetailsResponse>, t: Throwable) {
                _userstartcall.value = null
            }
        })
    }


    fun savedGalleryImages(user_id: String) {
        strangerSparksRepository.getGalleryProfile(user_id).enqueue(object :
            Callback<GalleryImagesResponse> {
            override fun onResponse(
                call: Call<GalleryImagesResponse>,
                response: Response<GalleryImagesResponse>
            ) {
                if (response.isSuccessful) {
                    _savedGalleryImages.value = response.body()
                } else {
                    _savedGalleryImages.value = null
                }
            }

            override fun onFailure(call: Call<GalleryImagesResponse>, t: Throwable) {
                _savedGalleryImages.value = null
            }
        })
    }

    fun likedProfile(user_id: String, profile_id: String) {
        strangerSparksRepository.likedProfile(user_id, profile_id).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(
                call: Call<StatusMessageResponse>,
                response: Response<StatusMessageResponse>
            ) {
                if (response.isSuccessful) {
                    _likeHitChangeLiveData.value = response.body()
                } else {
                    _likeHitChangeLiveData.value = null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _likeHitChangeLiveData.value = null
            }
        })
    }

    fun viewProfileLiveData(user_id: String, profile_id: String) {
        strangerSparksRepository.viewProfile(user_id, profile_id).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(
                call: Call<StatusMessageResponse>,
                response: Response<StatusMessageResponse>
            ) {
                if (response.isSuccessful) {
                    _viewProfileLiveData.value = response.body()
                } else {
                    _viewProfileLiveData.value = null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _viewProfileLiveData.value = null
            }
        })
    }



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

    fun submitUserCallHistory(userId: String, type: String, CallerId: String, Name: String, duration: String, callType: String) {
        strangerSparksRepository.submitUserCallHistory(userId, type, CallerId, Name, duration, callType).enqueue(object :
            Callback<StatusMessageDataResponse> {
            override fun onResponse(call: Call<StatusMessageDataResponse>, response: Response<StatusMessageDataResponse>) {
                if (response.isSuccessful) {
                    _callUser.value = response.body()
                } else {
                    _callUser.value =null
                }
            }

            override fun onFailure(call: Call<StatusMessageDataResponse>, t: Throwable) {
                _callUser.value = null
            }
        })
    }


    fun submitProfileCallHistory(userId: String, type: String, CallerId: String, Name: String, duration: String, callType: String) {
        strangerSparksRepository.submitProfileCallHistory(userId, type, CallerId, Name, duration, callType).enqueue(object :
            Callback<StatusMessageDataResponse> {
            override fun onResponse(call: Call<StatusMessageDataResponse>, response: Response<StatusMessageDataResponse>) {
                if (response.isSuccessful) {
                    _callProfile.value = response.body()
                } else {
                    _callProfile.value =null
                }
            }

            override fun onFailure(call: Call<StatusMessageDataResponse>, t: Throwable) {
                _callProfile.value = null
            }
        })
    }

    fun userAudioCall(user_id: String, profile_id: String, exitTime: String) {
        strangerSparksRepository.userAudioCall(user_id, profile_id, exitTime).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(
                call: Call<StatusMessageResponse>,
                response: Response<StatusMessageResponse>
            ) {
                if (response.isSuccessful) {
                    _userAudioCallLiveData.value = response.body()
                } else {
                    _userAudioCallLiveData.value = null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _userAudioCallLiveData.value = null
            }
        })
    }


    fun userVideoCall(user_id: String, profile_id: String, exitTime: String) {
        strangerSparksRepository.userVideoCall(user_id, profile_id, exitTime).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(
                call: Call<StatusMessageResponse>,
                response: Response<StatusMessageResponse>
            ) {
                if (response.isSuccessful) {
                    _userVideoCallLiveData.value = response.body()
                } else {
                    _userVideoCallLiveData.value = null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _userVideoCallLiveData.value = null
            }
        })
    }



}