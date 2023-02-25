package com.dicky.findyourmovie.ui.home

import android.annotation.SuppressLint
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
import com.dicky.findyourmovie.data.response.ResultsItemRated
import com.dicky.findyourmovie.data.response.ResultsItemUpcomingAndNowPlaying
import com.dicky.findyourmovie.data.response.ResultsPopularFavoriteSimilar
import com.dicky.findyourmovie.databinding.ItemRecyclerViewBinding
import com.dicky.findyourmovie.ui.detail.DetailActivity

class ItemUpcomingMoviesAdapter(private val listItem: List<ResultsItemUpcomingAndNowPlaying>, private val listItem2: List<ResultsItemRated>, private val listItem3: List<ResultsPopularFavoriteSimilar>): RecyclerView.Adapter<ItemUpcomingMoviesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bundle = Bundle()

        val id = listItem[position].id
        val idRated = listItem2.filter { it.id == id }
        val idFavorite = listItem3.filter { it.id == id }

        if (idRated.isNotEmpty()){
            idRated.forEach { value ->
                bundle.putParcelable(DetailActivity.EXTRA_DATA, MovieDataRating(id, value.rating))
            }
        } else {
            bundle.putParcelable(DetailActivity.EXTRA_DATA, MovieDataRating(id, 0))
        }

        if (idFavorite.isNotEmpty()){
            bundle.putParcelable(DetailActivity.EXTRA_VALUE, MovieDataFavorite(id, true))
        } else {
            bundle.putParcelable(DetailActivity.EXTRA_VALUE, MovieDataFavorite(id, false))
        }

        val title = listItem[position].title
        val imageUrl = BuildConfig.IMAGE_URL
        val description = listItem[position].overview
        val rating = listItem[position].voteAverage
        val poster = listItem[position].posterPath

        when (rating) {
            in 0.0f..3.3f -> {
                holder.binding.pbPresentage.progressDrawable = holder.itemView.context.getDrawable(R.drawable.rating_precentage_red)
            }
            in 3.31f..6.61f -> {
                holder.binding.pbPresentage.progressDrawable = holder.itemView.context.getDrawable(R.drawable.rating_precentage_yellow)
            }
            else -> {
                holder.binding.pbPresentage.progressDrawable = holder.itemView.context.getDrawable(R.drawable.rating_precentage_green)
            }
        }

        Glide.with(holder.itemView.context)
            .load(imageUrl + poster)
            .apply(RequestOptions.placeholderOf(R.drawable.background_crash_image).error(R.drawable.background_crash_image))
            .into(holder.binding.ivPoster)

        holder.binding.tvDescription.text = description
        holder.binding.tvRating.text = rating.toString()
        holder.binding.pbPresentage.progress = rating.times(10).toInt()
        holder.binding.tvTitle.text = title

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_home_to_navigation_detail, bundle)
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}