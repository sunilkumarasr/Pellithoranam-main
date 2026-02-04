package com.stranger_sparks.view.activities.ui.activities.manage_subcription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.PlansAdapter
import com.stranger_sparks.data_model.SubscriptionPlansDTO
import com.stranger_sparks.databinding.ActivityManageSubscriptionBinding
import com.stranger_sparks.inerfaces.OnItemClickListenerWithPosition
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.SharedPreferenceManager
import javax.inject.Inject

class ManageSubscription : AppCompatActivity() , OnItemClickListenerWithPosition {
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    lateinit var binding: ActivityManageSubscriptionBinding
    @Inject
    lateinit var viewModel: ManageSubscriptionViewModel
    lateinit var userID: String
    private var dataList: MutableList<SubscriptionPlansDTO.Data> = arrayListOf()
    lateinit var plansAdapter: PlansAdapter
    var selectedPlanId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityManageSubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.notification_bar_color_two), false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // notificationAdapter.setDataList(dataList)
        sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()

        binding.ivClose.setOnClickListener {
            finish()
        }

        viewModel.manageSubscriptions(userID)

        viewModel.getSubscriptionList()

        viewModel.getSubscriptionLiveData.observe(this) { resp->
            if (resp != null) {
                dataList.addAll(resp.data)
                plansAdapter.setDataList(dataList)
                plansAdapter.notifyDataSetChanged()
            }
        }

        viewModel.manageSubscriptionLiveData.observe(this){

            if(it?.status == true){
                binding.llDataFound.visibility = View.VISIBLE
                binding.llSubScription.visibility = View.GONE
                binding.llSubscriptionStatusPage.visibility = View.GONE
                binding.tvCurrentPlan.text = it.data?.title ?: ""
                binding.tvCurrentPlanFeatures.text = it.data?.features ?: ""
                binding.tvNoOfProfiles.text = it.data?.no_of_profile ?: ""
                binding.tvCurrentPlanPrice.text = it.data?.price ?: ""
                binding.tvAvableProfiles.text = it.data?.available_balance ?: ""
                binding.tvPlanStatud.text = it.data?.plan_status ?: ""
            }else{
                binding.llDataFound.visibility = View.GONE
                binding.llSubScription.visibility = View.VISIBLE
                binding.llSubscriptionStatusPage.visibility = View.GONE
                it?.message?.let { it1 -> ConstantUtils.showToast(applicationContext, it1) }
            }
        }

        binding.rcvSubscriptionPackages.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        plansAdapter = PlansAdapter(this, this)
        binding.rcvSubscriptionPackages.adapter = plansAdapter

        binding.btnGetStarted.setOnClickListener {
            /*if(!selectedPlanId.isNullOrBlank()){
                if (!ConstantUtils.isNetworkConnected(applicationContext)) {
                    ConstantUtils.alertDialog("Please Check Internet Connection", this)
                }else{
                    binding.llDataFound.visibility = View.GONE
                    binding.llSubScription.visibility = View.GONE
                    binding.llSubscriptionStatusPage.visibility = View.VISIBLE
                }
            }else{
                ConstantUtils.showToast(this, "Please Select Plan")
            }*/

            if(!selectedPlanId.isNullOrBlank()){
                if (!ConstantUtils.isNetworkConnected(applicationContext)) {
                    ConstantUtils.alertDialog("Please Check Internet Connection", this)
                }else{
                    viewModel.addPaymentLiveData(userID, selectedPlanId)
                    /* binding.llSubScription.visibility = View.GONE
                     binding.llSubscriptionStatusPage.visibility = View.VISIBLE*/
                }
            }else{
                ConstantUtils.showToast(this, "Please Select Plan")
            }
        }

        binding.btnContinue.setOnClickListener {
            /*sharedPreferenceManager = SharedPreferenceManager(this)
            userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()*/
            viewModel.manageSubscriptions(userID)
        }

        viewModel.addPaymentLiveData.observe(this){
            if(it?.status == true){


                if (it != null) {
                    /*sharedPreferenceManager.clearAllData()
                    sharedPreferenceManager.saveLoginResponse(it)*/
                }
                binding.llSubScription.visibility = View.GONE
                binding.llSubscriptionStatusPage.visibility = View.VISIBLE

                it?.message?.let { it1 -> ConstantUtils.showSuccessToast(applicationContext, it1) }
            }else{
                it?.message?.let { it1 -> ConstantUtils.showToast(applicationContext, it1) }
            }

        }

    }

    override fun clickOnCurrentPositionListener(position: Int) {
         dataList.forEachIndexed { index, data ->
            if(index == position){
                dataList[position].isSelected = true
            }else{
                dataList[position].isSelected = false
            }
        }
        selectedPlanId = dataList[position].id
        plansAdapter.notifyDataSetChanged()

    }
}