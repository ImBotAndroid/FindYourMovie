package com.dicky.findyourmovie.ui.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.data.local.UserPreferences
import com.dicky.findyourmovie.ui.main.MainActivity
import com.dicky.findyourmovie.databinding.ActivitySplashScreenBinding
import com.dicky.findyourmovie.ui.ViewModelFactory
import com.dicky.findyourmovie.ui.login.LoginViewModel
import com.dicky.findyourmovie.ui.web_view.WebViewActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
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
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setUpViewModel() {
        val apiKey = BuildConfig.API_KEY
        userPreferences = UserPreferences(this)

        loginViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[LoginViewModel::class.java]

        loginViewModel.getNewToken(apiKey).observe(this){ newToken ->
            if (newToken.success){
                userPreferences.setToken(newToken)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            loginViewModel.getSessionIdPref().observe(this){
                if (it.sessionId.isEmpty()){
                    startActivity(Intent(this, WebViewActivity::class.java))
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    finish()
                }
            }
        }, 1000)
    }
}