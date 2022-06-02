package com.example.nasa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.adapter.holder.LoadingViewHolder
import com.example.nasa.adapter.holder.NasaImageViewHolder
import com.example.nasa.databinding.ItemLoadingBinding
import com.example.nasa.databinding.ItemNasaImageBinding
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.PagingItem

class NasaImagesAdapter(
    context: Context,
    private val onClickListener: (String) -> Unit,
) : ListAdapter<PagingItem<NasaImage>, RecyclerView.ViewHolder>(DIF_UTIL) {


    private val layoutInflater = LayoutInflater.from(context)


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagingItem.Content -> TYPE_CONTENT
            is PagingItem.Error -> TYPE_ERROR
            PagingItem.Loading -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CONTENT -> {
                NasaImageViewHolder(
                    ItemNasaImageBinding.inflate(layoutInflater, parent, false),
                    onClickListener,
                )
            }
            TYPE_LOADING -> {
                LoadingViewHolder(
                    ItemLoadingBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> {
                error("unknown type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_CONTENT -> {
                val typedHolder = holder as? NasaImageViewHolder ?: return
                val typedItem = getItem(position) as? PagingItem.Content ?: return
                typedHolder.bind(typedItem)
            }
        }
    }


    companion object {

        private const val TYPE_CONTENT = 0
        private const val TYPE_ERROR = 1
        private const val TYPE_LOADING = 2


        val DIF_UTIL = object : DiffUtil.ItemCallback<PagingItem<NasaImage>>() {
            override fun areItemsTheSame(
                oldItem: PagingItem<NasaImage>,
                newItem: PagingItem<NasaImage>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PagingItem<NasaImage>,
                newItem: PagingItem<NasaImage>
            ): Boolean {
                val oldContent = oldItem as? PagingItem.Content ?: return false
                val newContent = newItem as? PagingItem.Content ?: return false
                return oldContent.data.imageUrl == newContent.data.imageUrl
            }
        }
    }
}