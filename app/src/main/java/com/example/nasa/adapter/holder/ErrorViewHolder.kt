package com.example.nasa.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.databinding.ItemErrorBinding

class ErrorViewHolder(
    private val binding: ItemErrorBinding,
    private val onReloadButtonClicked: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(throwable: Throwable) {
        with(binding) {
            errorMessage.text = throwable.message ?: ""
            reloadButton.setOnClickListener {
                onReloadButtonClicked()
            }
        }
    }
}