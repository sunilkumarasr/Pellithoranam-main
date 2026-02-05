package com.stranger_sparks.view.activities.ui.activities.chat.chat_room

import android.R.attr.name
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.ChatRoomAdapter
import com.stranger_sparks.data_model.ChatResponse
import com.stranger_sparks.databinding.ActivityChatRoomBinding
import com.stranger_sparks.datamodels.Message
import com.stranger_sparks.inerfaces.OnItemClickListenerChatRoom
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.FileUtil
import com.stranger_sparks.utils.SharedPreferenceManager
import id.zelory.compressor.Compressor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream

class ChatRoom : AppCompatActivity(), OnItemClickListenerChatRoom {
    private lateinit var dialog: Dialog
    lateinit var binding: ActivityChatRoomBinding

    @Inject
    lateinit var viewModel: ChatRoomViewModel
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    lateinit var userID: String
    var messages: ArrayList<Message>? = null
    lateinit var profile_id: String
    var is_chart: Boolean = false
    var isInLetImageSelected: Boolean = false
    private var selectedImagesUri = mutableListOf<Uri>()
    private var selectedImagesFile = mutableListOf<File>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )

        profile_id = intent.extras?.getString("PROFILE_ID").toString()
        is_chart = intent.getBooleanExtra("is_chart", false)

        val sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        /*messages = ArrayList()
        messages!!.add(Message("Hello world", "1", 2023))
        messages!!.add(Message("Hello world", "2", 2023))
        messages!!.add(Message("How Are you", "1", 2023))
        messages!!.add(Message("Fine", "2", 2023))
        messages!!.add(Message("How Are you", "1", 2023))
        messages!!.add(Message("Thanks You", "2", 2023))
        messages!!.add(Message("Hello world", "1", 2023))*/
        chatRoomAdapter = ChatRoomAdapter(this@ChatRoom, userID, profile_id, this)

        val layoutManager = LinearLayoutManager(this@ChatRoom).apply {
            stackFromEnd = true
        }

        binding!!.rcvChat.layoutManager = layoutManager
        binding!!.rcvChat.adapter = chatRoomAdapter


        // notificationAdapter.setDataList(dataList)

        viewModel.getMessageLiveData(userID, profile_id)
        viewModel.getMessageLiveData.observe(this) {
            if (it?.status == true && !it.data.isNullOrEmpty()) {

                binding.tvNoRecordsDefault.visibility = View.GONE
                binding.rcvChat.visibility = View.VISIBLE

                val reversedList = it.data.reversed()   // 👈 reverse here
                chatRoomAdapter.setDataList(reversedList)
                chatRoomAdapter.notifyDataSetChanged()

                binding.rcvChat.post {
                    binding.rcvChat.scrollToPosition(reversedList.size - 1)
                }

            } else {
                binding.tvNoRecordsDefault.visibility = View.VISIBLE
                binding.rcvChat.visibility = View.GONE
            }
        }

        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.ivSendMessage.setOnClickListener {
            if (is_chart) {
                if (!binding.etEnteredMessage.text.toString().trim().isNullOrBlank()) {
                    viewModel.sendMessageLiveData(
                        userID,
                        profile_id,
                        binding.etEnteredMessage.text.toString().trim()
                    )
                } else {
                    ConstantUtils.showToast(applicationContext, "Please enter Message...!")
                }
            }
        }
        viewModel.sendMessageLiveData.observe(this) {
            if (it?.status == true) {
                binding.etEnteredMessage.text.clear()
                viewModel.getMessageLiveData(userID, profile_id)
            } else {
                if (it != null) {
                    ConstantUtils.showToast(applicationContext, it.message)
                }
            }
        }
        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                // 'ActivityResultCallback': Handle the returned Uri

            Log.e("File Path  ","File Path $uri");
                if (uri != null) {
                    selectedImagesUri.clear()
                    selectedImagesFile.clear()

                    selectedImagesUri.add(uri)
                    selectedImagesFile.add(FileUtil.from(applicationContext, uri))
                    //selectedImagesFile.add(FileUtil.from(applicationContext, uri))
                    isInLetImageSelected = true

                    dialog = Dialog(this@ChatRoom, R.style.AlertDialogCustom)
                    dialog.setContentView(R.layout.custom_alert_chat_image)
                    dialog.setCancelable(false)

                    val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancel)
                    val btnSend = dialog.findViewById<AppCompatButton>(R.id.btnSend)
                    val ivChatImage = dialog.findViewById<ImageView>(R.id.ivChatImage)
                    val progressBar = dialog.findViewById<ProgressBar>(R.id.progressBar)
                    ivChatImage.setImageURI(selectedImagesUri[0])
                    btnSend.setOnClickListener { v: View? ->
                        /*callSendImageCahtImage()*/

                        if (!ConstantUtils.isNetworkConnected(applicationContext)) {
                            ConstantUtils.alertDialog("Please Check Internet Connection", this)
                        } else {
                            var inletMultiPart: MultipartBody.Part?
                            if (selectedImagesFile[0]?.path.isNullOrBlank()) {
                                ConstantUtils.showToast(this, "Please Select Picture")
                                return@setOnClickListener
                            } else {
                                val compressedFile = compressImageFile(selectedImagesFile[0])
                                val requestFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                inletMultiPart = MultipartBody.Part.createFormData(
                                    "image",
                                    compressedFile.name,
                                    requestFile
                                )
                            }

                            if (inletMultiPart != null) {
                                val sharedPreferenceManager = SharedPreferenceManager(this)
                                // progressBar.visibility = View.VISIBLE
                                try {
                                    var phnunber =
                                        sharedPreferenceManager.getSavedLoginResponseUser()?.data?.phone
                                    var userID =
                                        sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id

                                    if (isInLetImageSelected) {
                                        userID?.let {
                                            viewModel.sendChartImage(
                                                it.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                                profile_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),

                                                inletMultiPart
                                            )
                                        }
                                        isInLetImageSelected = false

                                    }

                                } catch (e: Exception) {
                                    Log.v("Purushotham", e.message.toString())
                                }
                            }

                           // dialog.dismiss()
                        }

                    }
                    btnCancel.setOnClickListener { v: View? ->
                        isInLetImageSelected = false
                        dialog.dismiss()
                    }
                    dialog.show()

                    /*binding.ivProfilePicture.setImageURI(selectedImagesUri[0])
                    binding.tvFileName.text = selectedImagesFile[0].name*/

                }
            }
        binding.ivAttachment.setOnClickListener { }
        binding.ivCamera.setOnClickListener { getContent.launch("image/*") }
        viewModel.sendChartImageLiveData.observe(this) {
            if (it?.status == true) {
                if (dialog != null) {
                    dialog.dismiss()
                    isInLetImageSelected = false
                    viewModel.getMessageLiveData(userID, profile_id)
                }
            } else {
                if (it != null) {
                    ConstantUtils.showToast(applicationContext, it.message)
                }
            }
        }
        startUpdates()

        viewModel.deleteMessageLiveData.observe(this) {
            if (it?.status == true) {
                viewModel.getMessageLiveData(userID, profile_id)
            } else {
                Toast.makeText(this@ChatRoom, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.deleteMessageBothData.observe(this) {
            if (it?.status == true) {
                viewModel.getMessageLiveData(userID, profile_id)
            } else {
                Toast.makeText(this@ChatRoom, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun startUpdates() {
        val lifecycle = this //in Activity

        lifecycle.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // this block is automatically executed when moving into
                // the started state, and cancelled when stopping.
                while (true) {
                    viewModel.getMessageLiveData(userID, profile_id) // the function to repeat
                    delay(5000)
                }
            }
        }

    }



    override fun clickOnCurrentPositionListener(item: ChatResponse.Data, type: String) {
        if (type.equals("single")) {
            viewModel.delete_only_for_you_post(item.sender_id, item.id)
        } else {
            viewModel.delete_only_for_both_post(item.sender_id, item.id)
        }
    }


    fun compressImageFile(originalFile: File): File {
        // Decode the original file to a bitmap
        val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath)

        // Compress the bitmap to JPEG with reduced quality
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream) // 70 = compression quality

        // Save the compressed bytes to a new file
        val compressedFile = File(this@ChatRoom.cacheDir, "compressed_${originalFile.name}")
        val fos = FileOutputStream(compressedFile)
        fos.write(outputStream.toByteArray())
        fos.flush()
        fos.close()

        return compressedFile
    }



}