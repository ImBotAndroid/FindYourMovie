package com.dicky.findyourmovie.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.local.UserData
import com.dicky.findyourmovie.data.local.UserPreferences
import com.dicky.findyourmovie.data.response.ResultsItemRated
import com.dicky.findyourmovie.databinding.FragmentProfileBinding
import com.dicky.findyourmovie.ui.ViewModelFactory
import com.dicky.findyourmovie.ui.web_view.WebViewActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity?.intent?.getParcelableExtra(EXTRA_DATA, UserData::class.java)
        } else {
            @Suppress("DEPRECATION")
            activity?.intent?.getParcelableExtra(EXTRA_DATA)
        }

        setUpAccountData(data)
        setUpViewModel(data)
    }

    private fun setUpViewModel(data: UserData?) {
        userPreferences = UserPreferences(requireActivity())
        val accountId = data?.id
        val apiKey = BuildConfig.API_KEY
        val sessionId = userPreferences.getSessionId().sessionId

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val profileViewModel: ProfileViewModel by viewModels {
            factory
        }

        profileViewModel.getRatedMovies(accountId, apiKey, sessionId).observe(viewLifecycleOwner){
            setUpProgressBarRating(it)
        }

        binding.btnLogout.setOnClickListener {
            profileViewModel.deleteDataPref()
            profileViewModel.getNewToken(apiKey).observe(viewLifecycleOwner){
                if (it.success){
                    userPreferences.setToken(it)
                    val intent = Intent(requireActivity(), WebViewActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun setUpProgressBarRating(data: List<ResultsItemRated>) {
        val total = data.sumOf {
            it.rating
        }

        val result = total.toFloat() / data.size.toFloat()
        binding.pbPresentage.progressDrawable = context?.getDrawable(R.drawable.rating_precentage_green)
        binding.pbPresentage.progress = result.toInt().times(10)
        binding.tvPresentage.text = String.format("%.1f", result * 10) + "%"
    }

    private fun setUpAccountData(data: UserData?) {
        binding.tvUsernameProfile.text = data?.username

        Glide.with(this)
            .load(data?.image)
            .apply(RequestOptions.placeholderOf(R.drawable.icon_image).error(R.drawable.icon_account_white).centerInside())
            .circleCrop()
            .into(binding.ivProfile)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_DATA = "data"
    }
}