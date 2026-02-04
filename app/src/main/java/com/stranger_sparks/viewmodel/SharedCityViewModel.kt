package com.stranger_sparks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedCityViewModel: ViewModel() {

    private val text = MutableLiveData<String>()

    fun setText(input: String) {
        text.value = input
    }

    fun getText(): LiveData<String>? {
        return text
    }
}