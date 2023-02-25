package com.dicky.findyourmovie.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.local.UserData
import com.dicky.findyourmovie.data.local.UserPreferences
import com.dicky.findyourmovie.data.response.AccountDetailResponse
import com.dicky.findyourmovie.data.response.ResultsPopularFavoriteSimilar
import com.dicky.findyourmovie.data.response.ResultsItemRated
import com.dicky.findyourmovie.data.response.ResultsItemUpcomingAndNowPlaying
import com.dicky.findyourmovie.databinding.FragmentHomeBinding
import com.dicky.findyourmovie.ui.ViewModelFactory
import com.dicky.findyourmovie.ui.bookmark.BookmarkFragment
import com.dicky.findyourmovie.ui.profile.ProfileFragment
import com.dicky.findyourmovie.ui.search.SearchFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences

    val apiKey = BuildConfig.API_KEY
    private val imageUrl = BuildConfig.IMAGE_URL

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel(savedInstanceState)
    }

    private fun setUpViewModel(savedInstanceState: Bundle?) {

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())

        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        userPreferences = UserPreferences(requireActivity())

        val sessionId = userPreferences.getSessionId().sessionId

        if (savedInstanceState == null) {
            homeViewModel.apply {
                getAccountDetail(apiKey, sessionId)
                getNowPlayingMovies(apiKey)
                getUpComingMovies(apiKey)
                getPopularMovies(apiKey)
            }
        }

        homeViewModel.apply {
            saveAccountDetail().observe(viewLifecycleOwner) { account ->
                getUserData(account)
                userPreferences.setAccountId(id)
                getMoviesNowPlaying().observe(viewLifecycleOwner) {
                    getRatedMovies(account.id, apiKey, sessionId).observe(viewLifecycleOwner){ list ->
                        getFavoriteMovies(account.id, apiKey, sessionId).observe(viewLifecycleOwner){ list2 ->
                            showRecyclerViewNowPlaying(it, list, list2)
                        }
                    }
                }
                getMoviesUpComing().observe(viewLifecycleOwner) {
                    getRatedMovies(account.id, apiKey, sessionId).observe(viewLifecycleOwner){ list ->
                        getFavoriteMovies(account.id, apiKey, sessionId).observe(viewLifecycleOwner){ list2 ->
                            showRecyclerViewUpcoming(it, list, list2)
                        }
                    }

                }
                getMoviesPopular().observe(viewLifecycleOwner) {
                    getRatedMovies(account.id, apiKey, sessionId).observe(viewLifecycleOwner){ list ->
                        getFavoriteMovies(account.id, apiKey, sessionId).observe(viewLifecycleOwner){ list2 ->
                            showRecyclerViewPopular(it, list, list2)
                        }

                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData(account: AccountDetailResponse){
        shimmerStyle()

        binding.tvUsernameHome.apply {
            visibility = View.VISIBLE
            text = "Hi, ${account.username}"
        }

        binding.ivImageHome.visibility = View.VISIBLE

        Glide.with(this)
            .load(imageUrl + account.avatar.tmdb.avatarPath)
            .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_account_white).centerInside())
            .circleCrop()
            .into(binding.ivImageHome)

        sendUserData(account)
    }

    private fun sendUserData(account: AccountDetailResponse) {
        val id = account.id
        val username = account.username
        val name = account.name
        val image = imageUrl + account.avatar.tmdb.avatarPath

        val data = UserData(id, username, name, image)

        activity?.intent?.apply {
            putExtra(BookmarkFragment.EXTRA_DATA, data)
            putExtra(SearchFragment.EXTRA_DATA, data)
            putExtra(ProfileFragment.EXTRA_DATA, data)
        }
    }

    private fun shimmerStyle() {
        binding.containerHomeProfileShimmer.apply {
            stopShimmer()
            visibility = View.INVISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerViewNowPlaying(listItem: List<ResultsItemUpcomingAndNowPlaying>, listItem2: List<ResultsItemRated>, listItem3: List<ResultsPopularFavoriteSimilar>) {
        binding.containerNowPlayingMovies.apply {
            stopShimmer()
            visibility = View.INVISIBLE
        }

        binding.tvTitleNowPlaying.visibility = View.VISIBLE
        val listItemAdapter = ItemNowPlayingMoviesAdapter(listItem, listItem2, listItem3)
        binding.rvNowPlaying.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listItemAdapter
            setHasFixedSize(true)
        }
        listItemAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerViewUpcoming(listItem: List<ResultsItemUpcomingAndNowPlaying>, listItem2: List<ResultsItemRated>, listItem3: List<ResultsPopularFavoriteSimilar>) {
        binding.containerUpcomingMovies.apply {
            stopShimmer()
            visibility = View.INVISIBLE
        }
        binding.tvTitleUpcoming.visibility = View.VISIBLE
        val listItemAdapter = ItemUpcomingMoviesAdapter(listItem, listItem2, listItem3)
        binding.rvUpcomingMovies.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listItemAdapter
            setHasFixedSize(true)
        }
        listItemAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerViewPopular(listItem: List<ResultsPopularFavoriteSimilar>, listItem2: List<ResultsItemRated>, listItem3: List<ResultsPopularFavoriteSimilar>) {
        binding.containerPopularMovies.apply {
            stopShimmer()
            visibility = View.INVISIBLE
        }

        binding.tvTitlePopular.visibility = View.VISIBLE
        val listItemAdapter = ItemPopularMoviesAdapter(listItem, listItem2, listItem3)
        binding.rvPopularMovies.apply {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = listItemAdapter
            setHasFixedSize(true)
        }
        listItemAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}