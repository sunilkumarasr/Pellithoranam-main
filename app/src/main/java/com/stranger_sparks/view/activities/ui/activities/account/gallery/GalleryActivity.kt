package com.stranger_sparks.view.activities.ui.activities.account.gallery

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.stranger_sparks.R
import com.stranger_sparks.StrangerSparksApplication
import com.stranger_sparks.adapterrs.SavedGalleryImagesAdapter
import com.stranger_sparks.data_model.GalleryImagesResponse
import com.stranger_sparks.databinding.ActivityGalleryBinding
import com.stranger_sparks.datamodels.HomeDataModel
import com.stranger_sparks.inerfaces.SavedGalareyImageOnItemClickListenerWithPosition
import com.stranger_sparks.utils.ConstantUtils
import com.stranger_sparks.utils.Constants
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.ui.activities.account.add_images.AddImagesActivity
import javax.inject.Inject

class GalleryActivity : AppCompatActivity(), SavedGalareyImageOnItemClickListenerWithPosition {
    lateinit var binding: ActivityGalleryBinding
    @Inject
    lateinit var viewModel: GalleryViewModel
    private lateinit var  savedGalleryImagesAdapter: SavedGalleryImagesAdapter
    private var dataList = mutableListOf<HomeDataModel>()
    lateinit var userID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.application as StrangerSparksApplication).applicationComponent.inject(this)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
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
        binding.ivAddImages.setOnClickListener {
            Intent(applicationContext, AddImagesActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.rcvGallery.layoutManager = GridLayoutManager(applicationContext,2)
        savedGalleryImagesAdapter = SavedGalleryImagesAdapter(this@GalleryActivity, this@GalleryActivity)
        binding.rcvGallery.adapter = savedGalleryImagesAdapter
        val sharedPreferenceManager = SharedPreferenceManager(this)
        userID = sharedPreferenceManager.getSavedLoginResponseUser()?.data?.id.toString()
        viewModel.savedGalleryImages(userID)
        viewModel.savedGalleryImages.observe(this){
            if(it?.status == true) {
                binding.tvNoRecordsDefault.visibility = View.GONE
                binding.rcvGallery.visibility = View.VISIBLE
                it?.data?.let { it1 -> savedGalleryImagesAdapter.setDataList(it1) }
                savedGalleryImagesAdapter.notifyDataSetChanged()
            }else{
                binding.tvNoRecordsDefault.visibility = View.VISIBLE
                binding.rcvGallery.visibility = View.GONE
            }
        }

        viewModel.deleteGalleryImage.observe(this){
            if(it?.status == true){
                it?.message?.let { it1 -> ConstantUtils.showSuccessToast(this, it1) }

                viewModel?.savedGalleryImages(userID)

            }else{
                it?.message?.let { it1 -> ConstantUtils.showSuccessToast(this, it1) }
            }
        }

    }
    private fun validateViewModelEvents(observerEvents: String) {
        if (observerEvents === Constants.ObserverEvents.CLOSE_NOTIFICATION_SCREEN.toString()) {

            finish()
        }
    }



    override fun clickOnCurrentPositionListener(slectedImage: GalleryImagesResponse.Data) {





        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure want to delete?")
        builder.setTitle("Alert !")
        builder.setCancelable(false)
        builder.setPositiveButton(
            "Yes",
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                dialog?.cancel()
                viewModel.deleteGalleryImage(slectedImage.id)
            } as DialogInterface.OnClickListener)
        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                // If user click no then dialog box is canceled.
                dialog.cancel()
            } as DialogInterface.OnClickListener)
        val alertDialog = builder.create()
        alertDialog.show()
        //ConstantUtils.showToast(this, slectedImage.image)
    }

    override fun onResume() {
        super.onResume()
        viewModel?.savedGalleryImages(userID)

    }
}