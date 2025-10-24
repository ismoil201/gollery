package com.example.gollery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gollery.databinding.ItemImageBinding
import com.example.gollery.databinding.ItemImageMoreBinding

class TestAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<TestAdapter.ImageItems, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<ImageItems>() {
            override fun areItemsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {

                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ImageItems, newItem: ImageItems): Boolean {
                return oldItem == newItem
            }
        }
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_IMAGE -> {
                val binding = ItemImageBinding.inflate(
                    inflater, parent, false
                )
                ImageViewHolder(binding)
            }

            else -> {
                val binding = ItemImageMoreBinding.inflate(
                    inflater,
                    parent,
                    false
                )
                ItemMoreViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        when (holder) {
            is ImageViewHolder -> {
                holder.bind(currentList[position] as ImageItems.Image)
            }

            is ItemMoreViewHolder -> {
                holder.bind(itemClickListener)
            }
        }
    }


    override fun getItemCount(): Int {
        val originalSize = currentList.size
        return if (originalSize == 0) 0
        else originalSize + 1

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == currentList.size) ITEM_LOAD_MORE else ITEM_IMAGE
    }

    interface ItemClickListener {
        fun onItemClick()
    }


    companion object {
        const val ITEM_IMAGE = 0;
        const val ITEM_LOAD_MORE = 1;
    }

    class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageItems.Image) {
            binding.previewImageView.setImageURI(item.uri)
        }
    }

    class ItemMoreViewHolder(private val binding: ItemImageMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemClickListener: ItemClickListener) {

            itemView.setOnClickListener {
                itemClickListener.onItemClick()
            }
        }
    }

    sealed class ImageItems {
        data class Image(val uri: Uri) : ImageItems()
        object LoadMore : ImageItems()

    }
}