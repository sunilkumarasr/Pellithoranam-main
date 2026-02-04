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

class DatingMatchesAdapter(var context: Context) : RecyclerView.Adapter<DatingMatchesAdapter.ViewHolder>() {

    var dataList = emptyList<TransactionDataModel>()
    internal fun setDataList(dataList: List<TransactionDataModel>) {
        this.dataList = dataList
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView
        /*var tvPaymentType: TextView
        var tvAmount: TextView
        var tvStatus: TextView*/

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
           /* tvPaymentType = itemView.findViewById(R.id.tvPaymentType)
            tvAmount = itemView.findViewById(R.id.tvAmount)
            tvStatus = itemView.findViewById(R.id.tvStatus)*/

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingMatchesAdapter.ViewHolder {

        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_dating_matches, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DatingMatchesAdapter.ViewHolder, position: Int) {

        var data = dataList[position]
        holder.tvTitle.text = data.date
        /*holder.tvAmount.text = data.amount
        holder.tvStatus.text = data.status
        holder.tvPaymentType.text = data.paymentType*/
    }
    override fun getItemCount() = dataList.size
}
