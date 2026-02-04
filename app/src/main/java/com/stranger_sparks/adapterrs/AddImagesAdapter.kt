package com.stranger_sparks.adapterrs
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stranger_sparks.R
import com.stranger_sparks.view.activities.ui.activities.account.add_images.AddImagesActivity

import java.io.File

class AddImagesAdapter(
    var ctx: AddImagesActivity,
    var selectedImagesUri: MutableList<Uri>,
    var selectedImagesFile: MutableList<File>,
    val beforeSaveImageItemSelect: AddImagesActivity
) : RecyclerView.Adapter<ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_add_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.tvStatus.visibility = View.GONE
        holder.tvDelete.visibility = View.VISIBLE
        holder.image.setImageURI(selectedImagesUri[position])
        holder.filename.text = selectedImagesFile[position].name

        holder.tvDelete.setOnClickListener {
            beforeSaveImageItemSelect.singleItemSelect(selectedImagesUri[position], position,
            selectedImagesFile[position], position)
        }

        Log.v("Purushotham", "--- "+selectedImagesFile[position].absolutePath.toString())
    }

    override fun getItemCount(): Int {
        return selectedImagesUri.size
    }

   /* fun addSelectedImages(images: List<String>) {
        this.selectedImagesUri = images
        notifyDataSetChanged()
    }*/
}

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val image: ImageView = view.findViewById(R.id.imgSelected)
    val filename: TextView = view.findViewById(R.id.tvSelected)
    val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    val tvDelete: TextView = view.findViewById(R.id.tvDelete)

}