package com.stranger_sparks.adapterrs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.stranger_sparks.R
import com.stranger_sparks.data_model.CallHistoryDataResponse
import com.stranger_sparks.view.activities.ui.activities.display_user.DisplayUserActivity

class CallListAdapter(
    var context: Context
) : RecyclerView.Adapter<CallListAdapter.ViewHolder>() {

    var dataList = emptyList<CallHistoryDataResponse.CallData>()
    internal fun setDataList(dataList: List<CallHistoryDataResponse.CallData>) {
        this.dataList = dataList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtCallType: TextView
        var txtTitle: TextView
        var img: ImageView
        var txtType: TextView
        var txtTime: TextView
        var txtDuration: TextView

        init {
            txtCallType = itemView.findViewById(R.id.txtCallType)
            txtTitle = itemView.findViewById(R.id.txtTitle)
            img = itemView.findViewById(R.id.img)
            txtType = itemView.findViewById(R.id.txtType)
            txtTime = itemView.findViewById(R.id.txtTime)
            txtDuration = itemView.findViewById(R.id.txtDuration)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_call_list_screen, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        // Set safe default values
        holder.txtCallType.text = data.call_type ?: ""
        holder.txtTitle.text = data.name ?: ""
        holder.txtType.text = data.type ?: ""
        holder.txtTime.text = data.created_at ?: ""
        holder.txtDuration.text = ""

        // Set duration if valid
        if (!data.duration.isNullOrEmpty()) {
            holder.txtDuration.text = "Duration: ${data.duration}"
        }

        // Reset and set correct icon based on type
        val drawableRes = when (data.type) {
            "OutGoing Call" -> R.drawable.outgoing_call
            "Incoming Call" -> R.drawable.incoming_call
            "Missed Call" -> R.drawable.missed_call
            else -> R.drawable.outgoing_call // Optional: fallback icon
        }
        holder.img.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, drawableRes))

        // Item click to open DisplayUserActivity
        holder.itemView.setOnClickListener {
            Intent(context, DisplayUserActivity::class.java).also {
                it.putExtra("PROFILE_ID", data.caller_id.toString())
                context.startActivity(it)
            }
        }
    }


    override fun getItemCount() = dataList.size

}