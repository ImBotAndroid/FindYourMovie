package com.dicky.findyourmovie.ui.detail.review

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.data.local.DataId
import com.dicky.findyourmovie.data.local.MovieDataRating
import com.dicky.findyourmovie.data.response.ResultsItemReviews
import com.dicky.findyourmovie.databinding.ActivityReviewBinding
import com.dicky.findyourmovie.ui.ViewModelFactory
import com.dicky.findyourmovie.ui.detail.DetailActivity

class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding
    private lateinit var reviewsViewModel: ReviewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(DetailActivity.EXTRA_DATA, DataId::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(DetailActivity.EXTRA_DATA)
        }

        Log.e("TAG", "Review: ${dataId?.id}", )

        setUpViewModel(dataId)
        setUpView()
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

    private fun setUpViewModel(dataId: DataId?) {
        val apiKey = BuildConfig.API_KEY
        val movieId = dataId?.id
        reviewsViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[ReviewsViewModel::class.java]

        reviewsViewModel.getReviewsMovies(movieId, apiKey).observe(this){
            showRecyclerViewReviews(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerViewReviews(data: List<ResultsItemReviews>) {
        val listItemAdapter = ItemReviewsAdapter(data)
        binding.rvReviews.apply {
            adapter = listItemAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        listItemAdapter.notifyDataSetChanged()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}