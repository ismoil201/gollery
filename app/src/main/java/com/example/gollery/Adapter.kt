package com.example.gollery

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class Adapter(): ListAdapter<ImageItems3, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ImageItems3>() {
        override fun areItemsTheSame(
            oldItem: ImageItems3,
            newItem: ImageItems3
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(
            oldItem: ImageItems3,
            newItem: ImageItems3
        ): Boolean {
            TODO("Not yet implemented")
        }

    }
){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

}

sealed class  ImageItems3 ()

data class  Image(val uri: Uri): ImageItems3()

object LoadMore: ImageItems3()