package com.example.outgamble_android.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.outgamble_android.data.model.Test
import com.example.outgamble_android.databinding.ItemTestBinding
import com.example.outgamble_android.presentation.test.input.TestInputActivity

class TestAdapter(
    val activity: TestInputActivity,
    val list: MutableList<Test> = mutableListOf()
): RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTestBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(test: Test) {
            binding.tvQuestion.text = test.question
            binding.rbAnswer1.text = test.answer1
            binding.rbAnswer2.text = test.answer2
            binding.rbAnswer3.text = test.answer3
            binding.rbAnswer4.text = test.answer4

            binding.rgAnswer.setOnCheckedChangeListener(null)

            when (test.poin) {
                1 -> binding.rbAnswer1.isChecked = true
                2 -> binding.rbAnswer2.isChecked = true
                3 -> binding.rbAnswer3.isChecked = true
                4 -> binding.rbAnswer4.isChecked = true
                else -> binding.rgAnswer.clearCheck()
            }

            binding.rgAnswer.setOnCheckedChangeListener { _, checkedId ->
                test.poin = when (checkedId) {
                    binding.rbAnswer1.id -> 1
                    binding.rbAnswer2.id -> 2
                    binding.rbAnswer3.id -> 3
                    binding.rbAnswer4.id -> 4
                    else -> 0
                }

                activity.countTotal()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(data: List<Test>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}