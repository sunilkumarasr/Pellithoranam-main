package com.stranger_sparks.adapterrs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.stranger_sparks.R
import com.stranger_sparks.data_model.WalletTransectionResponse

class WalletTransactionAdapter(var context: Context) : RecyclerView.Adapter<WalletTransactionAdapter.ViewHolder>() {

    //private lateinit var dataList: WalletTransectionResponse.Data

    var dataList = emptyList<WalletTransectionResponse.Data>()
    internal fun setDataList(dataList: List<WalletTransectionResponse.Data>) {
        this.dataList = dataList
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvPlanDetails: TextView
        var tvDate: TextView
        var tvPaymentType: TextView
        var tvAmount: TextView
        var tvStatus: TextView

        init {
            tvPlanDetails = itemView.findViewById(R.id.tvPlanDetails)
            tvDate = itemView.findViewById(R.id.tvDate)
            tvPaymentType = itemView.findViewById(R.id.tvPaymentType)
            tvAmount = itemView.findViewById(R.id.tvAmount)
            tvStatus = itemView.findViewById(R.id.tvStatus)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletTransactionAdapter.ViewHolder {
        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalletTransactionAdapter.ViewHolder, position: Int) {

        var data = dataList.get(position)
        holder.tvDate.text = data.created_at
        holder.tvPlanDetails.text = data.details
        holder.tvStatus.text = data.payment_status

        if(data.action.equals("+")){
            holder.tvAmount.text = "+ "+context.resources.getString(R.string.ruppe)+" "+data.amount.toString()
            holder.tvAmount.setTextColor(ContextCompat.getColor(context,R.color.purple_200))
        }else if(data.action.equals("-")){
            holder.tvAmount.text = "- "+context.resources.getString(R.string.ruppe)+" "+data.amount.toString()
            holder.tvAmount.setTextColor(ContextCompat.getColor(context,R.color.heilet_color))
        }else{
            holder.tvAmount.text = context.resources.getString(R.string.ruppe)+" "+data.amount.toString()
            holder.tvAmount.setTextColor(ContextCompat.getColor(context,R.color.black))
        }

        //holder.tvPaymentType.text = data.paymentType
    }

    override fun getItemCount() = dataList.size

}
