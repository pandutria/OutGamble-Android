package com.example.outgamble_android.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.outgamble_android.data.model.News
import com.example.outgamble_android.databinding.ItemNewsBinding

class NewsAdapter(
    private val list: MutableList<News> = mutableListOf(),
    private val onClick: (News) -> Unit
): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News, onClick: (News) -> Unit) {
            binding.tvTitle.text = news.title
            binding.tvDesc.text = news.desc

            Glide.with(binding.root.context)
                .load(news.image)
                .into(binding.image)

            binding.root.setOnClickListener {
                onClick(news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun set(data: List<News>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}