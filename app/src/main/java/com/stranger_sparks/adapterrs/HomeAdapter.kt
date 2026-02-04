package com.stranger_sparks.adapterrs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.stranger_sparks.R
import com.stranger_sparks.data_model.UserProfileResponse
import com.stranger_sparks.inerfaces.OnItemClickListenerProfiles


class HomeAdapter(
    var context: Context,
    val onItemClickListenerWithPosition: OnItemClickListenerProfiles
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var dataList = emptyList<UserProfileResponse.Data>()
    internal fun setDataList(dataList: List<UserProfileResponse.Data>) {
        this.dataList = dataList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: AppCompatImageView
        var ivStatus: ImageView
        var title: TextView
        var tvAge_year: TextView
        var tvLocation: TextView
        var tvImageCount: TextView

        init {
            image = itemView.findViewById(R.id.img_item_topGallery)
            ivStatus = itemView.findViewById(R.id.ivStatus)
            title = itemView.findViewById(R.id.tvName)
            tvAge_year = itemView.findViewById(R.id.tvAge_year)
            tvLocation = itemView.findViewById(R.id.tvLocation)
            tvImageCount = itemView.findViewById(R.id.tvImagesCount)
        }

    }

    //https://pasindulaksara.medium.com/recyclerview-with-grid-layout-in-kotlin-414d780c47ae
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {

        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_home_screen, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {

        var data = dataList[position]
        holder.title.text = data.name
        holder.tvAge_year.text = data.age+"/"+ data.gender
        holder.tvLocation.text = data.location
        holder.tvImageCount.text = data.image_count.toString()
        if(data.is_online == 1){
            holder.ivStatus.setBackgroundResource(R.drawable.ic_online_green)
        }else{
            holder.ivStatus.setBackgroundResource(R.drawable.ic_offline)
        }
        Glide.with(context).load(data.image)
            .error(R.drawable.img_placeholder)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(holder.image)
        holder.itemView.setOnClickListener {
            onItemClickListenerWithPosition.clickOnCurrentPositionListener(data)
        }
    }

    override fun getItemCount() = dataList.size

}
