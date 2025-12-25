package com.example.outgamble_android.presentation.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.outgamble_android.data.model.Doctor
import com.example.outgamble_android.data.model.News
import com.example.outgamble_android.databinding.ItemDoctorBinding
import com.example.outgamble_android.util.RupiahHelper

class DoctorAdapter(
    private val list: MutableList<Doctor> = mutableListOf(),
    private val onClick: (Doctor) -> Unit
): RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemDoctorBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: Doctor, onClick: (Doctor) -> Unit) {
            binding.tvName.text = "Dr. ${doctor.name}"
            binding.tvPatient.text = "${doctor.patient} Pasien"
            binding.tvYear.text = "${doctor.year} Tahun"
            binding.tvPrice.setPaintFlags(0)
            binding.tvPrice.text = "Rp. ${RupiahHelper.format(doctor.price)}"
            binding.tvPrice.paintFlags = binding.tvPrice.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            Glide.with(binding.root.context)
                .load(doctor.image)
                .into(binding.image)

            binding.btnChat.setOnClickListener {
                onClick(doctor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun set(data: List<Doctor>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}