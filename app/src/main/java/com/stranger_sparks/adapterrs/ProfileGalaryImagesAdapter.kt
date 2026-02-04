package com.stranger_sparks.adapterrs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.stranger_sparks.BuildConfig
import com.stranger_sparks.R
import com.stranger_sparks.data_model.GalleryImagesResponse
import com.stranger_sparks.inerfaces.OnItemClickListenerProfilesGalleryImages
import com.stranger_sparks.view.activities.ui.activities.display_user.DisplayUserActivity

class ProfileGalaryImagesAdapter(
    var ctx: DisplayUserActivity, val onItemClickListenerWithPosition: OnItemClickListenerProfilesGalleryImages
) : RecyclerView.Adapter<ProfileGalleryImageViewHolder>() {

    private var dataList: MutableList<GalleryImagesResponse.Data> = arrayListOf()
    internal fun setDataList(dataList: List<GalleryImagesResponse.Data>) {
        this.dataList.addAll(dataList)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileGalleryImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_profile_gallery_image, parent, false)
        return ProfileGalleryImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileGalleryImageViewHolder, position: Int) {
        Glide.with(ctx).load(BuildConfig.API_URL+""+dataList[position].image)
            .error(R.drawable.img_placeholder)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onItemClickListenerWithPosition.clickOnCurrentPositionListener(dataList[position], position)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /* fun addSelectedImages(images: List<String>) {
         this.selectedImagesUri = images
         notifyDataSetChanged()
     }*/
}

class ProfileGalleryImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val image: ImageView = view.findViewById(R.id.imgSelected)


}