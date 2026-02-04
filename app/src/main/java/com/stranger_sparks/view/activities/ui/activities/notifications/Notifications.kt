package com.stranger_sparks.view.activities.ui.activities.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.NotificationAdapter
import com.stranger_sparks.databinding.ActivityNotifications2Binding
import com.stranger_sparks.fcm.ZegoCallManager
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.SharedPreferenceManager
import javax.inject.Inject

class Notifications : AppCompatActivity() {
    lateinit var binding: ActivityNotifications2Binding
    @Inject
    lateinit var viewModel: NotificationViewModel
    private lateinit var notificationAdapter: NotificationAdapter
    lateinit var userID: String

    //pagnation
    private var isLoading = false
    private var currentPage = 0

    /*private var dataList = mutableListOf<String>(
        "Big Discount, Hurry!",
        "Sara Christin liked you back",
        "You liked Ruby",
        "New proposal from Sansa Ben",
        "Myley Corbyn liked you"
    )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityNotifications2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.black), false)

        val sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupRecyclerView()
        loadNotifications(currentPage)

        Log.e("userID_",userID)

        viewModel.notificationsLiveData.observe(this, Observer { response ->
            binding.progressbar.visibility = View.GONE
            if (response?.status == true) {
                binding.tvNoRecordsDefault.visibility = View.GONE
                binding.rcvNotifications.visibility = View.VISIBLE
                response.data?.let { data ->
                    if (currentPage == 0) {
                        notificationAdapter.setDataList(data)
                    } else {
                        notificationAdapter.addDataList(data)
                    }
                    notificationAdapter.notifyDataSetChanged()
                }
                isLoading = false
            } else {
                if (currentPage == 0) { // Only show no records if it's the first page
                    binding.tvNoRecordsDefault.visibility = View.VISIBLE
                    binding.rcvNotifications.visibility = View.GONE
                }
                isLoading = false
            }
        })

        // Add scroll listener for pagination
        binding.rcvNotifications.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && firstVisibleItemPosition + visibleItemCount >= totalItemCount - 5) {
                    // Load more items when nearing the end (e.g., 5 items before the end)
                    currentPage++
                    loadNotifications(currentPage)
                }
            }
        })

        binding.ivClose.setOnClickListener {
            finish()
        }

        //incoming call received
        incomingCallReceived()


    }


    private fun incomingCallReceived() {
        //incoming call received
        val sharedPreferenceManager = SharedPreferenceManager(this)
        val userName = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.name.orEmpty()
        ZegoCallManager.initialize(application, userID, userName)
    }


    private fun setupRecyclerView() {
        binding.rcvNotifications.layoutManager = LinearLayoutManager(this)
        notificationAdapter = NotificationAdapter(this)
        binding.rcvNotifications.adapter = notificationAdapter
    }

    private fun loadNotifications(page: Int) {
        if (isLoading) return
        isLoading = true
        binding.progressbar.visibility = View.VISIBLE
        viewModel.notificationsLiveData(userID, "20", page)
    }

    private fun validateViewModelEvents(observerEvents: String) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()) {

            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        //incoming call received
        incomingCallReceived()
    }

}