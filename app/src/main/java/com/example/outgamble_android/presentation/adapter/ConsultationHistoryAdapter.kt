package com.example.outgamble_android.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.outgamble_android.data.model.ConsultationHistory
import com.example.outgamble_android.databinding.ItemHistoryConsultationBinding

class ConsultationHistoryAdapter(
    private val list: MutableList<ConsultationHistory> = mutableListOf(),
    private val onClick: (ConsultationHistory) -> Unit
): RecyclerView.Adapter<ConsultationHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHistoryConsultationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(history: ConsultationHistory, onClick: (ConsultationHistory) -> Unit) {
            binding.tvName.text = history.doctor.name
            binding.tvLastMessage.text = history.lastMessage

            Glide.with(binding.root.context)
                .load(history.doctor.image)
                .into(binding.image)

            if (history.lastMessage == "") {
                binding.root.visibility = View.GONE
                binding.root.layoutParams.height = 0
            }

            binding.root.setOnClickListener {
                onClick(history)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryConsultationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun set(data: List<ConsultationHistory>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}