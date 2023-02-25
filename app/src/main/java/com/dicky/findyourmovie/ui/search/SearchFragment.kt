package com.dicky.findyourmovie.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.data.local.UserData
import com.dicky.findyourmovie.data.local.UserPreferences
import com.dicky.findyourmovie.data.response.ResultsItemRated
import com.dicky.findyourmovie.data.response.ResultsItemSearch
import com.dicky.findyourmovie.databinding.FragmentSearchBinding
import com.dicky.findyourmovie.ui.ViewModelFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    var lastPosition: Int = 0
    private lateinit var data: SharedPreferences

    private lateinit var userPreferences: UserPreferences
    val apiKey = BuildConfig.API_KEY

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel(savedInstanceState)
    }

    private fun setUpViewModel(savedInstanceState: Bundle?) {

        val dataParcel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity?.intent?.getParcelableExtra(EXTRA_DATA, UserData::class.java)
        } else {
            @Suppress("DEPRECATION")
            activity?.intent?.getParcelableExtra(EXTRA_DATA)
        }

        val accountId = dataParcel?.id

        userPreferences = UserPreferences(requireActivity())

        val sessionId = userPreferences.getSessionId().sessionId
        val searchView = binding.searchView

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())

        val searchViewModel: SearchViewModel by viewModels {
            factory
        }

        if (savedInstanceState == null){
            searchViewModel.saveSearchData()
        }

        searchViewModel.apply {
            saveSearchData().observe(viewLifecycleOwner){
                getRatedMovies(accountId, apiKey, sessionId).observe(viewLifecycleOwner){ list ->
                    showRecyclerView(it, list)
                }
            }
        }

        data = requireActivity().getSharedPreferences("", Context.MODE_PRIVATE)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchViewModel.searchMovie(apiKey, query).let {
                    binding.tvResultNoData.visibility = View.INVISIBLE
                    binding.rvSearchFilm.visibility = View.INVISIBLE
                    binding.containerSearchShimmer.apply {
                        visibility = View.VISIBLE
                        startShimmer()
                    }
                    val e : SharedPreferences.Editor = data.edit()
                    e.putInt("", 0)
                    e.apply()
                }
                searchView.clearFocus()
                setObserver(dataParcel, searchViewModel, query, sessionId)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setObserver(
        dataParcel: UserData?,
        searchViewModel: SearchViewModel,
        query: String,
        sessionId: String
    ) {
        searchViewModel.searchMovie(apiKey, query).observe(viewLifecycleOwner){ isEmpty ->
            if (isEmpty.totalResults != 0){
                searchViewModel.apply {
                    saveSearchData().observe(viewLifecycleOwner){
                        getRatedMovies(dataParcel?.id, apiKey, sessionId).observe(viewLifecycleOwner){ list ->
                            showRecyclerView(it, list)
                        }
                    }
                }
            } else {
                binding.tvResultNoData.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerView(listItem: List<ResultsItemSearch>, listItem2: List<ResultsItemRated>) {

        lastPosition = data.getInt("", 0)

        binding.rvSearchFilm.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                lastPosition = (binding.rvSearchFilm.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            }
        })

        binding.containerSearchShimmer.apply {
            stopShimmer()
            visibility = View.INVISIBLE
        }

        val listItemAdapter = ItemSearchMovieAdapter(listItem, listItem2)
        binding.rvSearchFilm.apply {
            visibility = View.VISIBLE
            layoutManager = GridLayoutManager(context, 2)
            adapter = listItemAdapter
            scrollToPosition(lastPosition)
        }
        listItemAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        val e : SharedPreferences.Editor = data.edit()
        e.putInt("", lastPosition)
        e.apply()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}