package com.trip.manager.ui.login.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseActivity
import com.trip.manager.databinding.ActivityLoginBinding
import com.trip.manager.listeners.FirebaseAuthListener
import com.trip.manager.ui.home.view.MainActivity
import com.trip.manager.ui.login.model.LoginRequest
import com.trip.manager.ui.login.viewmodel.LoginViewModel
import com.trip.manager.utils.showShortToast
import org.koin.androidx.viewmodel.ext.android.getViewModel

class LoginActivity : BaseActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = getViewModel(LoginViewModel::class)
        init()
    }

    private fun init() {
        binding.loginRequest = LoginRequest()
        binding.login.setOnClickListener {
            viewModel.validateCredentials(binding.loginRequest!!).observe(
                    this, validationObserver
            )
        }
    }

    private val validationObserver = Observer<String> {
        if (it.isEmpty()) viewModel.loginUser(binding.loginRequest!!, authListener)
        else showShortToast(it)
    }

    private val authListener = object : FirebaseAuthListener {
        override fun onAuthSuccess() {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        }

        override fun onAuthFailure(error: String) = showShortToast(error)
    }
}