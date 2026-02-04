package com.stranger_sparks.adapterrs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.stranger_sparks.R
import com.stranger_sparks.data_model.SubscriptionPlansDTO
import com.stranger_sparks.inerfaces.OnItemClickListenerWithPosition

class PlansAdapter(
    var context: Context,
    val onItemClickListenerWithPosition: OnItemClickListenerWithPosition
) : RecyclerView.Adapter<PlansAdapter.ViewHolder>() {

    private var dataList: MutableList<SubscriptionPlansDTO.Data> = arrayListOf()
    private var selectedPosition = RecyclerView.NO_POSITION

    internal fun setDataList(dataList: MutableList<SubscriptionPlansDTO.Data>) {
        this.dataList.clear() // Clear the existing data
        this.dataList.addAll(dataList) // Add new data
        notifyDataSetChanged() // Notify the adapter to refresh the views
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPlanValid: TextView = itemView.findViewById(R.id.tvPlanValid)
        var tvPlanFeatures: TextView = itemView.findViewById(R.id.tvPlanFeatures)
        var llBackground: LinearLayout = itemView.findViewById(R.id.llBackground)
        var plan_title: TextView = itemView.findViewById(R.id.plan_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlansAdapter.ViewHolder {
        // Inflate the custom layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_subscription_package, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlansAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val data = dataList[position]
        holder.tvPlanValid.text = "Rs: "+data.price
        holder.tvPlanFeatures.text = data.features

        // Set the background based on the selected position
        if (position == selectedPosition) {
            holder.llBackground.setBackgroundResource(R.drawable.selected_item)
        } else {
            holder.llBackground.setBackgroundResource(R.drawable.unselected_item)
        }

        holder.itemView.setOnClickListener {
            // Update the selected position
            val previousSelectedPosition = selectedPosition
            selectedPosition = position

            // Notify the adapter to refresh the old and new selected positions
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)

            // Trigger the click listener
            onItemClickListenerWithPosition.clickOnCurrentPositionListener(position)
        }
    }

    override fun getItemCount() = dataList.size


}

