package com.stranger_sparks.adapterrs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stranger_sparks.R
import com.stranger_sparks.data_model.NotificationsResponse
import com.stranger_sparks.data_model.WalletTransectionResponse
import com.stranger_sparks.datamodels.HomeDataModel
import com.stranger_sparks.view.activities.ui.activities.display_user.DisplayUserActivity

class NotificationAdapter(private var context: Context) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private var dataList = mutableListOf<NotificationsResponse.Data>()

    internal fun setDataList(dataList: List<NotificationsResponse.Data>) {
        this.dataList = dataList.toMutableList()
        notifyDataSetChanged()
    }

    fun addDataList(newData: List<NotificationsResponse.Data>) {
        val startPosition = dataList.size
        dataList.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_notifications, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        val data = dataList[position]
        holder.tvTitle.text = data.title
        holder.tvDescription.text = data.body
        holder.tvDate.text = "${data.created_at?.split(" ")?.get(0)} - ${data.created_at?.split(" ")?.get(1)}"

        holder.itemView.setOnClickListener {
            Intent(context, DisplayUserActivity::class.java).also {
                it.putExtra("PROFILE_ID", data.profile_id.toString())
                context.startActivity(it)
            }
        }
    }

    override fun getItemCount() = dataList.size
}
