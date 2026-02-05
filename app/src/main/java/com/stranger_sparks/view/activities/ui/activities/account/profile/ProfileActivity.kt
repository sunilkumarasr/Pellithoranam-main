package com.stranger_sparks.view.activities.ui.activities.account.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.data_model.Caste
import com.stranger_sparks.databinding.ActivityProfileBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.FileUtil
import com.stranger_sparks.utils.SharedPreferenceManager
import com.yalantis.ucrop.UCrop
import id.zelory.compressor.Compressor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import javax.inject.Inject


class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding

    @Inject
    lateinit var viewModel: ProfileViewModel
    lateinit var phnunber: String
    lateinit var userID: String
    lateinit var fullName: String
    lateinit var gender: String
    lateinit var hobbies: String

    lateinit var location: String
    lateinit var email: String
    lateinit var imageurl: String
    var familyName: String = ""
    var fatherName: String = ""
    var height: String = ""
    var weight: String = ""
    var profession: String = ""
    var education: String = ""
    var working_company: String = ""
    var designation: String = ""
    var salary: String = ""
    var religion: String = ""
    var emailId: String = ""

    //var locationAddress: String = ""
    var imgPos: Int = 0
    val txtList: MutableList<TextView> = mutableListOf()
    val imgUriList: Array<Uri?> = arrayOfNulls(1)
    var isInLetImageSelected: Boolean = false

    private var selectedImagesUri = mutableListOf<Uri>()
    private var selectedImagesFile = mutableListOf<File>()
    private var castList = arrayListOf<Caste>()
    var mYear: Int? = null
    var mMonth: Int? = null
    var mDay: Int? = null
    var dob: String = ""
    var bio: String = ""
    var caste: String = ""
    var birth_star: String = ""
    var maritalSelection: String = ""
    var languageSelection: String = ""

    val context: Context = this

    val mobileNumberPattern = "^[6-9]\\d{9}$"
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    private lateinit var galleryLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ConstantUtils.changeNotificationBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.inputSignal.observe(this) {

            if (it != null) {
                validateViewModelEvents(it)

            }
        }
        selectedImagesFile = arrayListOf<File>()
        selectedImagesUri = arrayListOf<Uri>()
        txtList += listOf(
            binding.tvFileName
            // Add more Uri objects as needed
        )
        binding.ivClose.setOnClickListener {
            finish()
        }


        //disable
        binding.rbMale.isEnabled = false
        binding.rbFemale.isEnabled = false
        binding.radioButtonSingle.isEnabled = false
        binding.radioButtonMarriage.isEnabled = false
        binding.tvDOB.isEnabled = false


        val sharedPreferenceManager = SharedPreferenceManager(this)
        familyName =
            sharedPreferenceManager.getSavedLoginResponseUser()?.data?.family_name.toString()
        fatherName =
            sharedPreferenceManager.getSavedLoginResponseUser()?.data?.father_name.toString()
        weight = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.weight.toString()
        designation =
            sharedPreferenceManager.getSavedLoginResponseUser()?.data?.designation.toString()
        education = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.education.toString()
        working_company =
            sharedPreferenceManager.getSavedLoginResponseUser()?.data?.company.toString()
        profession = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.job.toString()
        salary = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.salary.toString()
        religion = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.religion.toString()
        phnunber = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.phone.toString()
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()
        fullName = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.name.toString()
        gender = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.gender.toString()
        hobbies = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.hobbies.toString()
        email = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.email.toString()
        height = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.height.toString()
        location = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.location.toString()
        imageurl = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.image.toString()
        bio = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.description.toString()
        dob = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.age.toString()
        birth_star =
            sharedPreferenceManager.getSavedLoginResponseUser()?.data?.birth_star.toString()
        maritalSelection =
            sharedPreferenceManager.getSavedLoginResponseUser()?.data?.marital.toString()
        languageSelection =
            sharedPreferenceManager.getSavedLoginResponseUser()?.data?.languages.toString()
        caste = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.caste.toString()

