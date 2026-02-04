package com.stranger_sparks.adapterrs

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.stranger_sparks.BuildConfig
import com.stranger_sparks.R
import com.stranger_sparks.data_model.ChatResponse
import com.stranger_sparks.data_model.NotificationsResponse
import com.stranger_sparks.databinding.DeleteLayoutBinding
import com.stranger_sparks.databinding.ReceiveMsgBinding
import com.stranger_sparks.databinding.SendMsgBinding
import com.stranger_sparks.datamodels.Message
import com.stranger_sparks.inerfaces.OnItemClickListenerChatRoom
import com.stranger_sparks.inerfaces.OnItemClickListenerProfiles
import com.stranger_sparks.view.activities.ui.activities.chat.chat_room.ChatRoom

class ChatRoomAdapter (
    var context: Context,
    senderRoom: String,
    receiverRoom: String,
    val onItemClickListenerWithPosition: OnItemClickListenerChatRoom
) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    val ITEM_SENT = 1
    val ITEM_RECIVE = 2
    val senderRoom: String
    var receiverRoom: String
    var dataList = emptyList<ChatResponse.Data>()
    internal fun setDataList(dataList: List<ChatResponse.Data>) {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT) {
            val view = LayoutInflater.from(context).inflate(R.layout.send_msg, parent, false)
            SentMsgHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.receive_msg, parent, false)
            ReceiveMsgHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(dataList[position].sender_id == senderRoom){
            return ITEM_SENT
        }else{
            return ITEM_RECIVE
        }
    }
    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = dataList[position]
        var data = dataList[position]
        if (holder.javaClass == SentMsgHolder::class.java) {
            //if (message.senderId == "1") {
            val viewHolder = holder as SentMsgHolder
            if(!message.message.isNullOrBlank()){
                viewHolder.binding.message.text = message.message
                viewHolder.binding.image.visibility =  View.GONE
            }
            if (!message.image_url.isNullOrBlank()) {
                viewHolder.binding.image.visibility = View.VISIBLE

                viewHolder.binding.mLinear.visibility = View.GONE

                Log.v("Purushotham", message.image_url)
                Glide.with(context).load(message.image_url)
                    .error(R.drawable.ic_image_place_holder)
                    .into(viewHolder.binding.image)
            }

            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()
                binding.everyOne.setOnClickListener {
                    onItemClickListenerWithPosition.clickOnCurrentPositionListener(data,"both")
                    dialog.dismiss()
                }
                binding.delete.setOnClickListener {
                    onItemClickListenerWithPosition.clickOnCurrentPositionListener(data,"single")
                    dialog.dismiss()
                }
                binding.cancel.setOnClickListener { dialog.dismiss() }

                dialog.show()

                false
            }
        }else{
            val viewHolder = holder as ReceiveMsgHolder
            if(!message.message.isNullOrBlank()){
                viewHolder.binding.message.text = message.message
            }
            if (!message.image_url.isNullOrBlank()) {
                viewHolder.binding.image.visibility = View.VISIBLE
                viewHolder.binding.message.visibility = View.VISIBLE
                viewHolder.binding.mLinear.visibility = View.VISIBLE
                Glide.with(context).load(message.image_url)
                    .error(R.drawable.ic_image_place_holder)

                    .into(viewHolder.binding.image)

            }
            viewHolder.itemView.setOnLongClickListener {
                val view = LayoutInflater.from(context).inflate(R.layout.delete_layout, null)
                val binding: DeleteLayoutBinding = DeleteLayoutBinding.bind(view)
                val dialog = AlertDialog.Builder(context)
                    .setTitle("Delete Message")
                    .setView(binding.root)
                    .create()
                binding.everyOne.visibility = View.GONE

                binding.delete.setOnClickListener {
                    onItemClickListenerWithPosition.clickOnCurrentPositionListener(data,"single")
                    dialog.dismiss()
                }
                binding.cancel.setOnClickListener { dialog.dismiss() }

                dialog.show()
                false
            }
        }
    }

    inner class SentMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding: SendMsgBinding = SendMsgBinding.bind(itemView)
    }

    inner class ReceiveMsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding: ReceiveMsgBinding = ReceiveMsgBinding.bind(itemView)
    }

    init {
        if (dataList != null) {
            this.dataList = dataList
        }
        this.senderRoom = senderRoom
        this.receiverRoom = receiverRoom
    }


}