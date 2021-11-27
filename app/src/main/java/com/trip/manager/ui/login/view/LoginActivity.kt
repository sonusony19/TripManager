package com.trip.manager.ui.login.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseActivity
import com.trip.manager.databinding.ActivityLoginBinding
import com.trip.manager.ui.login.viewmodel.LoginViewModel
import com.trip.manager.utils.setGif

class LoginActivity : BaseActivity<LoginViewModel>(LoginViewModel::class) {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        openSheet()
    }

    private fun openSheet() {
        binding.image.setGif(R.raw.out_of_office)
        LoginFragment(viewModel) { onBackPressed() }.show(supportFragmentManager, "")
    }
}