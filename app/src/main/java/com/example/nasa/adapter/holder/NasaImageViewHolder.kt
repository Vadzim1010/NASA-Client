package com.example.nasa.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.example.nasa.databinding.ItemNasaImageBinding
import com.example.nasa.domain.model.NasaImage

class NasaImageViewHolder(
    private val binding: ItemNasaImageBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(image: NasaImage) {
        with(binding) {
            nasaImage.load(image.imageUrl) {
                scale(Scale.FILL)
                size(ViewSizeResolver(root))
            }
            card.setOnClickListener {
                onClickListener(image.id)
            }
        }
    }
}