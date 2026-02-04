package com.stranger_sparks.adapterrs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mikhaellopez.circularimageview.CircularImageView
import com.stranger_sparks.R
import com.stranger_sparks.data_model.LikeLikedResponse
import com.stranger_sparks.data_model.WalletTransectionResponse
import com.stranger_sparks.view.activities.ui.activities.display_user.DisplayUserActivity

class LikeLikedAdapter(val context: Context) : RecyclerView.Adapter<LikeLikedAdapter.ViewHolder>() {

    //private lateinit var dataList: WalletTransectionResponse.Data
   var liketype: Int=1

     var dataList = emptyList<LikeLikedResponse.Data>()
    internal fun setDataList(dataList: List<LikeLikedResponse.Data>) {
        this.dataList = dataList
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvName: TextView
        var tvProfileDescription: TextView
        var ivProfleImage: CircularImageView


        init {
            tvName = itemView.findViewById(R.id.tvName)
            tvProfileDescription = itemView.findViewById(R.id.tvProfileDescription)
            ivProfleImage = itemView.findViewById(R.id.ivProfleImage)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeLikedAdapter.ViewHolder {

        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_dating_matches, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikeLikedAdapter.ViewHolder, position: Int) {

        var data = dataList.get(position)
        holder.tvName.text = data.name

        if(data.type==null)
            if(liketype==2)
            data.type="like"
                else if(liketype==3)
                data.type="liked"

        if (data.type.equals("liked")){
            holder.tvProfileDescription.text = data.name+ " liked your profile!"
        }else{
            holder.tvProfileDescription.text = "You Liked "+data.name+ " Profile!"
        }

        holder.itemView.setOnClickListener{
            Intent(context, DisplayUserActivity::class.java).also {
                it.putExtra("PROFILE_ID", data.id)
                context.startActivity(it)
            }
        }


        Glide.with(context).load(data.image)
            .error(R.drawable.img_placeholder)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(holder.ivProfleImage)

    }

    override fun getItemCount() = dataList.size
    fun setType(types: Int) {
        liketype=types
    }

}
