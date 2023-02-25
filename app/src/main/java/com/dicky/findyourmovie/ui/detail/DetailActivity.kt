package com.dicky.findyourmovie.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.local.*
import com.dicky.findyourmovie.data.response.*
import com.dicky.findyourmovie.databinding.ActivityDetailBinding
import com.dicky.findyourmovie.ui.ViewModelFactory
import com.dicky.findyourmovie.ui.detail.review.ReviewActivity
import com.dicky.findyourmovie.ui.helper.FormatCurrency
import java.util.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    private lateinit var userPreferences: UserPreferences
    val apiKey = BuildConfig.API_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataRating = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DATA, MovieDataRating::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        val dataFavorite = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_VALUE, MovieDataFavorite::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_VALUE)
        }

        setUpView()
        setUpViewModel(dataRating, dataFavorite)
    }



    private fun setUpViewModel(dataRating: MovieDataRating?, dataFavorite: MovieDataFavorite?) {

        userPreferences = UserPreferences(this)
        val sessionId = userPreferences.getSessionId().sessionId
        val accountId = userPreferences.getAccountId().id

        detailViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[DetailViewModel::class.java]

        val idRating = dataRating?.id

        detailViewModel.apply {
            getDetailMovies(idRating, apiKey).observe(this@DetailActivity) { detail ->
                setUpData(detail, dataRating, dataFavorite)
            }
            getVideoTrailer(idRating, apiKey).observe(this@DetailActivity) {
                val constraint = ConstraintSet()
                if (it.results.isEmpty()){
                    binding.tvTrailerMoviesNull.visibility = View.VISIBLE
                    constraint.clone(binding.containerDetail)
                    constraint.connect(R.id.tv_title_actor, ConstraintSet.START, R.id.tv_trailer_movies_null, ConstraintSet.START, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt())
                    constraint.connect(R.id.tv_title_actor, ConstraintSet.END, R.id.tv_trailer_movies_null, ConstraintSet.END, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt())
                    constraint.connect(R.id.tv_title_actor, ConstraintSet.TOP, R.id.tv_trailer_movies_null, ConstraintSet.BOTTOM, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt())
                    constraint.applyTo(binding.containerDetail)
                } else {
                    showRecyclerViewVideoTrailer(it.results)
                    binding.tvTrailerMoviesNull.visibility = View.GONE
                }
            }
            getCredits(idRating, apiKey).observe(this@DetailActivity) {
                showRecyclerViewCredit(it)
            }
            getSimilarMovies(idRating, apiKey).observe(this@DetailActivity) {
                getRatedMovies(accountId, apiKey, sessionId).observe(this@DetailActivity) { listRated ->
                    getFavoriteMovies(accountId, apiKey, sessionId).observe(this@DetailActivity){ listFavorite ->
                        if (it.isEmpty()){
                            binding.tvSimilarMoviesNull.visibility = View.VISIBLE
                        } else {
                            binding.tvSimilarMoviesNull.visibility = View.GONE
                            showRecyclerViewSimilarMovies(it, listRated, listFavorite)
                        }

                    }
                }
            }
        }
    }

    private fun sendData(id: Int?) {
        val bundle = Bundle()
        bundle.putParcelable(ReviewActivity.EXTRA_DATA, DataId(id))
        Log.e("TAG", "Detail: $id, ${DataId(id)}" )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerViewSimilarMovies(list: List<ResultsPopularFavoriteSimilar>, listRated: List<ResultsItemRated>, listFavorite: List<ResultsPopularFavoriteSimilar>) {
        val listItemAdapter = ItemSimilarMoviesAdapter(list, listRated, listFavorite)
        binding.rvSimilar.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listItemAdapter
            setHasFixedSize(true)
        }
        listItemAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerViewCredit(data: List<CastItem>) {
        val listItemAdapter = ItemCreditsAdapter(data)
        binding.rvCredits.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listItemAdapter
            setHasFixedSize(true)
        }
        listItemAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerViewVideoTrailer(data: List<ResultsItemTrailerVideos>) {
        val listItemAdapter = ItemTrailerVideosAdapter(data)
        binding.rvTrailerMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listItemAdapter
            setHasFixedSize(true)
        }
        listItemAdapter.notifyDataSetChanged()
    }

    private fun setUpData(detail: DetailMoviesResponse, dataRating: MovieDataRating?, dataFavorite: MovieDataFavorite?) {

        val imageUrl = BuildConfig.IMAGE_URL

        val revenue = FormatCurrency.formatCurrencyUS(detail.revenue)
        binding.tvTitle.text = detail.title
        binding.tvStatus.text = detail.status
        binding.tvRevenue.text = revenue
        binding.tvRating.text = String.format("%.1f", detail.voteAverage)
        binding.pbPresentage.progress = detail.voteAverage.toInt().times(10)
        if (detail.overview.isEmpty()){
            val constraint = ConstraintSet()
            binding.tvSinopsisMoviesNull.visibility = View.VISIBLE
            constraint.clone(binding.containerDetail)
            constraint.connect(R.id.tv_title_trailer, ConstraintSet.START, R.id.tv_sinopsis_movies_null, ConstraintSet.START, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt())
            constraint.connect(R.id.tv_title_trailer, ConstraintSet.END, R.id.tv_sinopsis_movies_null, ConstraintSet.END, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt())
            constraint.connect(R.id.tv_title_trailer, ConstraintSet.TOP, R.id.tv_sinopsis_movies_null, ConstraintSet.BOTTOM, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt())
            constraint.applyTo(binding.containerDetail)
        } else {
            binding.tvSinopsis.text = detail.overview
            binding.tvSinopsisMoviesNull.visibility = View.GONE
        }

        Glide.with(this)
            .load(imageUrl + detail.backdropPath)
            .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_image_crash).centerCrop())
            .into(binding.ivBackdropDetail)

        Glide.with(this)
            .load(imageUrl + detail.posterPath)
            .into(binding.ivPosterDetail)

        binding.btnRatingDetail.setOnClickListener(this)
        binding.btnFavoriteDetail.setOnClickListener {
            postFavoriteMovies(dataRating, dataFavorite)
        }

        val favorite = dataFavorite?.isFavorite
        if (favorite == true){
            binding.btnFavoriteDetail.setImageResource(R.drawable.icon_favorite)
        } else {
            binding.btnFavoriteDetail.setImageResource(R.drawable.icon_favorite_border)
        }

        val rating = dataRating?.rating
        if (rating != 0){
            binding.btnRatingDetail.setImageResource(R.drawable.icon_star)
            binding.ratingBarDetail.rating = rating?.toFloat()?.div(2)!!
        } else {
            binding.btnRatingDetail.setImageResource(R.drawable.icon_star_border)
        }
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.btn_rating_detail -> {
                binding.containerRatingBar.visibility = View.VISIBLE

                binding.btnCloseRating.setOnClickListener {

                    binding.containerRatingBar.visibility = View.GONE

                    postRatingMovies()
                }
            }
        }
    }

    private fun postFavoriteMovies(dataRating: MovieDataRating?, dataFavorite: MovieDataFavorite?) {
        userPreferences = UserPreferences(this)
        val sessionId = userPreferences.getSessionId().sessionId
        val accountId = userPreferences.getAccountId().id

        val movieId = dataRating?.id

        if (dataFavorite?.isFavorite == true){
            val postData = PostData("movie", movieId!!, false)
            detailViewModel.postFavoriteMovies(accountId, apiKey, sessionId, postData).observe(this) {
                if (it.success) {
                    binding.btnFavoriteDetail.setImageResource(R.drawable.icon_favorite_border)
                    dataFavorite.isFavorite = false
                }
            }
        } else {
            val postData = PostData("movie", movieId!!, true)
            detailViewModel.postFavoriteMovies(accountId, apiKey, sessionId, postData).observe(this) {
                if (it.success) {
                    binding.btnFavoriteDetail.setImageResource(R.drawable.icon_favorite)
                    dataFavorite?.isFavorite = true
                }
            }
        }
    }

    private fun postRatingMovies() {
        userPreferences = UserPreferences(this)
        val sessionId = userPreferences.getSessionId().sessionId

        val ratingValue = binding.ratingBarDetail.rating * 2
        val apiKey = BuildConfig.API_KEY

        val dataRating = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DATA, MovieDataRating::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        val movieId = dataRating?.id
        var rating = dataRating?.rating

        if (ratingValue != rating?.toFloat() && ratingValue != 0.0f){
            detailViewModel.postRatingMovies(movieId, apiKey, sessionId, ratingValue).observe(this){
                if (it.success){
                    rating = ratingValue.toInt()
                    Toast.makeText(this, "Rating : $ratingValue", Toast.LENGTH_SHORT).show()
                    binding.btnRatingDetail.setImageResource(R.drawable.icon_star)
                }
            }
        } else if (ratingValue == 0.0f) {
            detailViewModel.deleteRatingMovies(movieId, apiKey, sessionId).observe(this){
                if (it.success){
                    Toast.makeText(this, "Rating : $ratingValue", Toast.LENGTH_SHORT).show()
                    binding.btnRatingDetail.setImageResource(R.drawable.icon_star_border)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dataRating = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DATA, MovieDataRating::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        val intent = Intent(this, ReviewActivity::class.java)
        intent.putExtra(ReviewActivity.EXTRA_DATA, DataId(dataRating?.id))
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.review -> startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_VALUE = "extra_value"
    }
}