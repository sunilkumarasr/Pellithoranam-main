package com.stranger_sparks.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.databinding.ActivitySignInSignUpBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.viewmodel.SignInSignUpViewModel
import javax.inject.Inject

class SignInSignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignInSignUpBinding
    @Inject
    lateinit var viewModel: SignInSignUpViewModel
    var phoneNumber: String = ""
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivitySignInSignUpBinding.inflate(layoutInflater)

        viewModel.getAboutTermsPrivacy("6")
        setContentView(binding.root)
        ConstantUtils.createFullScreen(this)

        viewModel.inputSignal.observe(this) {
            if (it != null) {
                validateViewModelEvents(it,"")
            }
        }

        viewModel.getAboutTermsPrivacy("6")

        viewModel.getAboutTermsPrivacyLiveData.observe(this) {
            Log.v("Purushotham", it.toString())
        }

        binding.btnLogin.setOnClickListener {

            if (!ConstantUtils.isNetworkConnected(applicationContext)) {
                ConstantUtils.alertDialog("Please Check Internet Connection", this)
            } else if (checkValidation()){
                binding.progressLay.progressBar.visibility = View.VISIBLE
                phoneNumber = binding.etPhoneNumberSignin.text.toString().trim()
                if(phoneNumber.isNotEmpty() && phoneNumber.matches(emailPattern.toRegex())){

                }else{
                    ConstantUtils.alertDialog("Please enter valid email address", this)
                    return@setOnClickListener
                }
                /*if(phoneNumber.length<10)
                {
                    ConstantUtils.alertDialog("Please enter valid email address", this)
                    return@setOnClickListener
                }*/
                viewModel.getUserLogin(binding.etPhoneNumberSignin.text.toString().trim(),)
            }

        }
        viewModel.loginLiveData.observe(this){res->
            res?.message?.let { ConstantUtils.showToast(this, it) }
            binding.progressLay.progressBar.visibility = View.GONE
            if(res?.status == true){
                validateViewModelEvents(Constants.ObserverEvents.GOTO_OTP.toString(),res?.otp.toString())
            } else{
                //res?.message?.let { ConstantUtils.showToast(this, it) }
            }
        }


    }

    fun checkValidation(): Boolean {
        var ret = true
        if (!ConstantUtils.hasEditText(binding.etPhoneNumberSignin, "Please Enter Mobile Number ")) ret = false
        return ret
    }

    fun validateViewModelEvents(observerEvents: String?,otp: String) {
        if (observerEvents === Constants.ObserverEvents.GOTO_OTP.toString()) {
            Intent(applicationContext, OtpVerificationActivity::class.java).also {
                it.putExtra("phone_number", binding.etPhoneNumberSignin.getText().toString())
                it.putExtra("otp_", otp)
                startActivity(it)
                finish()
            }
        } else if (observerEvents === Constants.ObserverEvents.GOTO_SIGN_UP.toString()) {
           /* binding.llSignUp.visibility = View.VISIBLE
            binding.llLogin.visibility = View.GONE*/
        } else if (observerEvents === Constants.ObserverEvents.GOTO_SIGN_IN.toString()) {
            /*binding.llSignUp.visibility = View.GONE
            binding.llLogin.visibility = View.VISIBLE*/
        }
    }
}