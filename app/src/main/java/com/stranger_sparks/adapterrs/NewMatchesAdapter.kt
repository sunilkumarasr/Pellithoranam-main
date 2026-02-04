package com.stranger_sparks.adapterrs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stranger_sparks.R
import com.stranger_sparks.datamodels.HomeDataModel
import com.stranger_sparks.datamodels.TransactionDataModel

class NewMatchesAdapter(var context: Context) : RecyclerView.Adapter<NewMatchesAdapter.ViewHolder>() {

    var dataList = emptyList<TransactionDataModel>()
    internal fun setDataList(dataList: List<TransactionDataModel>) {
        this.dataList = dataList
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvName: TextView
        /*var tvPaymentType: TextView
        var tvAmount: TextView
        var tvStatus: TextView*/

        init {
            tvName = itemView.findViewById(R.id.tvName)
           /* tvPaymentType = itemView.findViewById(R.id.tvPaymentType)
            tvAmount = itemView.findViewById(R.id.tvAmount)
            tvStatus = itemView.findViewById(R.id.tvStatus)*/

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewMatchesAdapter.ViewHolder {

        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_new_matches, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewMatchesAdapter.ViewHolder, position: Int) {

        var data = dataList[position]
        holder.tvName.text = data.date
        /*holder.tvAmount.text = data.amount
        holder.tvStatus.text = data.status
        holder.tvPaymentType.text = data.paymentType*/
    }
    override fun getItemCount() = dataList.size
}
