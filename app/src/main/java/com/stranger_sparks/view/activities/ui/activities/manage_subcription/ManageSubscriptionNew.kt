package com.stranger_sparks.view.activities.ui.activities.manage_subcription

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.SubscriptionPagerAdapter
import com.stranger_sparks.databinding.ActivityManageSubscriptionBinding
import com.stranger_sparks.databinding.ActivityManageSubscriptionNewBinding
import com.stranger_sparks.utils.ConstantUtils

class ManageSubscriptionNew : AppCompatActivity() {

    lateinit var binding: ActivityManageSubscriptionNewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageSubscriptionNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.black), false)

        val fragments = listOf(
            AudioSubscriptionFragment(),
            VideoSubscriptionFragment(),
            ProfileSubscriptionFragment()
        )

        val adapter = SubscriptionPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.txtAudio.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.blue_text))
                    binding.txtVideo.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
                    binding.txtProfile.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
                } else if (position == 1) {
                    binding.txtVideo.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.blue_text))
                    binding.txtAudio.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
                    binding.txtProfile.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
                }else if (position == 2) {
                    binding.txtProfile.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.blue_text))
                    binding.txtVideo.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
                    binding.txtAudio.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
                }
                super.onPageSelected(position)
            }
        })

        binding.txtAudio.setOnClickListener {
            binding.viewPager.currentItem = 0
            binding.txtAudio.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.blue_text))
            binding.txtVideo.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
            binding.txtProfile.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
        }
        binding.txtVideo.setOnClickListener {
            binding.viewPager.currentItem = 1
            binding.txtVideo.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.blue_text))
            binding.txtAudio.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
            binding.txtProfile.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
        }
        binding.txtProfile.setOnClickListener {
            binding.viewPager.currentItem = 2
            binding.txtProfile.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.blue_text))
            binding.txtVideo.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
            binding.txtAudio.setBackgroundColor(ContextCompat.getColor(this@ManageSubscriptionNew, R.color.white))
        }

        binding.ivClose.setOnClickListener {
            finish()
        }

    }


}