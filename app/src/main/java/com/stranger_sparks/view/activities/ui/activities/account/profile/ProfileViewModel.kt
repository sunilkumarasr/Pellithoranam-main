package com.stranger_sparks.view.activities.ui.activities.account.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.CastResponse
import com.stranger_sparks.data_model.Caste
import com.stranger_sparks.data_model.CitysListResponse
import com.stranger_sparks.data_model.LoginResponse
import com.stranger_sparks.data_model.StatusMessageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProfileViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _citysListData = MutableLiveData<CitysListResponse?>()
    val cityListLiveData: LiveData<CitysListResponse?> get() = _citysListData

    private val _casteListData = MutableLiveData<List<Caste>>()
    val casteListData: LiveData<List<Caste>> get() = _casteListData

    private val _helpLiveData = MutableLiveData<StatusMessageResponse?>()
    val helpLiveData: LiveData<StatusMessageResponse?> get() = _helpLiveData

    fun contactUs(name: String,
                      email: String,
                      phone: String,
                      subject: String,
                      message: String,) {
        strangerSparksRepository.contactUs(name, email, phone,subject, message).enqueue(object :
            Callback<StatusMessageResponse> {
            override fun onResponse(call: Call<StatusMessageResponse>, response: Response<StatusMessageResponse>) {
                if (response.isSuccessful) {
                    _helpLiveData.value = response.body()
                } else {
                    _helpLiveData.value =null
                }
            }

            override fun onFailure(call: Call<StatusMessageResponse>, t: Throwable) {
                _helpLiveData.value = null
            }
        })
    }


    private val _upDateProfileLiveData = MutableLiveData<LoginResponse?>()
    val upDateProfileLiveData: LiveData<LoginResponse?> get() = _upDateProfileLiveData

    fun updateProfile(
        full_name: RequestBody,
        familyname: RequestBody,
        fathername: RequestBody,
        gender: RequestBody,
        hobbies: RequestBody,
        age: RequestBody,
        phone: RequestBody,
        alterNativeNumber: RequestBody,
        height: RequestBody,
        weight: RequestBody,
        profession: RequestBody,
        education: RequestBody,
        company: RequestBody,
        designation: RequestBody,
        salary: RequestBody,
        religion: RequestBody,
        location: RequestBody,
        languages: RequestBody,
        description: RequestBody,
        user_id: RequestBody,
        caste: RequestBody,
        image_outlet: MultipartBody.Part) {
        strangerSparksRepository.updateProfile( full_name,
            familyname,
            fathername,
            gender,
            hobbies,
            age,
            phone,
            alterNativeNumber,
            height,
            weight,
            profession,
            education,
            company,
            designation,
            salary,
            religion,
            location,
            languages,
            description,
            user_id,
            caste,
            image_outlet).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _upDateProfileLiveData.value = response.body()
                } else {
                    _upDateProfileLiveData.value =null
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _upDateProfileLiveData.value = null
            }
        })
    }

    fun updateProfilewithoutpic(
        full_name: RequestBody,
        familyname: RequestBody,
        fathername: RequestBody,
        gender: RequestBody,
        hobbies: RequestBody,
        age: RequestBody,
        phone: RequestBody,
        birth_star: RequestBody,
        height: RequestBody,
        weight: RequestBody,
        profession: RequestBody,
        education: RequestBody,
        company: RequestBody,
        designation: RequestBody,
        salary: RequestBody,
        religion: RequestBody,
        location: RequestBody,
        languages: RequestBody,
        description: RequestBody,
        user_id: RequestBody,
        caste: RequestBody,
        ) {
        strangerSparksRepository.updateProfilewithoutpic(full_name,
            familyname,
            fathername,
            gender,
            hobbies,
            age,
            phone,
            birth_star,
            height,
            weight,
            profession,
            education,
            company,
            designation,
            salary,
            religion,
            location,
            languages,
            description,
            user_id,caste).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    _upDateProfileLiveData.value = response.body()
                } else {
                    _upDateProfileLiveData.value =null
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _upDateProfileLiveData.value = null
            }
        })
    }

    fun getCitysList() {
        strangerSparksRepository.getCitysList().enqueue(object :
            Callback<CitysListResponse> {
            override fun onResponse(call: Call<CitysListResponse>, response: Response<CitysListResponse>) {
                if (response.isSuccessful) {
                    _citysListData.value = response.body()
                } else {
                    _citysListData.value =null
                }
            }
            override fun onFailure(call: Call<CitysListResponse>, t: Throwable) {
                _citysListData.value = null
            }
        })
    }
    fun getCaste() {
        strangerSparksRepository.getCaste().enqueue(object :
            Callback<CastResponse> {
            override fun onResponse(call: Call<CastResponse>, response: Response<CastResponse>) {
                if (response.isSuccessful) {
                    _casteListData.value = response.body()!!.data
                } else {
                    _casteListData.value =ArrayList()
                }
            }
            override fun onFailure(call: Call<CastResponse>, t: Throwable) {
                _casteListData.value = ArrayList()
            }
        })
    }

}