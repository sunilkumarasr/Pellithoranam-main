package com.stranger_sparks.view.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.data_model.Caste
import com.stranger_sparks.databinding.ActivityBasicProfileBinding
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.FileUtil
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.ui.activities.webview.WebViewUrlLoad
import com.stranger_sparks.viewmodel.BasicProfileViewModel
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


class BasicProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityBasicProfileBinding

    @Inject
    lateinit var viewModel: BasicProfileViewModel
    var fullName: String = ""
    var familyName: String = ""
    var fatherName: String = ""
    var Mobile: String = ""
    var gender: String = ""
    var hobbies: String = ""
    var height: String = ""
    var weight: String = ""
    var profession: String = ""
    var education: String = ""
    var working_company: String = ""
    var designation: String = ""
    var salary: String = ""
    var religion: String = ""
    var locationAddress: String = ""
    var dob: String = ""
    var bio: String = ""
    var caste: String = ""
    var birthStar: String = ""
    var imgPos: Int = 0
    val txtList: MutableList<TextView> = mutableListOf()
    val imgUriList: Array<Uri?> = arrayOfNulls(1)
    var isInLetImageSelected: Boolean = false
    private var selectedImagesUri = mutableListOf<Uri>()
    private var selectedImagesFile = mutableListOf<File>()
    var mYear: Int? = null
    var mMonth: Int? = null
    var mDay: Int? = null
    var maritalSelection: String = ""
    var languageSelection: String = ""

    val mobileNumberPattern = "^[6-9]\\d{9}$"
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val religionlist = mutableListOf<String>()

    private var castList = arrayListOf<Caste>()

    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cropLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityBasicProfileBinding.inflate(layoutInflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        ConstantUtils.createFullScreen(this)
        txtList += listOf(
            binding.tvFileName
            // Add more Uri objects as needed
        )
        viewModel.inputSignal.observe(this) {

            if (it != null) {
                validateViewModelEvents(it)

            }
        }
        religionlist.add( "Select Religion")
        religionlist.add( "Hindu")
        //religionlist.add( "Muslim")
        //religionlist.add( "Cristian")


        val religionAdapter = CustomArrayAdapter(this, R.layout.custom_spinner_dropdown_item, religionlist)
        religionAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spReligion.adapter = religionAdapter
        binding.spReligion.onItemSelectedListener=object:OnItemSelectedListener,
            AdapterView.OnItemSelectedListener {


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               if(position==0)
                   religion=""
                else religion=religionlist.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                return true
            }

        }

        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                // 'ActivityResultCallback': Handle the returned Uri
                if (uri != null) {
                    selectedImagesUri.clear()
                    selectedImagesFile.clear()

                    selectedImagesUri.add(uri)
                    selectedImagesFile.add(FileUtil.from(applicationContext, uri))
                    isInLetImageSelected = true

                    binding.ivProfilePicture.setImageURI(selectedImagesUri[0])
                    binding.tvFileName.text = selectedImagesFile[0].name
                }
            }

        //search city's list
        viewModel.getCitysList()
        viewModel.getCaste()
        viewModel.cityListLiveData.observe(this) { response ->
            response?.data?.let { cities ->
                val cityNames = cities.map { it.name }
                val citylist= arrayListOf("Select City")
                citylist.addAll(cityNames)

                val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, citylist)
                binding.spLocation.setAdapter(adapter)
                /*try {
                    val pos = cityNames.indexOf(locationAddress)
                    binding.spLocation.setSelection(pos)
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }*/
            }
        }
        viewModel.casteListData.observe(this) { response ->
            response?.let { castes ->
                castList.clear()
                castList.add(Caste("0","Select Caste"))
                castList.addAll(castes)
                val adapter = CasteArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, castList)
                binding.spCaste.setAdapter(adapter)


            }
        }

        // Gallery Picker
        // -----------------------------
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let { openCropActivity(it) }
            }

        // -----------------------------
        // uCrop Result
        // -----------------------------
        cropLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val resultUri = UCrop.getOutput(result.data!!)
                    resultUri?.let { handleCroppedImage(it) }
                } else if (result.resultCode == UCrop.RESULT_ERROR) {
                    UCrop.getError(result.data!!)?.printStackTrace()
                }
            }

        binding.llUploadProfilePicture.setOnClickListener {
            galleryLauncher.launch("image/*")
        }


        binding.txtTerms.setOnClickListener {
            Intent(applicationContext, WebViewUrlLoad::class.java).also {
                it.putExtra("url_type", Constants.ObserverEvents.TERMS_AND_CONDITIONS.toString())
                startActivity(it)
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
        heightslist.add(0, "Height")
        val adapter = CustomArrayAdapter(this, R.layout.custom_spinner_dropdown_item, heightslist)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spHeight.adapter = adapter


        val adapterCast = ArrayAdapter(this, R.layout.custom_spinner_dropdown_item, heightslist)
        adapterCast.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        binding.spCaste.adapter = adapterCast



        //marital
        binding.materialRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedmaterial = findViewById<RadioButton>(checkedId)
            maritalSelection = selectedmaterial.text.toString()
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
                if (!binding.etMobile.text.toString().isNullOrBlank()) {
                    Mobile = binding.etMobile.text.toString().trim()
                } else {
                    Mobile = ""
                }


                    birthStar = binding.etBirthstar.text.toString().trim()


                height = binding.spHeight.selectedItem.toString().trim()


                bio = binding.etBio.text.toString().trim()
                dob = binding.tvDOB.text.toString().trim()
                weight = binding.etWeight.text.toString().trim()
                profession = binding.etProfiession.text.toString().trim()
                education = binding.etEducation.text.toString().trim()
                working_company = binding.etCompany.text.toString().trim()
                designation = binding.etPosition.text.toString().trim()
                salary = binding.etSalary.text.toString().trim()


                if (!binding.rbMale.isChecked && !binding.rbFemale.isChecked) {
                    ConstantUtils.showToast(this, "Please Select Gender")
                    return@setOnClickListener
                } else {

                    if (!binding.checkTerms.isChecked) {
                        ConstantUtils.showToast(this, "Please accept our Terms & Conditions")

                        return@setOnClickListener
                    }
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
                        var selectedGender: String = ""
                        if (binding.rbMale.isChecked) {
                            selectedGender = "Male"
                        } else if (binding.rbFemale.isChecked) {
                            selectedGender = "Female"
                        }
                        if (inletMultiPart != null) {
                            val sharedPreferenceManager = SharedPreferenceManager(this)
                            binding.progressLay.progressBar.visibility = View.VISIBLE
                            try {
//                                var phnunber =
//                                    sharedPreferenceManager.getSavedLoginResponseUser()?.data?.phone
                                var userID =
                                    sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id

                                viewModel.updateProfile(
                                    fullName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    familyName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    fatherName.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    selectedGender.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    hobbies.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    dob.toRequestBody("multipart/form-data".toMediaTypeOrNull()),

                                    Mobile.toString()
                                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    birthStar.toRequestBody("multipart/form-data".toMediaTypeOrNull()),

                                    height.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    weight.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    profession.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    education.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    working_company.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    designation.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    salary.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    religion.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    locationAddress.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    languageSelection.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    bio.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    userID.toString()
                                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    caste.toString()
                                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    inletMultiPart,

                                )

                            } catch (e: Exception) {
                                Log.v("Purushotham", e.message.toString())
                            }
                        }
                    } else {
                        ConstantUtils.showToast(this, "Please Upload Picture")
                        return@setOnClickListener
                    }
                }

            }

        }

        viewModel.upDateProfileLiveData.observe(this) { resp ->
            resp?.message?.let { ConstantUtils.showToast(this, it) }
            binding.progressLay.progressBar.visibility = View.GONE
            if (resp?.status == true) {
                val sharedPreferenceManager = SharedPreferenceManager(this)
                sharedPreferenceManager.clearAllData()
                sharedPreferenceManager.saveLoginResponse(resp)

                if (sharedPreferenceManager.getSavedLoginResponseUser()?.data?.profile_completed == "1") {
                    Intent(applicationContext, HomeActivity::class.java).also {
                        startActivity(it)
                    }
                }
            } else {
                ConstantUtils.showToast(applicationContext, "Failed")
            }
        }


        binding.tvDOB.setOnClickListener {

            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                null, // ❗ listener is null now
                year,
                month,
                day
            )

            // 🔞 Allow only 18+ dates
            calendar.add(Calendar.YEAR, -18)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis

            // ✅ POSITIVE button
            datePickerDialog.setButton(
                DatePickerDialog.BUTTON_POSITIVE,
                "OK"
            ) { _, _ ->

                val picker = datePickerDialog.datePicker

                val selectedDay = picker.dayOfMonth
                val selectedMonth = picker.month
                val selectedYear = picker.year

                val dob = String.format(
                    Locale.getDefault(),
                    "%02d-%02d-%04d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )

                binding.tvDOB.text = dob
            }

            // ❌ NEGATIVE button
            datePickerDialog.setButton(
                DatePickerDialog.BUTTON_NEGATIVE,
                "Cancel"
            ) { dialog, _ ->
                dialog.dismiss()
            }

            datePickerDialog.show()

            // 🎨 Button colors
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.blue_inner_text))

            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.blue_inner_text))
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

    private fun handleCroppedImage(uri: Uri) {
        val file = compressImage(this, uri, 60) ?: return

        selectedImagesUri.clear()
        selectedImagesFile.clear()

        selectedImagesUri.add(Uri.fromFile(file))
        selectedImagesFile.add(file)
        isInLetImageSelected = true

        Glide.with(this)
            .load(file)
            .transform(CenterCrop(), RoundedCorners(12))
            .error(R.drawable.ic_image_place_holder)
            .into(binding.ivProfilePicture)

        binding.tvFileName.text = file.name
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = data?.let { UCrop.getOutput(it) }

            resultUri?.let { uri ->
                // Compress the cropped image
                val file = compressImage(applicationContext, uri, 60) // quality 60%

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

    class CustomArrayAdapter(context: Context, resource: Int, objects: List<String>) :
        ArrayAdapter<String>(context, resource, objects) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            if (position == 0) {
                (view as TextView).setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
            }
            return view
        }
    }

    fun languageS() {
        val selectedLanguages = mutableListOf<String>()
        if (binding.checkBoxTelugu.isChecked) selectedLanguages.add("Telugu")
        if (binding.checkBoxEnglish.isChecked) selectedLanguages.add("English")
        if (binding.checkBoxHindi.isChecked) selectedLanguages.add("Hindi")
        languageSelection = selectedLanguages.joinToString(", ")
    }

    fun checkValidation(): Boolean {
        var ret = true
        if (!ConstantUtils.hasEditText(binding.etFullName, "Please Enter Full Name")) ret = false
        if (!ConstantUtils.hasEditText(binding.etFamilyName, "Please Enter Family Name")) ret = false
        if (!ConstantUtils.hasEditText(binding.etFatherName, "Please Enter Father Name")) ret = false
        //if (!ConstantUtils.hasEditText(binding.etEmailId, "Please Email Id")) ret = false
        if (!ConstantUtils.hasEditText(binding.etStudy, "Please Enter Hobbies")) ret = false

        if (binding.tvDOB.text.toString()
                .trim() == applicationContext.resources.getString(R.string.selecte_dob)||binding.tvDOB.text.toString()
                .trim().isEmpty())
         {
            ConstantUtils.showToast(applicationContext, "Please select Date Birth")
            ret = false
        }

        if (binding.spHeight.selectedItem.toString().equals("Height")||binding.spHeight.selectedItem.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Please select Height", Toast.LENGTH_SHORT).show()
            ret = false
        }
        if (binding.etWeight.text.toString().isEmpty()) {
            Toast.makeText(applicationContext, "Please enter weight", Toast.LENGTH_SHORT).show()
            ret = false
        }

        if (!ConstantUtils.hasEditText(binding.etProfiession, "Please Enter Profession")) ret = false
        if (!ConstantUtils.hasEditText(binding.etEducation, "Please Enter Education")) ret = false
        if (!ConstantUtils.hasEditText(binding.etCompany, "Please Enter Company Name")) ret = false
        if (!ConstantUtils.hasEditText(binding.etPosition, "Please Enter Designation")) ret = false
        if (!ConstantUtils.hasEditText(binding.etSalary, "Please Enter Salary")) ret = false
        if (religion.equals("")) {
            ConstantUtils.showToast(applicationContext, "Please select Religion")
            ret = false
        }

        if(binding.spCaste.selectedItemPosition==0)
            caste=""
        else
            caste = binding.spCaste.selectedItem.toString().trim()

        if(binding.spLocation.selectedItemPosition==0)
            locationAddress=""
        else
            locationAddress = binding.spLocation.selectedItem.toString().trim()

        if (languageSelection.equals("")) {
            ConstantUtils.showToast(applicationContext, "Please select at least one language")
            ret = false
        }

        if (caste.isEmpty()) {
            Toast.makeText(applicationContext, "please select caste", Toast.LENGTH_SHORT).show()
            ret = false
        }


        if (!isValidMobileNumber(binding.etMobile.text.toString())) {
            Toast.makeText(this, "Invalid  Mobile Number", Toast.LENGTH_SHORT).show()
            ret = false
        }

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

    fun validateViewModelEvents(observerEvents: String?) {
        if (observerEvents === Constants.ObserverEvents.GOTO_OTP.toString()) {

        } else if (observerEvents === Constants.ObserverEvents.GOTO_SIGN_UP.toString()) {

        } else if (observerEvents === Constants.ObserverEvents.GOTO_SIGN_IN.toString()) {

        }
    }


    inner class CasteArrayAdapter(context: Context, resource: Int, objects: List<Caste>) :
        ArrayAdapter<Caste>(context, resource, objects) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            if (position == 0) {
                (view as TextView).setTextColor(ContextCompat.getColor(context, R.color.dark_grey))
            }
            (view as TextView).setText(castList.get(position).name)
            if(position==0)
                caste=""
            else caste=castList.get(position).id
            return view
        }
    }
}