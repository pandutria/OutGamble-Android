package com.example.outgamble_android.presentation.adapter

import android.content.ClipData.Item
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.longSetOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.outgamble_android.data.model.Education
import com.example.outgamble_android.databinding.ItemEducationBinding

class EducationAdapter(
    private val list: MutableList<Education> = mutableListOf(),
    private val onClick: (Education) -> Unit
): RecyclerView.Adapter<EducationAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: ItemEducationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(education: Education, onClick: (Education) -> Unit) {
            binding.tvTitle.text = education.title
            binding.tvDesc.text = education.desc

            Glide.with(binding.root.context)
                .load(education.thumbnail)
                .into(binding.image)

            binding.root.setOnClickListener {
                onClick(education)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEducationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun set(data: List<Education>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}