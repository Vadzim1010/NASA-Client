package com.example.nasa.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.databinding.ItemErrorBinding

class ErrorViewHolder(
    private val binding: ItemErrorBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(throwable: Throwable) {
        binding.errorMessage.text = throwable.message ?: ""
    }
}