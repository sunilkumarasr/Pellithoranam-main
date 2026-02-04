package com.stranger_sparks.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.databinding.ActivityOtpVerificationBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.viewmodel.OtpVerificationViewModel
import `in`.aabhasjindal.otptextview.OTPListener
import javax.inject.Inject

class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var phoneNumber: String
    lateinit var binding: ActivityOtpVerificationBinding
    lateinit var enteredOtp: String

    lateinit var otp: String
    var token : String? = null
    @Inject
    lateinit var viewModel: OtpVerificationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        ConstantUtils.createFullScreen(this)
        setContentView(binding.root)


        val intent = intent
        if (intent.hasExtra("phone_number")) {
            phoneNumber = intent.extras?.getString("phone_number").toString()
            binding.tvTileOfText.text = "${phoneNumber}"
        } else {
            phoneNumber = ""
        }

        otp= intent.getStringExtra("otp_").toString()
        binding.txtOTP.text = otp
        binding.txtOTP.visibility=View.VISIBLE


        binding.imgBack.setOnClickListener {
            startActivity(Intent(applicationContext,SignInSignUpActivity::class.java))
            finish()
        }
        enteredOtp = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
             token = task.result

            // Log and toast
            println("Token "+ token)
        })


        binding.otpView?.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }
            override fun onOTPComplete(otp: String) {
                enteredOtp = otp
            }
        }

        binding.btnVerifyOtp.setOnClickListener {
            if(enteredOtp.isNotBlank()){
                binding.progressLay.progressBar.visibility = View.VISIBLE
                // Log and toast
                token?.let { it1 -> viewModel.validateOtp(phoneNumber, enteredOtp, it1) }
            }else{
                ConstantUtils.showToast(this, "Please Enter OTP")
            }
        }
        viewModel.loginLiveData.observe(this) { resp ->
            binding.progressLay.progressBar.visibility = View.GONE

            if (resp?.status == true) {

                val sharedPreferenceManager = SharedPreferenceManager(this)
                sharedPreferenceManager.saveLoginResponse(resp)

                val isProfileCompleted =
                    sharedPreferenceManager.getSavedLoginResponseUser()
                        ?.data?.profile_completed == "1"

                val intent = if (isProfileCompleted) {
                    Intent(this, HomeActivity::class.java)
                } else {
                    Intent(this, BasicProfileActivity::class.java)
                }

                startActivity(intent)
                finish()

            } else {
                resp?.message?.let { ConstantUtils.showToast(this, it) }
            }
        }


    }

    fun validateViewModelEvents(observerEvents: Constants.ObserverEvents) {
        if (observerEvents === Constants.ObserverEvents.BACK_PRESS) {

            finish()
        } else if (observerEvents === Constants.ObserverEvents.GOTO_SCUBSCRIPTION) {
            Intent(applicationContext, Subcription::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }


}