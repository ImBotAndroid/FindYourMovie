package com.dicky.findyourmovie.ui.detail

import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.response.CastItem
import com.dicky.findyourmovie.databinding.ItemCreditsBinding

class ItemCreditsAdapter(private val listItem: List<CastItem>): RecyclerView.Adapter<ItemCreditsAdapter.ViewHolder>() {
    class ViewHolder (val binding: ItemCreditsBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCreditsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = "https://image.tmdb.org/t/p/original/" + listItem[position].profilePath
        val name = listItem[position].name
        val character = listItem[position].character

        Glide.with(holder.itemView.context)
            .load(image)
            .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_image_crash).centerCrop())
            .into(holder.binding.ivCredits)

        holder.binding.tvActorName.text = name
        holder.binding.tvCharacter.text = character

        if (holder.binding.tvActorName.text.length <= 19) {
            holder.binding.tvActorName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        } else if (holder.binding.tvActorName.text.length in 20..25) {
            holder.binding.tvActorName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        } else {
            holder.binding.tvActorName.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                setLines(1)
                ellipsize = TextUtils.TruncateAt.END
            }
        }

        if (holder.binding.tvCharacter.text.length <= 19) {
            holder.binding.tvCharacter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        } else if (holder.binding.tvCharacter.text.length in 20..25) {
            holder.binding.tvCharacter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        } else {
            holder.binding.tvCharacter.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                setLines(1)
                ellipsize = TextUtils.TruncateAt.END
            }
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}