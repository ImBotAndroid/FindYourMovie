package com.dicky.findyourmovie.ui.bookmark

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.local.DataId
import com.dicky.findyourmovie.data.local.MovieDataFavorite
import com.dicky.findyourmovie.data.local.MovieDataRating
import com.dicky.findyourmovie.data.response.ResultsPopularFavoriteSimilar
import com.dicky.findyourmovie.data.response.ResultsItemRated
import com.dicky.findyourmovie.databinding.ItemRecyclerViewBinding
import com.dicky.findyourmovie.ui.detail.DetailActivity
import com.dicky.findyourmovie.ui.detail.review.ReviewActivity

class ItemRatedMoviesAdapter(private val listItem: List<ResultsItemRated>, private val listItem2: List<ResultsPopularFavoriteSimilar>): RecyclerView.Adapter<ItemRatedMoviesAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val id = listItem[position].id
        val title = listItem[position].title
        val overview = listItem[position].overview
        val rating = listItem[position].rating
        val image = listItem[position].posterPath
        val imageUrl = BuildConfig.IMAGE_URL

        val bundle = Bundle()
        val idRated = listItem2.filter { it.id == id}

        if (idRated.isNotEmpty()){
            bundle.putParcelable(DetailActivity.EXTRA_VALUE, MovieDataFavorite(id, true))
        } else {
            bundle.putParcelable(DetailActivity.EXTRA_VALUE, MovieDataFavorite(id, false))
        }

        Glide.with(holder.itemView.context)
            .load(imageUrl + image)
            .into(holder.binding.ivPoster)

        holder.binding.tvTitle.text = title
        holder.binding.tvDescription.text = overview
        holder.binding.tvRating.text = rating.toString()
        holder.binding.pbPresentage.progress = rating.times(10)

        holder.itemView.setOnClickListener {
            bundle.putParcelable(DetailActivity.EXTRA_DATA, MovieDataRating(id, rating))
            it.findNavController().navigate(R.id.action_navigation_bookmark_to_navigation_detail, bundle)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun getItemCount(): Int {
        return listItem.size
    }
}