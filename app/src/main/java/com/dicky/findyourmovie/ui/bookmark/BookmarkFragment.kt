package com.dicky.findyourmovie.ui.bookmark

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.data.local.UserData
import com.dicky.findyourmovie.data.local.UserPreferences
import com.dicky.findyourmovie.data.response.ResultsPopularFavoriteSimilar
import com.dicky.findyourmovie.data.response.ResultsItemRated
import com.dicky.findyourmovie.databinding.FragmentBookmarkBinding
import com.dicky.findyourmovie.ui.ViewModelFactory

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity?.intent?.getParcelableExtra(EXTRA_DATA, UserData::class.java)
        } else {
            activity?.intent?.getParcelableExtra(EXTRA_DATA)
        }
        setUpViewModel(data)
    }

    private fun setUpViewModel(data: UserData?) {
        userPreferences = UserPreferences(requireActivity())
        val accountId = data?.id
        val apiKey = BuildConfig.API_KEY
        val sessionId = userPreferences.getSessionId().sessionId

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val bookmarkViewModel: BookmarkViewModel by viewModels {
            factory
        }

        bookmarkViewModel.getRatedMovies(accountId, apiKey, sessionId).observe(viewLifecycleOwner){
            bookmarkViewModel.getFavoriteMovies(accountId, apiKey, sessionId).observe(viewLifecycleOwner){ list ->
                showRecyclerViewFavorite(list, it)
                showRecyclerViewRated(it, list)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerViewFavorite(listItem: List<ResultsPopularFavoriteSimilar>, listItem2: List<ResultsItemRated>) {
        binding.containerFavoriteShimmer.apply {
            stopShimmer()
            visibility = View.INVISIBLE
        }
        binding.tvTitleFavorite.visibility = View.VISIBLE
        binding.rvFavoriteFilm.apply {
            visibility = View.VISIBLE
            val listItemAdapter = ItemFavoriteMoviesAdapter(listItem, listItem2)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            //layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
            adapter = listItemAdapter
            setHasFixedSize(true)
            listItemAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerViewRated(listItem: List<ResultsItemRated>, listItem2: List<ResultsPopularFavoriteSimilar>) {
        binding.containerRatedShimmer.apply {
            stopShimmer()
            visibility = View.INVISIBLE
        }
        binding.tvTitleRated.visibility = View.VISIBLE
        binding.rvRatedFilm.apply {
            visibility = View.VISIBLE
            val listItemAdapter = ItemRatedMoviesAdapter(listItem, listItem2)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listItemAdapter
            setHasFixedSize(true)
            listItemAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}