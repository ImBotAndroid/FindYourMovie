package com.dicky.findyourmovie.ui.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.local.MovieDataFavorite
import com.dicky.findyourmovie.data.local.MovieDataRating
import com.dicky.findyourmovie.data.response.ResultsItemRated
import com.dicky.findyourmovie.data.response.ResultsPopularFavoriteSimilar
import com.dicky.findyourmovie.databinding.ItemRecyclerViewBinding

class ItemSimilarMoviesAdapter (private val listItem: List<ResultsPopularFavoriteSimilar>, private val listItem2: List<ResultsItemRated>, private val listItem3: List<ResultsPopularFavoriteSimilar>): RecyclerView.Adapter<ItemSimilarMoviesAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id = listItem[position].id
        val idRated = listItem2.filter { it.id == id }
        val idFavorite = listItem3.filter { it.id == id }

        val title = listItem[position].title
        val imageUrl = BuildConfig.IMAGE_URL
        val description = listItem[position].overview
        val rating = listItem[position].voteAverage
        val poster = listItem[position].posterPath

        Glide.with(holder.itemView.context)
            .load(imageUrl + poster)
            .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_image_crash).centerInside())
            .into(holder.binding.ivPoster)

        holder.binding.tvDescription.text = description
        holder.binding.tvRating.text = String.format("%.1f", rating)
        holder.binding.pbPresentage.progress = rating.times(10).toInt()
        holder.binding.tvTitle.text = title

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            if (idRated.isNotEmpty()){
                idRated.forEach{
                    intent.putExtra(DetailActivity.EXTRA_DATA, MovieDataRating(id, it.rating))
                }
            } else {
                intent.putExtra(DetailActivity.EXTRA_DATA, MovieDataRating(id, 0))
            }

            if (idFavorite.isNotEmpty()){
                intent.putExtra(DetailActivity.EXTRA_VALUE, MovieDataFavorite(id, true))
            } else {
                intent.putExtra(DetailActivity.EXTRA_VALUE, MovieDataFavorite(id, false))
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}