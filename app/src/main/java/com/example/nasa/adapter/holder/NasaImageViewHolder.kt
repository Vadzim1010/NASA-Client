package com.example.nasa.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nasa.databinding.ItemNasaImageBinding
import com.example.nasa.domain.model.NasaImage

class NasaImageViewHolder(
    private val binding: ItemNasaImageBinding,
    private val onCardClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(image: NasaImage) {
        with(binding) {
            nasaImage.load(image.imageUrl) {
                crossfade(true)
                crossfade(500)
            }
            card.setOnClickListener {
                onCardClicked(image.id)
            }
        }
    }
}