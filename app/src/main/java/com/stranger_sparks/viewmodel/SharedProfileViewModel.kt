package com.stranger_sparks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.data_model.UserProfileResponse

class SharedProfileViewModel: ViewModel() {

    private val dataItem = MutableLiveData<UserProfileResponse.Data>()

    fun setDataItem(input: UserProfileResponse.Data) {
        dataItem.value = input
    }

    fun getDataItem(): LiveData<UserProfileResponse.Data>? {
        return dataItem
    }
}