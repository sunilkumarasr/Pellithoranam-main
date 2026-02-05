package com.stranger_sparks.view.activities.ui.activities.display_user

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.CheckedTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.stranger_sparks.BuildConfig
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.ProfileGalaryImagesAdapter
import com.stranger_sparks.api_dragger_flow.di.ApplicationModule
import com.stranger_sparks.data_model.GalleryImagesResponse
import com.stranger_sparks.databinding.ActivityDisplayUserBinding
import com.stranger_sparks.fcm.ZegoCallManager
import com.stranger_sparks.inerfaces.OnItemClickListenerProfilesGalleryImages
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.ui.activities.account.gallery.GalleryImageZoomActivity
import com.stranger_sparks.view.activities.ui.activities.chat.chat_room.ChatRoom
import com.stranger_sparks.view.activities.ui.activities.manage_subcription.ManageSubscriptionViewModel
import com.stranger_sparks.viewmodel.SharedProfileViewModel
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.internal.IncomingCallButtonListener
import com.zegocloud.uikit.prebuilt.call.invite.internal.OutgoingCallButtonListener
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallType
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallUser
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoInvitationCallListener
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import org.json.JSONObject
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


class DisplayUserActivity : AppCompatActivity(), OnItemClickListenerProfilesGalleryImages {

    lateinit var binding: ActivityDisplayUserBinding

    @Inject
    lateinit var viewModel: DisplayUserViewModel
    lateinit var userID: String
    lateinit var userName: String
    lateinit var profile_id: String
    lateinit var profileGalaryImagesAdapter: ProfileGalaryImagesAdapter
    private var dataList: MutableList<GalleryImagesResponse.Data> = arrayListOf()


    //chat
    var chatSubStatus: Boolean = false
    var is_chart: Boolean = false
    var imgUrl: String = ""
    var token: String = ""

    var Email: String = ""
    var Mobile: String = ""
    var subscriptionAudio: Boolean = false
    var subscriptionAudioTime: String = ""
    var subscriptionVideo: Boolean = false
    var subscriptionVideoTime: String = ""
    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )

    lateinit var profileName: String


    //call time
    var exitVideoTime: String = "00:00:00"
    var exitAudioTime: String = "00:00:00"
    var duration: String = ""
    private var handler = android.os.Handler(Looper.getMainLooper())
    private var startTime = 0L
    private var elapsedTime = 0L
    private var running = false

    //call end api run
    private var isApiCalled = false
    var calltype: String = ""
    private var timer: Timer? = null

    var isCallHandled = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityDisplayUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //permisstion
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }

        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(
            this,
            ContextCompat.getColor(this, R.color.app_red),
            false
        )
        profile_id = intent.extras?.getString("PROFILE_ID").toString()
        val sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()
        userName = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.name.toString()


        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.rcvProfileGalleryImages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        profileGalaryImagesAdapter = ProfileGalaryImagesAdapter(this, this)
        binding.rcvProfileGalleryImages.adapter = profileGalaryImagesAdapter

        /*if(userData != null){
            binding.tvName.text = userData!!.name
            binding.tvEmail.text = userData!!.email
            binding.tvHeight.text = userData!!.height
            binding.tvMobile.text = userData!!.phone
            binding.tvLocation.text = userData!!.location

           *//* Glide.with(applicationContext).load(userData!!.image)
                .error(R.drawable.img_placeholder)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(binding.ivProfileImage)*//*
            viewModel.getUserProfileLiveData(userData!!.id)
        }*/

