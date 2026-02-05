package com.stranger_sparks.view.activities.ui.activities.help

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.databinding.ActivityHelpBinding
import com.stranger_sparks.databinding.ActivityNotifications2Binding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.SignInSignUpActivity
import com.stranger_sparks.view.activities.ui.activities.notifications.NotificationViewModel
import javax.inject.Inject

class HelpActivity : AppCompatActivity() {
    lateinit var binding: ActivityHelpBinding
    @Inject
    lateinit var viewModel: HelpActivityViewModel
    var name: String = ""
    var email: String = ""
    var phone: String = ""
    var subject: String = ""
    var message: String = ""
    val mobileNumberPattern = "^[6-9]\\d{9}$"
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.inputSignal.observe(this) {
            if (it != null) {
                validateViewModelEvents(it)
            }
        }

        binding.btnsubmit.setOnClickListener {
            if (!ConstantUtils.isNetworkConnected(applicationContext)) {
                ConstantUtils.alertDialog("Please Check Internet Connection", this)
            } else if (checkValidation()) {
                binding.progressLay.progressBar.visibility = View.VISIBLE
                name = binding.etFullName.text.toString().trim()
                email = binding.etEmail.text.toString().trim()
                phone = binding.etPhoneNumber.text.toString().trim()
                subject = binding.etSubject.text.toString().trim()
                message = binding.etComment.text.toString().trim()
                viewModel.contactUs(name, email, phone, subject, message)
            }
        }

        viewModel.helpLiveData.observe(this){resp->
            resp?.message?.let { ConstantUtils.showToast(this, it) }
            binding.progressLay.progressBar.visibility = View.GONE
            if(resp?.status == true){
                finish()

            }else{
                //res?.message?.let { ConstantUtils.showToast(this, it) }
            }

        }
        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.ivLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to logout?")
            builder.setTitle("Alert !")
            builder.setCancelable(false)
            builder.setPositiveButton(
                "Yes",
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    if(SharedPreferenceManager(this).clearAllData()){
                        Intent(applicationContext, SignInSignUpActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    }
                } as DialogInterface.OnClickListener)
            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                    // If user click no then dialog box is canceled.
                    dialog.cancel()
                } as DialogInterface.OnClickListener)
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun checkValidation(): Boolean {
        var ret = true
        if (!ConstantUtils.hasEditText(binding.etFullName, "Please Enter Full Name")) ret = false
        if (!ConstantUtils.hasEditText(binding.etEmail, "Please Email Id")) ret = false
        if (!ConstantUtils.hasEditText(binding.etPhoneNumber, "Please Enter Phone number")) ret = false
        if (!ConstantUtils.hasEditText(binding.etSubject, "Please Enter Subject")) ret = false
        if (!ConstantUtils.hasEditText(binding.etComment, "Please Enter Comment")) ret = false

        if (!isValidMobileNumber(binding.etPhoneNumber.text.toString())) {
            Toast.makeText(this, "Invalid  Mobile Number", Toast.LENGTH_SHORT).show()
            ret = false
        }


        if (!isValidEmail(binding.etEmail.text.toString())) {
            Toast.makeText(this, "Invalid  Email", Toast.LENGTH_SHORT).show()
            ret = false
        }
        return ret
    }

    fun isValidMobileNumber(mobileNumber: String): Boolean {
        val regex = Regex(mobileNumberPattern)
        return mobileNumber.matches(regex)
    }

    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && email.matches(emailPattern.toRegex())
    }

    private fun validateViewModelEvents(observerEvents: String) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()) {

            finish()
        }
    }
}