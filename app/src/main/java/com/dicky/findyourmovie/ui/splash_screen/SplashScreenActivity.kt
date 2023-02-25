package com.dicky.findyourmovie.ui.splash_screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.dicky.findyourmovie.ui.main.MainActivity
import com.dicky.findyourmovie.databinding.ActivitySplashScreenBinding
import com.dicky.findyourmovie.ui.ViewModelFactory
import com.dicky.findyourmovie.ui.login.LoginActivity
import com.dicky.findyourmovie.ui.login.LoginViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[LoginViewModel::class.java]

        Handler(Looper.getMainLooper()).postDelayed({
            loginViewModel.getSessionIdPref().observe(this){
                if (it.sessionId.isEmpty()){
                    startActivity(Intent(this, LoginActivity::class.java))
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