package com.example.outgamble_android.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.outgamble_android.data.model.ConsultationMessage
import com.example.outgamble_android.databinding.ItemBcConsultationReceiverBinding
import com.example.outgamble_android.databinding.ItemBcConsultationSenderBinding

class ChatConsultationAdapter(
    private val currUserId: String,
    private val list: MutableList<ConsultationMessage> = mutableListOf()
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    inner class senderViewHolder(val binding: ItemBcConsultationSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ConsultationMessage) {
            binding.tvChat.text = message.message
        }
    }

    inner class receivedViewHolder(val binding: ItemBcConsultationReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ConsultationMessage) {
            binding.tvChat.text = message.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = list[position]
        return if (message.senderId == currUserId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemBcConsultationSenderBinding.inflate(inflater, parent, false)
            senderViewHolder(binding)
        } else {
            val binding = ItemBcConsultationReceiverBinding.inflate(inflater, parent, false)
            receivedViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        when (holder) {
            is senderViewHolder -> holder.bind(message)
            is receivedViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun set(data: List<ConsultationMessage>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}