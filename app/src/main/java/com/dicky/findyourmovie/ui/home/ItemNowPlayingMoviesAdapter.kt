package com.dicky.findyourmovie.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.local.MovieDataFavorite
import com.dicky.findyourmovie.data.local.MovieDataRating
import com.dicky.findyourmovie.data.response.ResultsPopularFavoriteSimilar
import com.dicky.findyourmovie.data.response.ResultsItemRated
import com.dicky.findyourmovie.data.response.ResultsItemUpcomingAndNowPlaying
import com.dicky.findyourmovie.databinding.ItemNowPlayingBinding
import com.dicky.findyourmovie.ui.detail.DetailActivity

class ItemNowPlayingMoviesAdapter(private val listItem: List<ResultsItemUpcomingAndNowPlaying>, private val listItem2: List<ResultsItemRated>, private val listItem3: List<ResultsPopularFavoriteSimilar>): RecyclerView.Adapter<ItemNowPlayingMoviesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemNowPlayingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNowPlayingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bundle = Bundle()
        val id = listItem[position].id
        val idRated = listItem2.filter { it.id == id }
        val idFavorite = listItem3.filter { it.id == id }

        if (idRated.isNotEmpty()){
            idRated.forEach{ value ->
                bundle.putParcelable(DetailActivity.EXTRA_DATA, MovieDataRating(id, value.rating))
            }
        } else {
            bundle.putParcelable(DetailActivity.EXTRA_DATA, MovieDataRating(id, 0))
        }

        listItem3.forEach{ _ ->
            if (idFavorite.isNotEmpty()){
                bundle.putParcelable(DetailActivity.EXTRA_VALUE, MovieDataFavorite(id, true))
            } else {
                bundle.putParcelable(DetailActivity.EXTRA_VALUE, MovieDataFavorite(id, false))
            }
        }

        val title = listItem[position].title
        val imageUrl = BuildConfig.IMAGE_URL
        val description = listItem[position].overview
        val poster = listItem[position].posterPath
        val backdrop = listItem[position].backdropPath

        Glide.with(holder.itemView.context)
            .load(imageUrl + poster)
            .placeholder(R.drawable.background_crash_image)
            .into(holder.binding.ivPosterNowPlaying)

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original/$backdrop")
            .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_image_crash).centerCrop().fitCenter())
            .override(1000, 1000)
            .into(holder.binding.ivBackdropNowPlaying)

        holder.binding.tvItemDescriptionUpcoming.text = description
        holder.binding.tvItemTitleUpcoming.text = title

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_home_to_navigation_detail, bundle)
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}