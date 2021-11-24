package com.trip.manager.ui.user.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseActivity
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.ActivityCreateUserBinding
import com.trip.manager.ui.user.model.User
import com.trip.manager.ui.user.viewmodel.UserViewModel
import com.trip.manager.utils.showShortToast
import org.koin.androidx.viewmodel.ext.android.getViewModel

class CreateUserActivity : BaseActivity() {
    private lateinit var binding: ActivityCreateUserBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_user)
        viewModel = getViewModel(UserViewModel::class)
        init()
    }

    private fun init() {
        binding.user = User()
        binding.back.setOnClickListener { onBackPressed() }
        binding.save.setOnClickListener {
            viewModel.validateAndCreateUser(binding.user ?: User()).observe(this, createUserObserver)
        }
    }

    private val createUserObserver = Observer<Response<Any>> {
        if (it.success) onBackPressed()
        else showShortToast(it.error)
    }
}