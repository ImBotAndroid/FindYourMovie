package com.dicky.findyourmovie.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.dicky.findyourmovie.BuildConfig
import com.dicky.findyourmovie.R
import com.dicky.findyourmovie.data.local.UserPreferences
import com.dicky.findyourmovie.databinding.ActivityLoginBinding
import com.dicky.findyourmovie.ui.ViewModelFactory
import com.dicky.findyourmovie.ui.web_view.WebViewActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userPreferences: UserPreferences

    private lateinit var data: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpViewModel()
        setUpButton()
    }

    private fun setUpViewModel() {
        loginViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[LoginViewModel::class.java]
    }

    @Suppress("DEPRECATION")
    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        supportActionBar?.hide()
    }

    private fun setUpButton() {
        binding.btnLoginFLogin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_login_f_login){
            login()
        }
    }

    private fun login() {
        val apiKey = BuildConfig.API_KEY
        val username = "hibotandroid"
        val password = "asdf123"

        userPreferences = UserPreferences(this)
        val token = ""

        data = this.getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        loginViewModel.getNewToken(apiKey).observe(this){ newToken ->
            if (newToken.success){
                userPreferences.setToken(newToken)

                loginViewModel.loginUser(apiKey, username, password, token).observe(this) { login ->
                    if (login.success) {
                        val intent = Intent(this, WebViewActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}