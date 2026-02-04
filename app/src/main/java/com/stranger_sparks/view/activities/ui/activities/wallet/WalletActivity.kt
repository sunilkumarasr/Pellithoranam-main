package com.stranger_sparks.view.activities.ui.activities.wallet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.razorpay.Checkout


import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.data_model.PaymentResponse
import com.stranger_sparks.databinding.ActivityWalletBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.RandomString
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.SignInSignUpActivity
import com.stranger_sparks.view.activities.ui.activities.wallet.transections.WalletTransactionsActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject


class WalletActivity : AppCompatActivity(), PaymentResultWithDataListener {
    lateinit var binding: ActivityWalletBinding
    @Inject
    lateinit var viewModel: WalletViewModel
    lateinit var userID: String
    lateinit var fullName: String
    lateinit var gender: String
    lateinit var hobbies: String
    lateinit var height: String
    lateinit var location: String
    lateinit var email: String
    lateinit var image: String
    lateinit var order_id: String
    lateinit var bank: String
    lateinit var payment_id: String
    lateinit var signature_id: String
    private lateinit var imageUri: Uri
    private lateinit var screenshotPart: MultipartBody.Part

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            handleImageResult(uri)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            handleImageResult(imageUri)
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.black), false)

        Checkout.preload(this)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()

        binding.uploadImg.setOnClickListener{
            openGallery()
        }
        binding.btnManual.setOnClickListener{

            if(checkManualValidation()){
                callManualPaymentApi()

            }


        }

        viewModel.getWalletAmount(userID)
        viewModel.getQrDetails()
        viewModel.getWalletData.observe(this){
            binding.tvWalletBalance.text = it?.data.toString()
        }

        viewModel.getBankDetails(userID)


        viewModel.bankDetails.observe(this){
            if(it!=null)
            {
                binding.etAccountNumber.setText("${it.accountNumber}")
                binding.etBranch.setText("${it.branchName}")
                binding.etIFSCode.setText("${it.ifscCode}")
                binding.etBank.setText("${it.bankName}")
                binding.etGPayNumber.setText("${it.googlepay}")
                binding.etPhonePayNumber.setText("${it.phonepe}")

            }
        }

        viewModel.walletWithdrawal.observe(this){

            if(it?.status == true){
                ConstantUtils.showToast(applicationContext, it.message)
                binding.llMyWallet.visibility = View.VISIBLE
                binding.llAddAmount.visibility = View.GONE
                binding.llWithDrawalAmount.visibility = View.GONE
                viewModel.getWalletAmount(userID)
            }else{
                it?.message?.let { it1 -> ConstantUtils.showToast(applicationContext, it1) }
            }
        }

        Glide.with(applicationContext).load(sharedPreferenceManager.getSavedLoginResponseUser()?.data?.image)
            .error(R.drawable.img_placeholder)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(binding.ivProfileImage)

        viewModel.inputSignal.observe(this) {
            if (it != null) {
                validateViewModelEvents(it)
            }
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
        binding.btnContinue.setOnClickListener {
            if(checkAmountValidation()){
                createOrderId()
                //viewModel.addWalletAmount(userID, binding.etAmount.text.trim().toString())
            }
        }
        binding.btnSubmit.setOnClickListener {
            if(checkWithdrawalValidation()){
                viewModel.walletWithdrawal(userID, binding.etWithdrawAmount.text.trim().toString(),
                    binding.etReason.text.trim().toString(),
                    binding.etBank.text.trim().toString(),
                    binding.etAccountNumber.text.trim().toString(),
                    binding.etConfirmAccountNumber.text.trim().toString(),
                    binding.etIFSCode.text.trim().toString(),
                    binding.etBranch.text.trim().toString(),
                    binding.etGPayNumber.text.trim().toString(),
                    binding.etPhonePayNumber.text.trim().toString(),
                )
            }
        }
        viewModel.createOrder.observe(this){
            if (it?.status == true) {
                it.data?.let { it1 ->
                    startPayment(it1)
                    //postonlineOrder()
                }
            }
            else{
            }
        }

        viewModel.paymentResult.observe(this){
            if (it?.status == true) {
                postOrder()
            }
            else{

            }
        }

        viewModel.walletWithdrawal.observe(this){
            if(it?.status == true){
                it?.message?.let { it1 -> ConstantUtils.showSuccessToast(this, it1) }
                binding.etWithdrawAmount.setText("")
                binding.etReason.setText("")
                binding.etAccountNumber.setText("")
                binding.etConfirmAccountNumber.setText("")
                binding.etBranch.setText("")
                binding.etIFSCode.setText("")
                binding?.llMyWallet?.visibility = View.VISIBLE
                binding?.llAddAmount?.visibility = View.GONE
                binding?.llWithDrawalAmount?.visibility = View.GONE
                viewModel.getWalletAmount(userID)

            }else{
                it?.message?.let { it1 -> ConstantUtils.showToast(this, it1) }
            }
        }
        viewModel.addWalletAmount.observe(this){
            if(it?.status == true){
                it?.message?.let { it1 -> ConstantUtils.showSuccessToast(this, it1) }
                binding?.llMyWallet?.visibility = View.VISIBLE
                binding?.llAddAmount?.visibility = View.GONE
                binding?.llWithDrawalAmount?.visibility = View.GONE
                binding?.etAmount?.setText("")
                binding?.uploadImg?.setText("")
                binding?.transactionId?.setText("")
                viewModel.getWalletAmount(userID)

            }else{
                it?.message?.let { it1 -> ConstantUtils.showToast(this, it1) }
            }
        }
        binding.ivClose.setOnClickListener {
            if(binding.llMyWallet.isVisible == true)
                finish()
            else if(binding?.llAddAmount?.isVisible == true || binding?.llWithDrawalAmount?.isVisible == true){
                binding?.llMyWallet?.visibility = View.VISIBLE
                binding?.llAddAmount?.visibility = View.GONE
                binding?.llWithDrawalAmount?.visibility = View.GONE
            }
        }
        binding.btnAddAmount.setOnClickListener {
            binding.llMyWallet.visibility = View.GONE
            binding.llAddAmount.visibility = View.VISIBLE
            binding.llWithDrawalAmount.visibility = View.GONE
        }
        binding.btnTransection.setOnClickListener {
            Intent(applicationContext, WalletTransactionsActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.tvWithdrawal.setOnClickListener {
            viewModel.getBankDetails(userID)
            binding?.llMyWallet?.visibility = View.GONE
            binding?.llAddAmount?.visibility = View.GONE
            binding?.llWithDrawalAmount?.visibility = View.VISIBLE
        }

    }



    private fun callManualPaymentApi() {
        val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), userID) // Replace with actual user ID
        val amount = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.etAmount.text.toString()) // Replace with actual amount
        val subscriptionId = RequestBody.create("text/plain".toMediaTypeOrNull(), " ") // Replace with actual subscription ID
        val transactionId = RequestBody.create("text/plain".toMediaTypeOrNull(), binding.transactionId.toString()) // Replace with actual transaction ID
        viewModel.manualPayment(userId, amount, subscriptionId, transactionId, screenshotPart)
    }
    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun handleImageResult(uri: Uri) {
        // Display the image
//        binding.setImageURI(uri)
//
        // Convert to MultipartBody.Part for API call
        val file = File(uriToFilePath(uri))
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        binding.uploadImg.setText(file.name.toString())
        screenshotPart = MultipartBody.Part.createFormData("screenshot", file.name, requestBody)
    }
    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Phone Number", text)
        clipboard.setPrimaryClip(clip)

        // Show a message to the user
        Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
    }
    private fun uriToFilePath(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
        val filePath = index?.let { cursor.getString(it) }
        cursor?.close()
        return filePath ?: ""
    }
    fun createOrderId() {
        viewModel.crateOrder(
            binding.etAmount.text.toString()
        )
    }
    fun postOrder() {
        viewModel.addWalletAmount( userID,binding.etAmount.text.toString())
        binding.etAmount.setText("")

    }
    fun postonlineOrder() {
        viewModel.addOnlineWalletAmount(order_id,payment_id,signature_id,bank,userID,binding.etAmount.text.toString())
        binding.etAmount.setText("")
    }
    fun razorpayCallback() {


        viewModel.checkRazorpayPayment(
            binding.etAmount.text.toString(),
            order_id,
            payment_id,
            signature_id

        )
    }

    private fun startPayment(orderId:String) {
        val co = Checkout()
        co.setKeyID("rzp_live_r6OGfrfmkAtp3g")
//        co.setKeyID("rzp_test_ZRO9SByaeBkTvh")
        try {
            val options = JSONObject()

            options.put("name", "Pellithoranam")
            options.put("description", "")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.drawable.img_app_logo_small)
            options.put("theme.color", R.color.colorPrimary);
            options.put("order_id", orderId)

            options.put("currency", "INR");
            val payment: String = binding.etAmount.text.toString()
//            val payment: String = "1"
            var total = payment.toDouble()
            total = total * 100
            options.put("amount", total)

            val preFill = JSONObject()
            preFill.put("email", "")
            preFill.put("contact", "")

            options.put("prefill", preFill)

            co.open(this, options)

        }catch (e: Exception){
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    fun checkAmountValidation(): Boolean {
        var ret = true
        if (!ConstantUtils.hasEditText(binding.etAmount, "Please Enter Amount")) ret = false
        return ret
    }
    fun checkWithdrawalValidation(): Boolean {
        var ret = true

        if (!ConstantUtils.hasEditText(binding.etWithdrawAmount, "Please Enter Withdraw Amount")) ret = false
        if (!ConstantUtils.hasEditText(binding.etAccountNumber, "Please Enter Account Number")) ret = false
        if (!ConstantUtils.hasEditText(binding.etBank, "Please Enter Bank Name")) ret = false
        if (!ConstantUtils.hasEditText(binding.etBranch, "Please Enter Branch Name")) ret = false
        if (!ConstantUtils.hasEditText(binding.etConfirmAccountNumber, "Please Enter Confirm Account Number")) ret = false
        if (!ConstantUtils.hasEditText(binding.etReason, "Please Enter Reason")) ret = false
        if (!ConstantUtils.hasEditText(binding.etIFSCode, "Please Enter IFSC code")) ret = false
        if (binding.etAccountNumber.text.toString() != binding.etConfirmAccountNumber.text.toString()
        ) {
            binding.etAccountNumber.setError("Please Enter Account Number and Conform Account number Same")
            binding.etConfirmAccountNumber.setError("Please Enter Account Number and Conform Account number Same")
            ret = false}

        if(binding.etPhonePayNumber.text.toString().length<=0&&binding.etGPayNumber.text.toString().length<=0)
        {
            Toast.makeText(this, "Please enter PhonePay/Gpay Number", Toast.LENGTH_SHORT).show()

            ret = false
        }
        return ret
    }
    fun checkManualValidation(): Boolean {
        var ret = true

        if (!ConstantUtils.hasEditText(binding.etAmount, "Please Enter Amount")) ret = false
        if (!ConstantUtils.hasEditText(binding.uploadImg, "Please Upload Screenshot")) ret = false
        if (!ConstantUtils.hasEditText(binding.transactionId, "Please Enter Transaction Id")) ret = false

        return ret
    }

    private fun validateViewModelEvents(observerEvents: String) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()) {

            finish()
        }
    }

    fun createOrderForWallet(){

    }
