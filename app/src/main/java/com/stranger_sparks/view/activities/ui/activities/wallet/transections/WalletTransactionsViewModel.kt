package com.stranger_sparks.view.activities.ui.activities.wallet.transections

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stranger_sparks.api_dragger_flow.repository.StrangerSparksRepository
import com.stranger_sparks.data_model.StatusMessageDataResponse
import com.stranger_sparks.data_model.StatusMessageResponse
import com.stranger_sparks.data_model.WalletTransectionResponse
import com.stranger_sparks.databinding.ActivitySettingsBinding
import com.stranger_sparks.databinding.ActivityWalletTransectionsBinding
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WalletTransactionsViewModel@Inject constructor(private val strangerSparksRepository: StrangerSparksRepository) : ViewModel() {
    private val _inputSignal = MutableLiveData<String?>()
    val inputSignal: LiveData<String?> get() = _inputSignal

    private val _walletTransactions = MutableLiveData<WalletTransectionResponse?>()
    val walletTransactions: LiveData<WalletTransectionResponse?> get() = _walletTransactions



    fun walletTransactions(user_id: String, status_type: String) {
        strangerSparksRepository.walletTransactionsList(user_id, status_type).enqueue(object :
            Callback<WalletTransectionResponse> {
            override fun onResponse(call: Call<WalletTransectionResponse>, response: Response<WalletTransectionResponse>) {
                if (response.isSuccessful) {
                    _walletTransactions.value = response.body()
                } else {
                    _walletTransactions.value =null
                }
            }

            override fun onFailure(call: Call<WalletTransectionResponse>, t: Throwable) {
                _walletTransactions.value = null
            }
        })
    }



}



