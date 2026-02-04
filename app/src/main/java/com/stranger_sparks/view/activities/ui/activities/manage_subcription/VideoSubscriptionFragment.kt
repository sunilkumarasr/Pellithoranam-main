package com.stranger_sparks.view.activities.ui.activities.manage_subcription

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.PlansAdapter
import com.stranger_sparks.data_model.SubscriptionPlansDTO
import com.stranger_sparks.databinding.FragmentAudioSubscriptionBinding
import com.stranger_sparks.databinding.FragmentVideoSubscriptionBinding
import com.stranger_sparks.inerfaces.OnItemClickListenerWithPosition
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.HomeActivity
import javax.inject.Inject


class VideoSubscriptionFragment : Fragment(), OnItemClickListenerWithPosition {


    lateinit var binding: FragmentVideoSubscriptionBinding

    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    @Inject
    lateinit var viewModel: ManageSubscriptionViewModel
    lateinit var userID: String
    private var dataList: MutableList<SubscriptionPlansDTO.Data> = arrayListOf()
    lateinit var plansAdapter: PlansAdapter
    var selectedPlanId: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoSubscriptionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as StrangerSparksApplication).applicationComponent.inject(this)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        sharedPreferenceManager = SharedPreferenceManager(requireActivity())
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()

        //plan details
        viewModel.manageSubscriptionsV2(userID,"1")
        viewModel.manageSubscriptionV2LiveData.observe(requireActivity()){

            if(it?.status == true){
                binding.llDataFound.visibility = View.VISIBLE
                binding.llSubScription.visibility = View.GONE
                binding.llSubscriptionStatusPage.visibility = View.GONE
                binding.tvCurrentPlan.text = it.data?.title ?: ""
                binding.tvCurrentPlanFeatures.text = it.data?.features ?: ""
                binding.tvNoOfProfiles.text = it.data?.consumed_duration +" minutes"
                binding.tvCurrentPlanPrice.text = "Rs:"+ (it.data?.price ?: "")
                binding.tvAvableProfiles.text = it.data?.available_balance +" minutes"
                binding.tvPlanStatud.text = it.data?.plan_status
            }else{
                binding.llDataFound.visibility = View.GONE
                binding.llSubScription.visibility = View.VISIBLE
                binding.llSubscriptionStatusPage.visibility = View.GONE
                //it?.message?.let { it1 -> ConstantUtils.showToast(requireActivity(), it1) }
            }

        }

        //sub List
        viewModel.getSubscriptionV2List("1")
        viewModel.getSubscriptionV2LiveData.observe(requireActivity()) { resp->
            if (resp != null) {
                if (resp.status){
                    dataList.addAll(resp.data)
                    plansAdapter.setDataList(dataList)
                    plansAdapter.notifyDataSetChanged()
                }
            }
        }

//        binding.rcvSubscriptionPackages.layoutManager = LinearLayoutManager(requireActivity(),  LinearLayoutManager.HORIZONTAL, false)
//        plansAdapter = PlansAdapter(requireActivity(), this)
//        binding.rcvSubscriptionPackages.adapter = plansAdapter

        binding.rcvSubscriptionPackages.layoutManager = GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false)
        plansAdapter = PlansAdapter(requireActivity(), this)
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
                if (!ConstantUtils.isNetworkConnected(requireActivity())) {
                    ConstantUtils.alertDialog("Please Check Internet Connection", requireActivity())
                }else{
                    viewModel.addPaymentLiveData(userID, selectedPlanId)
                    /* binding.llSubScription.visibility = View.GONE
                     binding.llSubscriptionStatusPage.visibility = View.VISIBLE*/
                }
            }else{
                ConstantUtils.showToast(requireActivity(), "Please Select Plan")
            }
        }

        binding.btnContinue.setOnClickListener {
            Intent(activity, HomeActivity::class.java).also {
                startActivity(it)}
        }

        viewModel.addPaymentLiveData.observe(requireActivity()){
            if(it?.status == true){
                if (it != null) {
                    /*sharedPreferenceManager.clearAllData()
                    sharedPreferenceManager.saveLoginResponse(it)*/
                }
                binding.llSubScription.visibility = View.GONE
                binding.llSubscriptionStatusPage.visibility = View.VISIBLE

                it?.message?.let { it1 -> ConstantUtils.showSuccessToast(requireActivity(), it1) }
            }else{
                it?.message?.let { it1 -> ConstantUtils.showToast(requireActivity(), it1) }
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
        binding.txtSubPlanDetails.text = dataList[position].features
        plansAdapter.notifyDataSetChanged()

    }

}