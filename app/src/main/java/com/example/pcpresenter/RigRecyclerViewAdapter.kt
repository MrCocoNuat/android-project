package com.example.pcpresenter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.DrawFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.pcpresenter.databinding.FragmentRigFeedBinding
import java.security.MessageDigest


class RigRecyclerViewAdapter(val rigs: List<Rig>) : RecyclerView.Adapter<RigRecyclerViewAdapter.ViewHolder>() {

    private val multiTransformation = MultiTransformation(CenterCrop(), RoundedCorners(30))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentRigFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rig = rigs[position]
        holder.nameView.text = rig.getName()
        holder.uploaderView.text = rig.getUploader().username
        Glide.with(holder.photoView)
            .load(rig.getPhoto().file)
            .transform(multiTransformation)
            .into(holder.photoView)

    }

    override fun getItemCount(): Int = rigs.size

    inner class ViewHolder(binding: FragmentRigFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val nameView: TextView = binding.tvRigName
            val uploaderView: TextView = binding.tvRigUploader
            val timestampView: TextView = binding.tvRigTimestamp
            val photoView: ImageView = binding.ivRigImage
    }

}