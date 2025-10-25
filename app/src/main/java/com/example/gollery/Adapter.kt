package com.example.gollery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gollery.databinding.ItemImageBinding
import com.example.gollery.databinding.ItemImageMoreBinding

class Adapter(var itemClickListener: ItemClickListener) : ListAdapter<ImageItems3, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ImageItems3>() {
        override fun areItemsTheSame(
            oldItem: ImageItems3,
            newItem: ImageItems3
        ): Boolean {

            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ImageItems3,
            newItem: ImageItems3
        ): Boolean {

            return oldItem == newItem
        }

    }
) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            IMAGE -> {
                val binding = ItemImageBinding.inflate(inflater, parent, false)
                ItemImageHolder(binding)
            }


            else -> {

                val binding = ItemImageMoreBinding.inflate(inflater, parent, false)
                ItemImageMoreHolder(binding)
            }
        }
    }


    override fun getItemCount(): Int {

        val originalSize  =  currentList.size

        return if(originalSize ==0 ) 0 else originalSize.inc()
    }

    override fun getItemViewType(position: Int): Int {

        return if(position == currentList.size) IMAGE_MORE else IMAGE
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ItemImageHolder -> holder.bind(getItem(position) as Image)

            is ItemImageMoreHolder -> holder.bind(itemClickListener)
        }
    }

    companion object {
        const val IMAGE = 0
        const val IMAGE_MORE = 1;
    }

    class ItemImageHolder(private var binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Image) {
            binding.previewImageView.setImageURI(item.uri)
        }
    }

    class ItemImageMoreHolder(binding: ItemImageMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemClickListener: ItemClickListener) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick()
            }

        }
    }

}

interface ItemClickListener {
    fun onItemClick()
}


sealed class ImageItems3()

data class Image(val uri: Uri) : ImageItems3()

object LoadMore : ImageItems3()