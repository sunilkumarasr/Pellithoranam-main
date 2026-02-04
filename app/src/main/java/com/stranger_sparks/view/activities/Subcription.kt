package com.stranger_sparks.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.PlansAdapter
import com.stranger_sparks.data_model.SubscriptionPlansDTO
import com.stranger_sparks.databinding.ActivitySubcriptionBinding
import com.stranger_sparks.inerfaces.OnItemClickListenerWithPosition
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.viewmodel.SubscriptionViewModel
import javax.inject.Inject

class Subcription : AppCompatActivity(), OnItemClickListenerWithPosition {
    lateinit var binding: ActivitySubcriptionBinding
    private var dataList: MutableList<SubscriptionPlansDTO.Data> = arrayListOf()
    lateinit var plansAdapter: PlansAdapter
    var selectedPlanId: String = ""
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    @Inject
    lateinit var viewModel: SubscriptionViewModel
    lateinit var userID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubcriptionBinding.inflate(layoutInflater)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        ConstantUtils.createFullScreen(this)


        viewModel.inputSignal.observe(this) {

            if (it != null) {
                validateViewModelEvents(it)

            }
        }
        viewModel.getSubscriptionList()

        viewModel.getSubscriptionLiveData.observe(this) { resp->
            if (resp != null) {
                dataList.addAll(resp.data)
                plansAdapter.setDataList(dataList)
                plansAdapter.notifyDataSetChanged()
            }
        }


        sharedPreferenceManager =  SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()


        binding.rcvSubscriptionPackages.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)
        plansAdapter = PlansAdapter(this, this)
        binding.rcvSubscriptionPackages.adapter = plansAdapter

        binding.btnGetStarted.setOnClickListener {
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
            Intent(applicationContext, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        viewModel.addPaymentLiveData.observe(this){
            if(it?.status == true){


                if (it != null) {
                    sharedPreferenceManager.clearAllData()
                    sharedPreferenceManager.saveLoginResponse(it)
                }
                binding.llSubScription.visibility = View.GONE
                binding.llSubscriptionStatusPage.visibility = View.VISIBLE
                it?.message?.let { it1 -> ConstantUtils.showToast(applicationContext, it1) }
            }else{
                it?.message?.let { it1 -> ConstantUtils.showToast(applicationContext, it1) }
            }

        }

        binding.tvSkipp.setOnClickListener {
            Intent(applicationContext, HomeActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }


    }
    fun validateViewModelEvents(observerEvents: String?) {
        if (observerEvents === Constants.ObserverEvents.GOTO_SUBSCRIPTION_SUCCESS.toString()) {

            binding.llSubScription.visibility = View.GONE
            binding.llSubscriptionStatusPage.visibility = View.VISIBLE
        } else if (observerEvents === Constants.ObserverEvents.GOTO_HOME_SCREEN.toString()) {
            Intent(applicationContext, HomeActivity::class.java).also {
                            startActivity(it)
                            finish()
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