//        viewModel.viewProfileLiveData(userID, profile_id)

        // binding.cbLiked.isChecked = true
        viewModel.getUserProfileLiveData(userID, profile_id)
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Loading......")
        mProgressDialog.show()
        Log.e("userID_", userID)
        Log.e("profile_id_", profile_id)
        viewModel.getUserProfileLiveData.observe(this) { resp ->
            mProgressDialog.dismiss()
            if (resp != null) {
                binding.tvName.text = resp.data.name
                profileName = resp.data.name


                //call
                subscriptionAudio = resp.data.subscription_audio
                subscriptionAudioTime = resp.data.subscription_audio_time
                subscriptionVideo = resp.data.subscription_video
                subscriptionVideoTime = resp.data.subscription_video_time

                Email = resp.data.email
                Mobile = resp.data.phone

                if (resp.profile_view_status) {
                    chatSubStatus = true;
                    binding.btnView.visibility = View.GONE
                    binding.tvEmail.text = Email
                    binding.tvMobile.text = Mobile
                    binding.tvTitle.text = "Hobbies: "+resp.data.hobbies
                    binding.tvCast.text = "Caste : ${checkNull(resp.data.caste)}"
                    binding.tvReligion.text = "Religion : ${checkNull(resp.data.religion)}"
                    binding.tvCompany.text = "Company : ${checkNull(resp.data.company)}"
                    binding.tvJob.text = "Profession : ${checkNull(resp.data.job)}"
                    binding.tvSalary.text = "Salary ₹ " + checkNull(resp.data.salary)
                } else {
                    chatSubStatus = false;
                    binding.btnView.visibility = View.VISIBLE
                    val maskedPhoneNumber = maskPhoneNumber(resp.data.phone)
                    binding.tvMobile.text = maskedPhoneNumber
                    binding.tvCompany.text = "Company ${maskText(resp.data.company.toString())}"
                    binding.tvJob.text = "Profession ${maskText(resp.data.job.toString())}"
                    binding.tvSalary.text = "Salary ₹ " + maskText(resp.data.salary.toString())
                    binding.tvCast.text = "Caste : ${maskText(resp.data.caste)}"
                    binding.tvReligion.text = "Religion : ${maskText(resp.data.religion)}"
                }

                binding.tvHeight.text = resp.data.height
                binding.tvLocation.text = resp.data.location
                binding.tvAge.text = "Age: " + resp.data.age + " Years"
                binding.tvDescription.text = "Description: "+resp.data.description
                is_chart = resp.data.is_chart
                binding.cbLiked.isChecked = true
                if (resp.data.liked_status) {
                    binding.cbLiked.isChecked = true
                } else {
                    binding.cbLiked.isChecked = false
                }
                binding.tvLikedCount.text = resp.data.liked_count.toString()
                imgUrl = resp.data.profile_pic
                Glide.with(applicationContext).load(imgUrl)
                    .error(R.drawable.img_placeholder)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .into(binding.ivProfileImage)
            }
        }
        viewModel.viewProfileLiveData.observe(this) {
            if (it?.status == true) {
                Log.v("Purushotham", "View Profile Status true")
            } else {
                Log.v("Purushotham", "View Profile Status false")
            }
        }

//        //Audio plan details
//        viewModel.manageSubscriptionsV2(userID,"2")
//        viewModel.manageSubscriptionV2LiveData.observe(this){
//            if(it?.status == true){
//                subscriptionAudio = true
//                subscriptionAudioTime = it.data.available_balance
//                binding.tvAudioCallration.visibility = View.VISIBLE
//                binding.tvAudioCallration.text = "Available Audio Call Duration: "+it.data.available_balance
//            }
//        }

