package com.trip.manager.ui.splash.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.trip.manager.baseclasses.BaseActivity
import com.trip.manager.baseclasses.BaseViewModel
import com.trip.manager.ui.home.view.MainActivity
import com.trip.manager.ui.login.view.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<BaseViewModel>(BaseViewModel::class) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moveForward()
    }

    private fun moveForward() {
        viewModel.checkLoginStatus().observe(this, {
            startActivity(Intent(this, if (it) MainActivity::class.java else LoginActivity::class.java))
            finish()
        })
    }
}