//        Log.e("image_",imageurl)
//        val uris = Uri.parse(imageurl)
//        if (uris!=null){
//
//            selectedImagesUri.add(uris)
//            isInLetImageSelected = true
//
//        }

        binding.etFullName.setText(fullName)
        binding.etFamilyName.setText(familyName)
        binding.etFatherName.setText(fatherName)
        binding.etFatherName.setText(fatherName)
        binding.etWeight.setText(weight)
        binding.etProfiession.setText(profession)
        binding.etEducation.setText(education)
        binding.etCompany.setText(working_company)
        binding.etPosition.setText(designation)
        binding.etSalary.setText(salary)
        binding.etReligion.setText(religion)
        binding.etEmailId.setText(email)
        // binding.spLocation.setText(location)
        binding.etStudy.setText(hobbies)
        binding.etBio.setText(bio)
        binding.tvDOB.setText(dob)
        binding.etEmailId.setText(phnunber)

        binding.etBirthstar.setText(birth_star)
        if (gender == "Male") {
            binding.rbMale.isChecked = true
        } else {
            binding.rbFemale.isChecked = true
        }
        if (maritalSelection.equals("Single")) {
            binding.radioButtonSingle.isChecked = true
        } else {
            binding.radioButtonMarriage.isChecked = true
        }
        val languagesList = languageSelection.split(",")
        languagesList.forEach { language ->
            println(language.trim())
            if (language.trim() == "Telugu") {
                binding.checkBoxTelugu.isChecked = true
            } else if (language.trim() == "English") {
                binding.checkBoxEnglish.isChecked = true
            } else if (language.trim() == "Hindi") {
                binding.checkBoxHindi.isChecked = true
            }
        }
        Glide.with(this).load(imageurl)
            .error(R.drawable.ic_image_place_holder)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(binding.ivProfilePicture)

        //search city's list
        viewModel.getCitysList()
        viewModel.getCaste()
        viewModel.cityListLiveData.observe(this) { response ->
            response?.data?.let { cities ->
                val cityNames = cities.map { it.name }
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cityNames)
                binding.spLocation.setAdapter(adapter)
                try {
                    val pos = cityNames.indexOf(location)
                    binding.spLocation.setSelection(pos)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        viewModel.casteListData.observe(this) { response ->
            response?.let { castes ->
                castList.clear()
                castList.addAll(castes)
                val castListNames = castList.map { it.name }
                val adapter =
                    CasteArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, castList)
                binding.spCaste.setAdapter(adapter)
                try {
                    val pos = castListNames.indexOf(caste)
                    binding.spCaste.setSelection(pos)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }


        val heightslist = mutableListOf<String>()

        for (feet in 4..7) {
            for (inch in 0..12) {
                val height = "$feet.$inch"
                if (height.toFloat() >= 4.0 && height.toFloat() <= 7.5) {
                    heightslist.add(height + " ft")
                }
            }
        }
        //default value
        heightslist.add(0, height)
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, heightslist)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spHeight.adapter = adapter

        val adapterCast = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, heightslist)
        adapterCast.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spCaste.adapter = adapterCast


        binding.materialRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedmaterial = findViewById<RadioButton>(checkedId)
            maritalSelection = selectedmaterial.text.toString()
        }


        /*val pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects media items or closes the
                // photo picker.
                if (uris.isNotEmpty()) {

                    selectedImagesUri.clear()
                    selectedImagesFile.clear()

                        selectedImagesUri.add(it)
                        selectedImagesFile.add(FileUtil.from(applicationContext, it))
                        *//*runOnUiThread(
                            object : Runnable {
                                override fun run() {
                                    selectedImagesUri.add(it)
                                    selectedImagesFile.add(FileUtil.from(ctx, it))
                                }
                            }
                        )*//*
                        isInLetImageSelected = true

                        binding.ivProfilePicture.setImageURI(selectedImagesUri[0])
                        binding.tvFileName.text = selectedImagesFile[0].name



                    //imageAdapter = ImageAdapter(ctx as ImagesGrid, selectedImagesUri, selectedImagesFile)


                    Log.d("PhotoPicker", "Number of items selected: ${uris.size} -- ${selectedImagesFile.size}")
                    Log.d("PhotoPicker", "Number of items selected: ${selectedImagesFile.toString()}")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }*/

        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                // 'ActivityResultCallback': Handle the returned Uri
                if (uri != null) {
                    selectedImagesUri.clear()
                    selectedImagesFile.clear()

                    selectedImagesUri.add(uri)
                    selectedImagesFile.add(FileUtil.from(applicationContext, uri))
                    isInLetImageSelected = true

                    Glide.with(this).load(selectedImagesFile[0])
                        .error(R.drawable.ic_image_place_holder)
                        .transform(CenterCrop(), RoundedCorners(10))
                        .into(binding.ivProfilePicture)
                    //binding.ivProfilePicture.setImageURI(selectedImagesUri[0])
                    binding.tvFileName.text = selectedImagesFile[0].name
                    Log.e("adsdas", "das")
                }
            }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                openCropActivity(it)
            }
        }

        binding.llUploadProfilePicture.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnContinue.setOnClickListener {

            languageS()


            if (!ConstantUtils.isNetworkConnected(applicationContext)) {
                ConstantUtils.alertDialog("Please Check Internet Connection", this)
            } else if (checkValidation()) {

                fullName = binding.etFullName.text.toString().trim()
                hobbies = binding.etStudy.text.toString().trim()
                familyName = binding.etFamilyName.text.toString().trim()
                fatherName = binding.etFatherName.text.toString().trim()
                if (!binding.etEmailId.text.toString().isNullOrBlank()) {
                    emailId = binding.etEmailId.text.toString().trim()
                } else {
                    emailId = ""
                }


                birth_star = binding.etBirthstar.text.toString().trim()


                height = binding.spHeight.selectedItem.toString().trim()
                bio = binding.etBio.text.toString().trim()
                location = binding.spLocation.selectedItem.toString().trim()
                dob = binding.tvDOB.text.toString().trim()
                weight = binding.etWeight.text.toString().trim()
                profession = binding.etProfiession.text.toString().trim()
                education = binding.etEducation.text.toString().trim()
                working_company = binding.etCompany.text.toString().trim()
                designation = binding.etPosition.text.toString().trim()
                salary = binding.etSalary.text.toString().trim()
                religion = binding.etReligion.text.toString().trim()
                caste = binding.spCaste.selectedItem.toString().trim()


                if (!binding.rbMale.isChecked && !binding.rbFemale.isChecked) {
                    ConstantUtils.showToast(this, "Please Select Gender")
                    return@setOnClickListener
                } else {
                    if (isInLetImageSelected) {
                        var file: File
                        var inletMultiPart: MultipartBody.Part?
                        if (selectedImagesFile[0]?.path.isNullOrBlank()) {
                            ConstantUtils.showToast(this, "Please Picture")
                            return@setOnClickListener
                        } else {
                            inletMultiPart =
                                selectedImagesFile[0].asRequestBody("multipart/form-data".toMediaTypeOrNull())
                                    ?.let {
                                        MultipartBody.Part.createFormData(
                                            "image",
                                            selectedImagesFile[0].name,
                                            it
                                        )
                                    }

                        }
                        if (inletMultiPart != null) {
                            val sharedPreferenceManager = SharedPreferenceManager(this)
                            binding.progressLay.progressBar.visibility = View.VISIBLE
                            try {
                                var phnunber =
                                    sharedPreferenceManager.getSavedLoginResponseUser()?.data?.phone
                                var userID =
                                    sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id
                                phnunber = binding.etEmailId.text.toString().trim()
                                var selectedGender: String = ""
                                if (binding.rbMale.isChecked) {
                                    selectedGender = "Male"
                                } else if (binding.rbFemale.isChecked) {
                                    selectedGender = "Female"
                                }

                                viewModel.updateProfile(
                                    fullName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    familyName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    fatherName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    selectedGender.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    hobbies.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    dob.toRequestBody("multipart/form-data".toMediaTypeOrNull()),


                                    phnunber.toString()
                                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    birth_star.toRequestBody("multipart/form-data".toMediaTypeOrNull()),

                                    height.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    weight.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    profession.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    education.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    working_company.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    designation.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    salary.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    religion.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    location.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    languageSelection.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    bio.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    userID.toString()
                                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    caste.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    inletMultiPart,
                                    )
                            } catch (e: Exception) {
                                Log.v("Purushotham", e.message.toString())
                            }
                        }
                    } else {
                        val sharedPreferenceManager = SharedPreferenceManager(this)
                        binding.progressLay.progressBar.visibility = View.VISIBLE
                        try {
                            var phnunber = binding.etEmailId.text.toString().trim()

                            var userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id

                            var selectedGender: String = ""
                            if (binding.rbMale.isChecked) {
                                selectedGender = "Male"
                            } else if (binding.rbFemale.isChecked) {
                                selectedGender = "Female"
                            }

                            viewModel.updateProfilewithoutpic(
                                fullName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                familyName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                fatherName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                selectedGender.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                hobbies.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                dob.toRequestBody("multipart/form-data".toMediaTypeOrNull()),


                                phnunber.toString()
                                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                birth_star.toRequestBody("multipart/form-data".toMediaTypeOrNull()),

                                height.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                weight.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                profession.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                education.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                working_company.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                designation.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                salary.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                religion.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                location.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                languageSelection.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                bio.toRequestBody("multipart/form-data".toMediaTypeOrNull()),

                                userID.toString()
                                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                caste.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                            )
                        } catch (e: Exception) {
                            Log.v("Purushotham", e.message.toString())
                        }
                    }
                }

            }

        }


        viewModel.upDateProfileLiveData.observe(this) { resp ->
            resp?.message?.let { it1 -> ConstantUtils.showSuccessToast(this, it1) }
            binding.progressLay.progressBar.visibility = View.GONE
            if (resp?.status == true) {
                val sharedPreferenceManager = SharedPreferenceManager(this)
                sharedPreferenceManager.clearAllData()
                if (resp != null) {
                    sharedPreferenceManager.saveLoginResponse(resp)
                }

                finish()
            } else {
                Log.e("resp", "resp")
                //res?.message?.let { ConstantUtils.showToast(this, it) }
            }
        }
        binding.tvDOB.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                R.style.SpinnerDatePickerDialogStyle,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
                    binding.tvDOB.text = selectedDate
                },
                mYear,
                mMonth,
                mDay
            )
            // Calculate the date 18 years ago from today
            c.add(Calendar.YEAR, -18)
            datePickerDialog.datePicker.maxDate = c.timeInMillis

            datePickerDialog.show()
        }
    }

    //image upload
    private fun openCropActivity(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "croppedImage.jpg"))

        val options = UCrop.Options().apply {
            setCompressionFormat(Bitmap.CompressFormat.JPEG)
            setCompressionQuality(90)
            setFreeStyleCropEnabled(true) // Allows user to adjust crop freely
        }

        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .withAspectRatio(1f, 1f) // Example: square crop
            .start(this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = data?.let { UCrop.getOutput(it) }

            resultUri?.let { uri ->
                // Compress the cropped image
                val file = compressImage(applicationContext, uri, 50) // quality 50%

                file?.let {
                    // Clear previous selections
                    selectedImagesUri.clear()
                    selectedImagesFile.clear()

                    // Add new selection
                    selectedImagesUri.add(Uri.fromFile(it))
                    selectedImagesFile.add(it)
                    isInLetImageSelected = true

                    // Display image in ImageView
                    Glide.with(this)
                        .load(it)
                        .error(R.drawable.ic_image_place_holder)
                        .transform(CenterCrop(), RoundedCorners(10))
                        .into(binding.ivProfilePicture)

                    // Show file name
                    binding.tvFileName.text = it.name
                }
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = data?.let { UCrop.getError(it) }
            cropError?.printStackTrace()
        }
    }
    private fun compressImage(context: Context, uri: Uri, quality: Int): File? {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

            val file = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun languageS() {
        val selectedLanguages = mutableListOf<String>()
        if (binding.checkBoxTelugu.isChecked) selectedLanguages.add("Telugu")
        if (binding.checkBoxEnglish.isChecked) selectedLanguages.add("English")
        if (binding.checkBoxHindi.isChecked) selectedLanguages.add("Hindi")
        languageSelection = selectedLanguages.joinToString(", ")
    }

    private fun validateViewModelEvents(observerEvents: String) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()) {
            finish()
        }
    }


    fun checkValidation(): Boolean {
        var ret = true
        if (!ConstantUtils.hasEditText(binding.etFullName, "Please Enter Full Name")) ret = false
        if (!ConstantUtils.hasEditText(binding.etFamilyName, "Please Enter Family Name")) ret =
            false
        if (!ConstantUtils.hasEditText(binding.etFatherName, "Please Enter Father Name")) ret =
            false
        //if (!ConstantUtils.hasEditText(binding.etEmailId, "Please Email Id")) ret = false
        if (!ConstantUtils.hasEditText(binding.etStudy, "Please Enter Hobbies")) ret = false
        /*  if (maritalSelection.equals("")) {
              ConstantUtils.showToast(applicationContext, "Please select Marital")
              ret = false
          }*/
        if (binding.tvDOB.text.toString()
                .trim() == applicationContext.resources.getString(R.string.selecte_dob) || binding.tvDOB.text.toString()
                .trim().isEmpty()
        ) {
            ConstantUtils.showToast(applicationContext, "Please select Date Birth")
            ret = false
        }

        if (binding.spHeight.selectedItem.toString()
                .equals("Height") || binding.spHeight.selectedItem.toString().isEmpty()
        ) {
            Toast.makeText(applicationContext, "Please select Height", Toast.LENGTH_SHORT).show()
            ret = false
        }
        if (binding.etWeight.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Please enter weight", Toast.LENGTH_SHORT).show()
            ret = false
        }

        if (!ConstantUtils.hasEditText(binding.etProfiession, "Please Enter Profession")) ret =
            false
        if (!ConstantUtils.hasEditText(binding.etEducation, "Please Enter Education")) ret = false
        if (!ConstantUtils.hasEditText(binding.etCompany, "Please Enter Company Name")) ret = false
        if (!ConstantUtils.hasEditText(binding.etPosition, "Please Enter Designation")) ret = false
        if (!ConstantUtils.hasEditText(binding.etSalary, "Please Enter Salary")) ret = false
        if (!ConstantUtils.hasEditText(binding.etReligion, "Please Enter Religion")) ret = false
        if (languageSelection.equals("")) {
            ConstantUtils.showToast(applicationContext, "Please select at least one language")
            ret = false
        }

        if (location.isEmpty()) {
            Toast.makeText(applicationContext, "please select location", Toast.LENGTH_SHORT).show()
            ret = false
        }



        if ((binding.etBirthstar.text.toString().isEmpty())) {
            Toast.makeText(this, "Enter Birth star", Toast.LENGTH_SHORT).show()
            ret = false
        }
        caste = binding.spCaste.selectedItem.toString().trim()

        if (caste.isEmpty()) {
            Toast.makeText(this, "Please Select Caste", Toast.LENGTH_SHORT).show()

            ret = false
        }

        /*if (!isValidMobileNumber(binding.etEmailId.text.toString())) {
            Toast.makeText(this, "Invalid  Mobile Number", Toast.LENGTH_SHORT).show()
            ret = false
        }*/

//
//        if (!isValidEmail(binding.etEmailId.text.toString())) {
//            Toast.makeText(this, "Invalid  Email", Toast.LENGTH_SHORT).show()
//            ret = false
//        }

        if (!ConstantUtils.hasEditText(binding.etBio, "Please Enter Bio")) ret = false
        return ret
    }

    fun isValidMobileNumber(mobileNumber: String): Boolean {
        val regex = Regex(mobileNumberPattern)
        return mobileNumber.matches(regex)
    }

    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && email.matches(emailPattern.toRegex())
    }

    inner class CasteArrayAdapter(context: Context, resource: Int, objects: List<Caste>) :
        ArrayAdapter<Caste>(context, resource, objects) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            if (position == 0) {
                (view as TextView).setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
            }
            (view as TextView).setText(castList.get(position).name)
            if (position == 0)
                caste = ""
            else caste = castList.get(position).id
            return view
        }
    }
}