//        //Video plan details
//        viewModel.manageSubscriptionsV2(userID,"1")
//        viewModel.manageSubscriptionV2LiveData.observe(this){
//            if(it?.status == true){
//                subscriptionVideo = true
//                subscriptionVideoTime = it.data.available_balance
//                binding.tvVideoCallration.visibility = View.VISIBLE
//                binding.tvVideoCallration.text =  "Available Video Call Duration: "+it.data.available_balance
//            }
//        }

        binding.btnView.setOnClickListener {
            viewModel.profileViewDetails(userID, profile_id)
        }
        viewModel.profileViewDetailsLiveData.observe(this) {
            if (it?.status == true) {
                binding.btnView.visibility = View.GONE
                binding.tvEmail.text = Email
                binding.tvMobile.text = Mobile
            } else {
                ConstantUtils.showToast(applicationContext, "No Subscription")
            }
        }

        binding.cbLiked.setOnClickListener {
            if (!binding.cbLiked.isChecked) {
                (it as CheckedTextView).toggle()
                viewModel.likedProfile(userID, profile_id)
                viewModel.likeHitChangeLiveData.observe(this) {
                    if (it?.status == true) {
                        it?.message?.let { it1 -> ConstantUtils.showSuccessToast(this, it1) }
                        viewModel.getUserProfileLiveData(userID, profile_id)
                    } else {
                        binding.cbLiked.isChecked = false
                        it?.message?.let { it1 -> ConstantUtils.showSuccessToast(this, it1) }
                    }
                }
            } else {
                ConstantUtils.showToast(
                    applicationContext,
                    "You already liked " + binding.tvName.text
                )
            }
        }

        //chat
        binding.ivMessage.setOnClickListener {

            Intent(applicationContext, ChatRoom::class.java).also {
                it.putExtra("PROFILE_ID", profile_id)
                it.putExtra("is_chart", is_chart)
                startActivity(it)
            }

//            if (chatSubStatus){
//                if (is_chart) {
//                    Intent(applicationContext, ChatRoom::class.java).also {
//                        it.putExtra("PROFILE_ID", profile_id)
//                        it.putExtra("is_chart", is_chart)
//                        startActivity(it)
//                    }
//                } else {
//                    ConstantUtils.showToast(applicationContext, "No Subscription")
//                }
//            }else{
//
//                val dialog = AlertDialog.Builder(this)
//                    .setTitle("Confirmation")
//                    .setMessage("Are you sure you want to chat this person?")
//                    .setPositiveButton("Yes") { dialogInterface, _ ->
//                        viewModel.profileViewDetails(userID, profile_id)
//                        viewModel.profileViewDetailsLiveData.observe(this) { response ->
//                            if (response?.status == true) {
//                                if (is_chart) {
//                                    Intent(applicationContext, ChatRoom::class.java).also {
//                                        it.putExtra("PROFILE_ID", profile_id)
//                                        startActivity(it)
//                                    }
//                                } else {
//                                    ConstantUtils.showToast(applicationContext, "No Subscription")
//                                }
//                            } else {
//                                ConstantUtils.showToast(applicationContext, "No Subscription")
//                            }
//                        }
//                        dialogInterface.dismiss()
//                    }
//                    .setNegativeButton("No") { dialogInterface, _ ->
//                        dialogInterface.dismiss()
//                    }
//                    .create()
//
//                dialog.show()
//
//                // Change button colors after showing the dialog
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.blue_inner_text))
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.blue_inner_text))
//
//            }


        }

        //call start
        binding.ivAudioCall.setOnClickListener {

            if (subscriptionAudio) {
                calltype = "Audio"
                if (checkSelfPermission()) {
                    viewModel.userstartcall(userID, profile_id, "Audio")
                } else {
                    ConstantUtils.showToast(applicationContext, "Permissions was not granted")
                }
            } else {
                ConstantUtils.showToast(applicationContext, "No Subscription")
            }
        }

        binding.ivVideoCall.setOnClickListener {
            if (subscriptionVideo) {
                calltype = "Video"
                if (checkSelfPermission()) {
                    viewModel.userstartcall(userID, profile_id, "Video")
                } else {
                    ConstantUtils.showToast(applicationContext, "Permissions was not granted")
                }
            } else {
                ConstantUtils.showToast(applicationContext, "No Subscription")
            }
        }

        viewModel.userstartcallData.observe(this) {
            if (it?.status == true) {
                if (calltype.equals("Audio")) {
                    initVoiceButton()
                    val newVoiceCall: ZegoSendCallInvitationButton =
                        findViewById(R.id.new_voice_call)
                    newVoiceCall.performClick()
                } else if (calltype.equals("Video")) {
                    initVideoButton()
                    val new_video_call: ZegoSendCallInvitationButton =
                        findViewById(R.id.new_video_call)
                    new_video_call.performClick()
                }
            } else {
                ConstantUtils.showToast(applicationContext, "Connection Failed Please Try again")
            }
        }

        viewModel.savedGalleryImages(profile_id)
        viewModel.savedGalleryImages.observe(this) {
            if (it?.status == true) {
                // binding.tvNoRecordsDefault.visibility = View.GONE
                dataList.clear()
                dataList.addAll(it.data)
                binding.rcvProfileGalleryImages.visibility = View.VISIBLE
                it?.data?.let { it1 -> profileGalaryImagesAdapter.setDataList(it1) }
                profileGalaryImagesAdapter.notifyDataSetChanged()
            } else {
                // binding.tvNoRecordsDefault.visibility = View.VISIBLE
                binding.rcvProfileGalleryImages.visibility = View.GONE
                binding.noDataTv.visibility = View.VISIBLE
            }
        }

        binding.ivProfileImage.setOnClickListener {
            Intent(applicationContext, GalleryImageZoomActivity::class.java).also {
                it.putExtra("IMAGE_URL", imgUrl)
                startActivity(it)
            }
        }


        //default call outgoing cut api run
        Thread {
            try {
                val call = ApplicationModule.getApiService().submitCallCheckStatus(userID, "0")
                val response = call.execute()
                if (response.isSuccessful) {
                    Log.d("API", "Call cancelled successfully: ${response.body()}")
                } else {
                    Log.e("API", "API error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API", "API call failed: ${e.message}")
            }
        }.start()


        //incoming call received
        ZegoCallManager.initialize(application, userID, userName)
        //outGoingCall
        outGoingCallMethod()

    }

    //call
    private fun outGoingCallMethod() {

        //collar side call cut without joining call
        ZegoUIKitPrebuiltCallService.events.invitationEvents.setOutgoingCallButtonListener(object :
            OutgoingCallButtonListener {
            override fun onOutgoingCallCancelButtonPressed() {
                Log.e("asdfgh","63")
                exitApiRun("0")
                //call cut api
//                viewModel.submitUserCallHistory(userID,"OutGoing Call", profile_id, profileName,"", calltype)
//                viewModel.submitProfileCallHistory(profile_id,"Missed Call", userID, userName,"", calltype)
            }
        })
        //after join the Call end listener
        ZegoUIKitPrebuiltCallService.events.callEvents.setCallEndListener { callEndReason, jsonObject ->
            //exitApiRun("1")
            Log.e("asdfgh","77")

            if (isApiCalled){
                Log.e("exitApiRun_", "1")
                val mProgressDialog = ProgressDialog(this)
                mProgressDialog.setMessage("Loading......")
                mProgressDialog.show()

                isApiCalled = false
                if (calltype.equals("Audio")){
                    // Toast.makeText(applicationContext, exitAudioTime, Toast.LENGTH_LONG).show()
                    viewModel.userAudioCall(userID, profile_id, exitAudioTime)
                    viewModel.userAudioCallLiveData.observe(this) { res ->
                        res?.message?.let {

                        }
                        mProgressDialog.dismiss()
//            if (res?.status == true) {
//                finish()
//            } else {
//                finish()
//            }
                    }
                }else{
                    // Toast.makeText(applicationContext, exitVideoTime, Toast.LENGTH_LONG).show()
                    viewModel.userVideoCall(userID, profile_id,exitVideoTime)
                    viewModel.userVideoCallLiveData.observe(this){res->
                        res?.message?.let {
                        }
                        mProgressDialog.dismiss()
//                if(res?.status == true){
//                    finish()
//                } else{
//                    finish()
//                }
                    }
                }
            }

//            var decoded = ""
//            if (calltype.equals("Audio")){
//                decoded = URLDecoder.decode(exitAudioTime, "UTF-8")
//            }
//            if (calltype.equals("Video")){
//                decoded = URLDecoder.decode(exitVideoTime, "UTF-8")
//            }
//
//            viewModel.submitUserCallHistory(userID,"OutGoing Call", profile_id, profileName, decoded, calltype)
//            viewModel.submitProfileCallHistory(profile_id,"Incoming Call", userID, userName, decoded, calltype)
        }

        ZegoUIKitPrebuiltCallService.events.invitationEvents.setIncomingCallButtonListener(object :
            IncomingCallButtonListener {
            override fun onIncomingCallDeclineButtonPressed() {
                exitApiRun("0")
                Log.e("asdfgh","89")
            }
            override fun onIncomingCallAcceptButtonPressed() {
                Log.e("asdfgh","88")
            }
        })

        ZegoUIKitPrebuiltCallService.events.invitationEvents.setInvitationListener(object :
            ZegoInvitationCallListener {
            override fun onIncomingCallReceived(
                callID: String?,
                caller: ZegoCallUser?,
                callType: ZegoCallType?,
                callees: MutableList<ZegoCallUser>?
            ) {
                //call ring time
                ringTime()
            }
            override fun onIncomingCallCanceled(callID: String?, caller: ZegoCallUser?) {
                Log.e("asdfgh","4")
                exitApiRun("0")
            }
            override fun onIncomingCallTimeout(callID: String?, caller: ZegoCallUser?) {
                Log.e("asdfgh","5")
                exitApiRun("0")
            }
            override fun onOutgoingCallAccepted(callID: String, callee: ZegoCallUser) {
                Log.e("asdfgh","6")
                isCallHandled = true
                callTime()
                isApiCalled = true
                exitApiRun("1")
            }
            override fun onOutgoingCallRejectedCauseBusy(callID: String?, callee: ZegoCallUser?) {
                Log.e("asdfgh","7")
                exitApiRun("0")
            }
            override fun onOutgoingCallDeclined(callID: String?, callee: ZegoCallUser?) {
                Log.e("asdfgh","8")
                exitApiRun("0")
            }
            override fun onOutgoingCallTimeout(
                callID: String?,
                callees: MutableList<ZegoCallUser>?
            ) {
                Log.e("asdfgh","9")
                exitApiRun("0")
            }
        })
    }

    private fun initVoiceButton() {
        Log.e("profile_id_",profile_id)
        val newVoiceCall: ZegoSendCallInvitationButton = findViewById(R.id.new_voice_call)
        if (profile_id.isNotEmpty()) {
            val userIdList = profile_id.split(",")
            val invitees = mutableListOf<ZegoUIKitUser>()

            for (userID in userIdList) {
                val userName = "${userID.trim()}_name"
                invitees.add(ZegoUIKitUser(userID.trim(), userName))
            }

            newVoiceCall.setIsVideoCall(false)
            newVoiceCall.setResourceID("zego_uikit_call") // Must match your ZEGOCLOUD Console
            newVoiceCall.setInvitees(invitees)

            callCancelOutSidePerson()

        } else {
            ConstantUtils.showToast(applicationContext, "User Not Available")
        }
    }

    private fun initVideoButton() {
        val newVideoCall: ZegoSendCallInvitationButton = findViewById(R.id.new_video_call)
        if (profile_id.isNotEmpty()) {
            val userIdList = profile_id.split(",")
            val invitees = mutableListOf<ZegoUIKitUser>()

            for (userID in userIdList) {
                val userName = "${userID.trim()}_name"
                invitees.add(ZegoUIKitUser(userID.trim(), userName))
            }

            applicationContext.wakeUpScreenAndKeepOn()

            newVideoCall.setIsVideoCall(true)
            newVideoCall.setResourceID("zego_uikit_call") // Must match your ZEGOCLOUD Console
            newVideoCall.setInvitees(invitees)

            callCancelOutSidePerson()
        } else {
            ConstantUtils.showToast(applicationContext, "User Not Available")
        }
    }

    //call time
    private fun callTime() {
        if (!running) {
            startTime = System.currentTimeMillis() - elapsedTime
            handler.post(updateTimeTask)
            running = true
        }
    }
    private val updateTimeTask = object : Runnable {
        override fun run() {
            if (running) {
                elapsedTime = System.currentTimeMillis() - startTime
                val seconds = (elapsedTime / 1000).toInt() % 60
                val minutes = (elapsedTime / (1000 * 60) % 60).toInt()
                val hours = (elapsedTime / (1000 * 60 * 60)).toInt()
                handler.postDelayed(this, 1000)
                if (calltype.equals("Audio")){
                    exitAudioTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    duration = exitAudioTime
                    if (exitAudioTime.equals(subscriptionAudioTime)){
                        ConstantUtils.showToast(applicationContext, "Sorry! Your Subscription Expired")
                        ZegoUIKitPrebuiltCallService.endCall()
                    }
                }else if (calltype.equals("Video")){
                    exitVideoTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    duration = exitVideoTime
                    if (exitVideoTime.equals(subscriptionVideoTime)){
                        ConstantUtils.showToast(applicationContext, "Sorry! Your Subscription Expired")
                        ZegoUIKitPrebuiltCallService.endCall()
                    }
                }
            }
        }
    }
    //exit call api
    private fun exitApiRun(status_: String) {

        if (status_.equals("0")) {
            viewModel.submitUserCallHistory(
                userID,
                "OutGoing Call",
                profile_id,
                profileName,
                "",
                calltype
            )
            viewModel.submitProfileCallHistory(
                profile_id,
                "Missed Call",
                userID,
                userName,
                "",
                calltype
            )
        } else if (status_.equals("1")) {
            viewModel.submitUserCallHistory(
                userID,
                "OutGoing Call",
                profile_id,
                profileName,
                "",
                calltype
            )
            viewModel.submitProfileCallHistory(
                profile_id,
                "Incoming Call",
                userID,
                userName,
                "",
                calltype
            )
        }

    }

    fun maskPhoneNumber(phoneNumber: String): String {
        if (phoneNumber.length < 4) {
            throw IllegalArgumentException("Phone number must have at least 4 digits")
        }
        val lastFourDigits = phoneNumber.takeLast(4)
        val maskedSection = "*".repeat(phoneNumber.length - 4)
        return maskedSection + lastFourDigits
    }

    fun maskText(str: String?): String {
        if (str == null || str.length == 0)
            return "***"
        /* if (str.length < 3) {
             return "***"
         }*/
        //val lastFourDigits = str.takeLast(str.length-1)
        val maskedSection = "*".repeat(str.length - 1)
        return maskedSection
    }

    fun checkNull(str: Any?): String {
        if (str == null || str == "null" || str.equals("null")) {
            return ""
        }

        return str.toString()
    }

    override fun clickOnCurrentPositionListener(item: GalleryImagesResponse.Data, position: Int) {
        Intent(applicationContext, GalleryImageZoomActivity::class.java).also {
            it.putExtra("IMAGE_URL", BuildConfig.API_URL + "" + dataList[position].image)
            startActivity(it)
        }
    }

    private fun checkSelfPermission(): Boolean {
        return !(ContextCompat.checkSelfPermission(
            this,
            REQUESTED_PERMISSIONS[0]
        ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    REQUESTED_PERMISSIONS[1]
                ) != PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun ringTime() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isCallHandled) {
                isCallHandled = false
                ZegoUIKitPrebuiltCallService.endCall()
            }
        }, 10_000)
    }

    private fun callCancelOutSidePerson() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                Thread {
                    try {
                        val call = ApplicationModule.getApiService().getCallStatus(userID)
                        val response = call.execute()
                        if (response.isSuccessful) {
                            val res = response.body()
                            if (res?.status == true && res.data.type == "1") {
                                //call cut api
                                exitApiRun("0")
                                //timer stop
                                stopRepeatingAPICall()
                                //call cut
                                ZegoUIKitPrebuiltCallService.endCall()
                                //api update
                                Thread {
                                    try {
                                        val call = ApplicationModule.getApiService().submitCallCheckStatus(userID, "0")
                                        val response = call.execute()
                                        if (response.isSuccessful) {
                                            Log.d("API__", "Call cancelled successfully: ${response.body()}")
                                        } else {
                                            Log.e("API__", "API error: ${response.errorBody()?.string()}")
                                        }
                                    } catch (e: Exception) {
                                        Log.e("API__", "API call failed: ${e.message}")
                                    }
                                }.start()
                            }
                            Log.d("API__", "Call cancelled successfully: ${response.body()}")
                        } else {
                            Log.e("API__", "API error: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        Log.e("API__", "Call failed: ${e.message}")
                    }
                }.start()
            }
        }, 0, 5000)// 5000ms = 5 seconds
    }
    fun stopRepeatingAPICall() {
        timer?.cancel()
        timer = null
    }

    fun Context.wakeUpScreenAndKeepOn() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE,
            "app:WakeLockTag"
        )
        wakeLock.acquire(30 * 1000L) // Wake for 30 seconds

        val window = (this as? Activity)?.window
        window?.addFlags(
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        )
    }

    override fun onResume() {
        super.onResume()

        //incoming call received
        ZegoCallManager.initialize(application, userID, userName)
        //outGoingCall
        outGoingCallMethod()

//        viewModel.getUserProfileLiveData(userID, profile_id)
    }

    override fun onDestroy() {
        super.onDestroy()
//        ZegoUIKitPrebuiltCallService.unInit()
        ZegoUIKitPrebuiltCallService.endCall()
    }

}