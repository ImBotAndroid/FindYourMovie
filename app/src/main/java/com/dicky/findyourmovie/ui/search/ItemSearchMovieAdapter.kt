package com.dicky.findyourmovie.ui.search

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
import com.dicky.findyourmovie.data.local.MovieDataRating
import com.dicky.findyourmovie.data.response.ResultsItemRated
import com.dicky.findyourmovie.data.response.ResultsItemSearch
import com.dicky.findyourmovie.databinding.ItemRecyclerViewBinding
import com.dicky.findyourmovie.ui.detail.DetailActivity

class ItemSearchMovieAdapter(private val listItem: List<ResultsItemSearch>, private val listItem2: List<ResultsItemRated>): RecyclerView.Adapter<ItemSearchMovieAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemRecyclerViewBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bundle = Bundle()

        val id = listItem[position].id
        val idRated = listItem2.filter { it.id == id }

        if (idRated.isNotEmpty()){
            idRated.forEach{ value ->
                bundle.putParcelable(DetailActivity.EXTRA_DATA, MovieDataRating(id, value.rating))
            }
        } else {
            bundle.putParcelable(DetailActivity.EXTRA_DATA, MovieDataRating(id, 0))
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
            .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_image_crash).centerInside())
            .into(holder.binding.ivPoster)

        holder.binding.tvDescription.text = description
        holder.binding.tvRating.text = rating.toString()
        holder.binding.pbPresentage.progress = rating.times(10).toInt()
        holder.binding.tvTitle.text = title

        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_search_to_navigation_detail, bundle)
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}