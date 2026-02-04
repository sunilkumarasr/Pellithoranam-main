package com.stranger_sparks.view.activities.ui.activities.webview


import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.stranger_sparks.BuildConfig
import com.stranger_sparks.R

import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.databinding.ActivityWebViewUrlLoadBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import javax.inject.Inject


class WebViewUrlLoad : AppCompatActivity() {
    lateinit var binding: ActivityWebViewUrlLoadBinding
    @Inject
    lateinit var viewModel: WebViewUrlViewModel
    var urlType: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityWebViewUrlLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(
            this,
            ContextCompat.getColor(this, R.color.notification_bar_color_two),
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val intent = intent

        if (intent.hasExtra("url_type")) {
            urlType = intent.extras?.getString("url_type").toString()

        } else {
            urlType = ""
        }
        //viewModel.getAboutTermsPrivacy("6")

        viewModel.inputSignal.observe(this) {

            if (it != null) {
                validateViewModelEvents(it)

            }
        }
        binding.wvCommon?.webViewClient = WebViewClient()

        /*viewModel.getAboutTermsPrivacyLiveData.observe(this) {
            binding.tvPageTitle.text = it?.data?.page_title

            Glide.with(this)
                .asBitmap()
                .load(it?.data?.page_image)
                .centerCrop()
                .into(binding.ivHeader);
            it?.data?.information_title?.let { it1 -> binding.wvCommon?.loadData(it1, "text/html", "UTF-8") }
        }*/


        if(urlType == Constants.ObserverEvents.PRIVACY_POLICY.toString()){
            binding.wvCommon?.loadUrl(BuildConfig.PRIVACY_POLCY)
        }else if(urlType == Constants.ObserverEvents.TERMS_AND_CONDITIONS.toString()){
            binding.wvCommon?.loadUrl(BuildConfig.TERMS_CONDITIONS)
        }else if(urlType == Constants.ObserverEvents.ABOUT_US.toString()){
            binding.wvCommon?.loadUrl(BuildConfig.ABOUT_US)
        }else if(urlType == Constants.ObserverEvents.FAQ.toString()){
            binding.wvCommon?.loadUrl(BuildConfig.FAQ)
        }


        binding.wvCommon?.settings?.javaScriptEnabled = true
        binding.wvCommon?.settings?.setSupportZoom(true)


    }
    override fun onBackPressed() {
        if (binding.wvCommon?.canGoBack()!!)
            binding.wvCommon?.goBack()
        else
            super.onBackPressed()
    }

    private fun validateViewModelEvents(observerEvents: String?) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()) {

            finish()
        }
    }
}