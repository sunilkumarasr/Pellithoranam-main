package com.stranger_sparks.view.activities.ui.activities.account.add_images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.AddImagesAdapter
import com.stranger_sparks.databinding.ActivityAddImagesBinding
import com.stranger_sparks.inerfaces.BeforeSaveImageItemSelect
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.FileUtil
import com.stranger_sparks.utils.SharedPreferenceManager
import id.zelory.compressor.Compressor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AddImagesActivity : AppCompatActivity(), BeforeSaveImageItemSelect {
    lateinit var binding: ActivityAddImagesBinding
    @Inject
    lateinit var viewModel: AddImagesViewModel
    private var selectedImagesUri = mutableListOf<Uri>()
    private var selectedImagesFile = mutableListOf<File>()
    lateinit var imageAdapter: AddImagesAdapter
    lateinit var userID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityAddImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(this, ContextCompat.getColor(this, R.color.black), false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.inputSignal.observe(this) {

            if (it != null) {
                validateViewModelEvents(it)
            }

        }

        val layoutManager = GridLayoutManager(applicationContext,2)
        binding.rcvUploadImages.layoutManager = layoutManager
        binding.rcvUploadImages.setHasFixedSize(true)

        val sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()

        selectedImagesFile = arrayListOf<File>()
        selectedImagesUri = arrayListOf<Uri>()
        imageAdapter = AddImagesAdapter(this, selectedImagesUri, selectedImagesFile, this)

        val pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                // Callback is invoked after the user selects media items or closes the
                // photo picker.
                if (uris.isNotEmpty()) {

                    selectedImagesUri.clear()
                    selectedImagesFile.clear()
                    uris.forEach {
                        selectedImagesUri.add(it)
                        selectedImagesFile.add(FileUtil.from(applicationContext, it))
                        /*runOnUiThread(
                            object : Runnable {
                                override fun run() {
                                    selectedImagesUri.add(it)
                                    selectedImagesFile.add(FileUtil.from(ctx, it))
                                }
                            }
                        )*/
                    }

                    //imageAdapter = ImageAdapter(ctx as ImagesGrid, selectedImagesUri, selectedImagesFile)
                    binding.rcvUploadImages.adapter = imageAdapter
                    imageAdapter.notifyDataSetChanged()

                    Log.d("PhotoPicker", "Number of items selected: ${uris.size} -- ${selectedImagesFile.size}")
                    Log.d("PhotoPicker", "Number of items selected: ${selectedImagesFile.toString()}")

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }

            }


        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.btnUpload.setOnClickListener {
            val multipartBodys = mutableListOf<MultipartBody.Part>()
            selectedImagesFile.forEachIndexed { index, file ->

                val compressedFile = compressImageFile(this@AddImagesActivity, file)

                val requestBody = compressedFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("files[]", compressedFile.name, requestBody)

                multipartBodys.add(part)

            }
            val userId: RequestBody =
                userID.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            if (selectedImagesFile.size > 0) {

                viewModel.uploadImagesLiveData(userId, multipartBodys.toTypedArray())
            }else{
                ConstantUtils.showToast(this, "Please Add Images")
            }

        }

        viewModel.uploadImagesLiveData.observe(this){
            if(it?.status == true){
                it?.message?.let { it1 -> ConstantUtils.showSuccessToast(this, it1) }

                finish()

            }else{
                it?.message?.let { it1 -> ConstantUtils.showSuccessToast(this, it1) }
            }
        }


    }

    fun compressImageFile(context: Context, file: File): File {
        // Decode the original image file to a Bitmap
        val originalBitmap = BitmapFactory.decodeFile(file.absolutePath)

        // Create a new File for the compressed image
        val compressedFile = File(context.cacheDir, "compressed_${file.name}")
        val outputStream = FileOutputStream(compressedFile)

        // Compress the bitmap to JPEG format with 70% quality (you can adjust this)
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        outputStream.flush()
        outputStream.close()

        return compressedFile
    }

    private fun validateViewModelEvents(observerEvents: String) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()) {

            finish()
        }
    }

    override fun singleItemSelect(
        deleteUri: Uri,
        deleteUriPosition: Int,
        deleteFile: File,
        deleteFilePosition: Int
    ) {
        selectedImagesUri.removeAt(deleteUriPosition)
        selectedImagesFile.removeAt(deleteFilePosition)
        imageAdapter.notifyDataSetChanged()
    }


}

/*
interface ItemsSelectionListener {
    fun selectedItems(isSelected: Boolean, imageItem: ImagesList)
}*/
