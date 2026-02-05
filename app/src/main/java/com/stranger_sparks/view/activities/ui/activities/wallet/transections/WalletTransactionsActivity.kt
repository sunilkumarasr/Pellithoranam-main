package com.stranger_sparks.view.activities.ui.activities.wallet.transections

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.WalletTransactionAdapter
import com.stranger_sparks.data_model.WalletTransectionResponse
import com.stranger_sparks.databinding.ActivityWalletTransectionsBinding
import com.stranger_sparks.datamodels.TransactionDataModel
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.SharedPreferenceManager
import javax.inject.Inject

class WalletTransactionsActivity : AppCompatActivity() {
    lateinit var binding: ActivityWalletTransectionsBinding
    @Inject
    lateinit var viewModel: WalletTransactionsViewModel
    private lateinit var  walletTransactionAdapter: WalletTransactionAdapter
    private var dataList = mutableListOf<WalletTransectionResponse.Data>()
    lateinit var userID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityWalletTransectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.rbAll.isChecked = true
        val sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()
        viewModel.walletTransactions(userID, "")
        binding.rcvPaymentTransaction.layoutManager = LinearLayoutManager(this)
        walletTransactionAdapter = WalletTransactionAdapter(this)
        binding.rcvPaymentTransaction.adapter = walletTransactionAdapter
        viewModel.walletTransactions.observe(this){
            if(it?.status == true) {
                binding.tvNoRecordsDefault.visibility = View.GONE
                binding.rcvPaymentTransaction.visibility = View.VISIBLE
                it?.data?.let { it1 -> walletTransactionAdapter.setDataList(it1) }
                walletTransactionAdapter.notifyDataSetChanged()
            }else{
                binding.tvNoRecordsDefault.visibility = View.VISIBLE
                binding.rcvPaymentTransaction.visibility = View.GONE
            }
        }

        viewModel.inputSignal.observe(this) {

            if (it != null) {
                validateViewModelEvents(it)

            }
        }

        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.rgType.setOnCheckedChangeListener { radioGroup, checkedId ->
            if(checkedId == R.id.rbAll){
                viewModel.walletTransactions(userID, "")
            }else if(checkedId == R.id.rbCompleted){
                viewModel.walletTransactions(userID, "Completed")
            }else if(checkedId == R.id.rbCancel){
                viewModel.walletTransactions(userID, "Cancel")
            }
        }
    }
    private fun validateViewModelEvents(observerEvents: String) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()) {

            finish()
        }
    }
}