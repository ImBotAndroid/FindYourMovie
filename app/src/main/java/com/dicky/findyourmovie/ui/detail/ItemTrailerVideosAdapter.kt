package com.dicky.findyourmovie.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.response.ResultsItemTrailerVideos
import com.dicky.findyourmovie.databinding.ItemTrailerVideoBinding

class ItemTrailerVideosAdapter (private val listItem: List<ResultsItemTrailerVideos>): RecyclerView.Adapter<ItemTrailerVideosAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemTrailerVideoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTrailerVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val linkImage = "https://img.youtube.com/vi/" + listItem[position].key + "/0.jpg"
        val linkTrailer = "https://www.youtube.com/watch?v=" + listItem[position].key
        Glide.with(holder.itemView.context)
            .load(linkImage)
            .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_image_crash).centerCrop())
            .into(holder.binding.ivTrailer)

        holder.binding.cvTrailer.setOnClickListener {
            holder.itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(linkTrailer)))
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}