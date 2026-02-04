package com.stranger_sparks.view.activities.ui.activities.call_List

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.CallListAdapter
import com.stranger_sparks.databinding.ActivityCallHistoryListBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.SharedPreferenceManager
import javax.inject.Inject

class CallHistoryListActivity : AppCompatActivity() {

    lateinit var binding: ActivityCallHistoryListBinding

    @Inject
    lateinit var viewModel: CallViewModel

    lateinit var userID: String
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    private lateinit var callListAdapter: CallListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        ConstantUtils.darkModeDisable()
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityCallHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ConstantUtils.changeNotificationBarColor(
            this,
            ContextCompat.getColor(this, R.color.app_red),
            false
        )

        sharedPreferenceManager = SharedPreferenceManager(this@CallHistoryListActivity)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()

        Log.e("userID_",userID)
        //list api
        viewModel.getCallHistory(userID)
        binding.recyclerView.layoutManager = GridLayoutManager(this@CallHistoryListActivity, 1)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.isNestedScrollingEnabled = false
        callListAdapter = CallListAdapter(this@CallHistoryListActivity)
        binding.recyclerView.adapter = callListAdapter

        viewModel.callHistory.observe(this@CallHistoryListActivity) { response ->
            if (response?.status == true) {
                binding.progressbar.visibility = View.GONE
                binding.tvNoRecordsDefault.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE

                val originalList = response.data
                val reversedList = originalList.reversed() // returns a new List in reverse order

                callListAdapter.setDataList(reversedList)
                callListAdapter.notifyDataSetChanged()
            } else {
                binding.tvNoRecordsDefault.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.progressbar.visibility = View.GONE
            }
        }

        binding.ivClose.setOnClickListener {
            finish()
        }



    }

    override fun onResume() {
        super.onResume()
        viewModel.getCallHistory(userID)
    }
}