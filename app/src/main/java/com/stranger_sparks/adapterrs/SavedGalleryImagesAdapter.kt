package com.stranger_sparks.adapterrs

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stranger_sparks.BuildConfig
import com.stranger_sparks.R
import com.stranger_sparks.data_model.GalleryImagesResponse
import com.stranger_sparks.datamodels.HomeDataModel
import com.stranger_sparks.inerfaces.OnItemClickListenerWithPosition
import com.stranger_sparks.inerfaces.SavedGalareyImageOnItemClickListenerWithPosition
import com.stranger_sparks.utils.SharedPreferenceManager
import com.stranger_sparks.view.activities.SignInSignUpActivity
import com.stranger_sparks.view.activities.ui.activities.account.gallery.GalleryImageZoomActivity
import com.stranger_sparks.view.activities.ui.activities.settings.SettingsActivity

class SavedGalleryImagesAdapter(
    var context: Context,
    val onItemClickListenerWithPosition: SavedGalareyImageOnItemClickListenerWithPosition
) : RecyclerView.Adapter<SavedGalleryImagesAdapter.ViewHolder>() {

    var dataList = emptyList<GalleryImagesResponse.Data>()
    internal fun setDataList(dataList: List<GalleryImagesResponse.Data>) {
        this.dataList = dataList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivSavedGalleryImage: ImageView
        var tvDelete: TextView

        init {
            ivSavedGalleryImage = itemView.findViewById(R.id.ivSavedGalleryImage)
            tvDelete = itemView.findViewById(R.id.tvDelete)

        }

    }

    //https://pasindulaksara.medium.com/recyclerview-with-grid-layout-in-kotlin-414d780c47ae
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedGalleryImagesAdapter.ViewHolder {

        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_saved_gallery_images, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedGalleryImagesAdapter.ViewHolder, position: Int) {

        var data = dataList[position]

        Glide.with(context)
            .load(BuildConfig.API_URL + "" + data.image)
            .dontAnimate()
            .into(holder.ivSavedGalleryImage);

        holder.tvDelete.setOnClickListener {

            onItemClickListenerWithPosition.clickOnCurrentPositionListener(data)
        }

        holder.ivSavedGalleryImage.setOnClickListener {
            val img: String = BuildConfig.API_URL + data.image
            val intent = Intent(context, GalleryImageZoomActivity::class.java)
            intent.putExtra("IMAGE_URL", img)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = dataList.size
}