//    fun callPayment(amount:String)
//    {
//        val checkout=Checkout()
//        checkout.setMerchantIdentifier(getString(R.string.marchant_code)) //where T1234 is the MERCHANT CODE, update it with Merchant Code provided by TPSL
//        checkout.setTransactionIdentifier(RandomString.getAlphaNumericString()) //where TXN001 is the Merchant Transaction Identifier, it should be different for each transaction (alphanumeric value, no special character allowed)
//        checkout.setTransactionReference("currentDealID") //where ORD0001 is the Merchant Transaction Reference number
//        checkout.setTransactionType(PaymentActivity.TRANSACTION_TYPE_SALE) //Transaction Type
//        checkout.setTransactionSubType(PaymentActivity.TRANSACTION_SUBTYPE_DEBIT) //Transaction Subtype
//        checkout.setTransactionCurrency("INR") //Currency Type
//        checkout.setTransactionAmount(amount + "00") //Transaction Amount
//        checkout.setTransactionDateTime(SimpleDateFormat("dd-MM-yyyy").format(Date())) //Transaction Date
//
//        // setting Consumer fields values
//        checkout.setConsumerIdentifier("") //Consumer Identifier, default value "", set this value as application user name if you want Instrument Vaulting, SI on Cards. Consumer ID should be alpha-numeric value with no space
//        checkout.setConsumerAccountNo("") //Account Number, default value "". For eMandate, you can set this value here or can be set later in SDK.
//        checkout.addCartItem(
//            "FIRST",
//            amount,
//            "0.0",
//            "0.0",
//            "ABCD",
//            "Mobile",
//            "ANDROID",
//            "www.lookingforyou.com"
//        )
//        checkout.setTransactionAmount(amount) //Transaction amount
//        val authIntent = PaymentModesActivity.Factory.getAuthorizationIntent(
//            applicationContext, true
//        )
//
//
//        // Checkout Object
//        Log.d(
//            "Checkout Request Object",
//            checkout.merchantRequestPayload.toString()
//        )
//
//        authIntent.putExtra(Constant.ARGUMENT_DATA_CHECKOUT, checkout)
//
//
//        // Public Key
//        authIntent.putExtra(PaymentActivity.EXTRA_PUBLIC_KEY, "2159848778EWQLDB")
//
//
//        // Requested Payment Mode
//        authIntent.putExtra(
//            PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE,
//            PaymentActivity.PAYMENT_METHOD_DEFAULT
//        )
//
//        val settings = PaymentModesActivity.Settings()
//        authIntent.putExtra(Constant.ARGUMENT_DATA_SETTING, settings)
//
//        startActivityForResult(authIntent, PaymentActivity.REQUEST_CODE)
//    }



    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        order_id = p1?.orderId.toString()
        payment_id = p1?.paymentId.toString()
        signature_id = p1?.signature.toString()
        bank = try {
            val paymentDataJson = JSONObject(p1?.data.toString())
            paymentDataJson.optString("bank", "N/A") // Fetches bank name if available
        } catch (e: Exception) {
            "N/A"
        }

//        razorpayCallback()
        postonlineOrder()

        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()

    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {

    }
}