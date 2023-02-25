package com.dicky.findyourmovie.ui.detail.review

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.response.ResultsItemReviews
import com.dicky.findyourmovie.databinding.ItemReviewsBinding
import com.dicky.findyourmovie.ui.helper.FormatDate
import java.util.TimeZone

class ItemReviewsAdapter (private val listItem: List<ResultsItemReviews>): RecyclerView.Adapter<ItemReviewsAdapter.ViewHolder>() {
    inner class ViewHolder (val binding: ItemReviewsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = BuildConfig.IMAGE_URL
        val name = listItem[position].author
        val authorImage = listItem[position].authorDetails.avatarPath
        val authorRating = listItem[position].authorDetails.rating
        val review = listItem[position].content

        val date = if (listItem[position].updatedAt == listItem[position].createdAt){
            listItem[position].createdAt
        } else {
            listItem[position].updatedAt
        }

        if (authorImage == null){
            Glide.with(holder.itemView.context)
                .load(R.drawable.contoh_gambar)
                .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_account_white).centerInside())
                .circleCrop()
                .into(holder.binding.ivAvatar)
        } else if (authorImage.startsWith("/https://www.gravatar.com",false)){
            val splitString = authorImage.split("/")
            val result = splitString[splitString.size - 1]
            Glide.with(holder.itemView.context)
                .load("https://www.gravatar.com/avatar/$result")
                .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_account_white).centerInside())
                .circleCrop()
                .into(holder.binding.ivAvatar)
        } else {
            Glide.with(holder.itemView.context)
                .load(imageUrl + authorImage)
                .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_account_white).centerInside())
                .circleCrop()
                .into(holder.binding.ivAvatar)
        }



        holder.binding.apply {
            if (authorRating == null){
                cvRatingReviews.visibility = View.GONE
            } else {
                tvRatingReviews.text = authorRating.toString()
                iconStarReviews.setImageResource(R.drawable.icon_star)
            }

            tvAuthorName.text = name
            tvReview.text = review
            tvDate.text = FormatDate.formatDateLocale(date, TimeZone.getDefault().id)
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}