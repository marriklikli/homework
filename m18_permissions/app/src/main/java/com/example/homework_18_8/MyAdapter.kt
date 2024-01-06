package com.example.homework_18_8

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.homework_18_8.databinding.AdapterBinding

class MyAdapter : RecyclerView.Adapter<PhotoHolder>() {
    private var data: List<DataPhoto> = emptyList()
    fun setData(data: List<DataPhoto>) {
        this.data = data
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val binding = AdapterBinding.inflate(LayoutInflater.from(parent.context))
        return PhotoHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val item = data[position]
        with(holder.binding) {
           newImage.setImageURI(Uri.parse(item.uri))
        }
    }


}

class PhotoHolder(val binding: AdapterBinding): ViewHolder(binding.root)
