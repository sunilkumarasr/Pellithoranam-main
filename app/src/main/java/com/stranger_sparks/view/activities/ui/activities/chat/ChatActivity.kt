package com.stranger_sparks.view.activities.ui.activities.chat

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.stranger_sparks.R
import com.stranger_sparks.adapterrs.AllMessageAdapter
import com.stranger_sparks.adapterrs.DatingMatchesAdapter
import com.stranger_sparks.adapterrs.NewMatchesAdapter
import com.stranger_sparks.databinding.ActivityChatBinding
import com.stranger_sparks.databinding.ActivityHelpBinding
import com.stranger_sparks.datamodels.TransactionDataModel
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.SignInSignUpActivity
import com.stranger_sparks.view.activities.ui.activities.help.HelpActivityViewModel

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding

    private lateinit var  newMatchesAdapter: NewMatchesAdapter
    private lateinit var  allMessageAdapter: AllMessageAdapter
    private var dataList = mutableListOf<TransactionDataModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        val viewModel: ChatViewModel =
            ViewModelProvider(this).get(ChatViewModel::class.java);
        viewModel.binding = binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.viewModel!!.observerEvents.observe(this) { observerEvents ->
            observerEvents?.let { validateViewModelEvents(observerEvents) }
        }

        binding.rcvNewMatches.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, true)
        newMatchesAdapter = NewMatchesAdapter(this)
        binding.rcvNewMatches.adapter = newMatchesAdapter

        binding.rcvAllMessages.layoutManager = LinearLayoutManager(this)
        allMessageAdapter = AllMessageAdapter(this)
        binding.rcvAllMessages.adapter = allMessageAdapter

        dataList.add(TransactionDataModel("Ruby Ramon", "Credit Card", "21,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Debit Card", "22,659", "Cancel"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Internet Bank", "23,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Mobile Wallet", "27,659", "Cancel"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Credit Card", "20,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Credit Card", "5,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Debit Card", "24,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Internet Bank", "8,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Credit Card", "6,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Debit Card", "3,659", "Cancel"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Wallet", "1,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Reward Points", "10,659", "Completed"))
        dataList.add(TransactionDataModel("Ruby Ramon", "Promotional Points", "659", "Completed"))

        newMatchesAdapter.setDataList(dataList)
        allMessageAdapter.setDataList(dataList)

        binding.ivLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to logout?")
            builder.setTitle("Alert !")
            builder.setCancelable(false)
            builder.setPositiveButton(
                "Yes",
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    if(SharedPreferenceManager(this).clearAllData()){
                        Intent(applicationContext, SignInSignUpActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                } as DialogInterface.OnClickListener)
            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                    // If user click no then dialog box is canceled.
                    dialog.cancel()
                } as DialogInterface.OnClickListener)
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun validateViewModelEvents(observerEvents: Constants.ObserverEvents) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN) {

            finish()
        }
    }
}