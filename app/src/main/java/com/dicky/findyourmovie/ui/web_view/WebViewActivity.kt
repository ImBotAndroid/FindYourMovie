package com.dicky.findyourmovie.ui.web_view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.ui.main.MainActivity
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.local.UserPreferences
import com.dicky.findyourmovie.data.response.LoginResponse
import com.dicky.findyourmovie.databinding.ActivityWebViewBinding
import com.dicky.findyourmovie.ui.ViewModelFactory

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private lateinit var webViewViewModel: WebViewViewModel
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViewModel()
        setUpView()
    }

    @Suppress("DEPRECATION")
    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    private fun setUpViewModel() {
        webViewViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[WebViewViewModel::class.java]

        userPreferences = UserPreferences(this)

        webViewViewModel.getTokenPref().observe(this) {
            setUpWebView(it)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView(it: LoginResponse) {
        binding.wvAccessApp.loadUrl("https://www.themoviedb.org/authenticate/${it.requestToken}")
        Log.e("TAG", "setUpWebView: ${it.requestToken}", )
        binding.wvAccessApp.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
        binding.wvAccessApp.settings.javaScriptEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.web_view_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit -> {
                userPreferences = UserPreferences(this)

                val apiKey = BuildConfig.API_KEY
                val token = userPreferences.getToken().requestToken

                webViewViewModel.getTokenPref().observe(this) {
                    if (userPreferences.getSessionId().sessionId.isEmpty()) {

                        webViewViewModel.getSessionId(apiKey, token).observe(this) { sessionId ->
                            if (sessionId.success) {
                                userPreferences.setSessionId(sessionId)

                                if (userPreferences.getSessionId().sessionId.isNotEmpty()){
                                    intent = Intent(this, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                }
                true
            }
            else -> true
        }
    }
}