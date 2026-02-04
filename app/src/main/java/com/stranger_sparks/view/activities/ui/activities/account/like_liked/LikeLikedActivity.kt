package com.stranger_sparks.view.activities.ui.activities.account.like_liked

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.LikeLikedAdapter
import com.stranger_sparks.data_model.WalletTransectionResponse
import com.stranger_sparks.databinding.ActivityLikeLikedBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.SharedPreferenceManager
import javax.inject.Inject

class LikeLikedActivity : AppCompatActivity() {
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    lateinit var binding: ActivityLikeLikedBinding
    @Inject
    lateinit var viewModel: LikeLikedViewModel
    private lateinit var  likeLikedAdapter: LikeLikedAdapter
    private var dataList = mutableListOf<WalletTransectionResponse.Data>()
    lateinit var userID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityLikeLikedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.black), false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.rbAll.isChecked = true
        sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()
        viewModel.likeLikedLiveData(userID, "1")
        binding.rcvLikeLiked.layoutManager = LinearLayoutManager(this)
        likeLikedAdapter = LikeLikedAdapter(this)
        binding.rcvLikeLiked.adapter = likeLikedAdapter
        viewModel.likeLikedLiveData.observe(this){
            if(it?.status == true) {
                binding.tvNoRecordsDefault.visibility = View.GONE
                binding.rcvLikeLiked.visibility = View.VISIBLE
                it?.data?.let { it1 -> likeLikedAdapter.setDataList(it1) }
                likeLikedAdapter.notifyDataSetChanged()
                Log.e("asdfghj_",it?.data?.size.toString())
            }else{
                binding.tvNoRecordsDefault.visibility = View.VISIBLE
                binding.rcvLikeLiked.visibility = View.GONE
            }
        }

        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.rgType.setOnCheckedChangeListener { radioGroup, checkedId ->
            if(checkedId == R.id.rbAll){
                viewModel.likeLikedLiveData(userID, "1")
                likeLikedAdapter.setType(1)
            }else if(checkedId == R.id.rbLike){
                viewModel.likeLikedLiveData(userID, "2")
                likeLikedAdapter.setType(2)
            }else if(checkedId == R.id.rbLiked){
                viewModel.likeLikedLiveData(userID, "3")
                likeLikedAdapter.setType(3)
            }else if(checkedId == R.id.rbViews){
                likeLikedAdapter.setType(4)
                viewModel.likeLikedLiveData(userID, "4")
            }
        }
    }
}