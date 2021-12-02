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

class CreateUserActivity : BaseActivity<UserViewModel>(UserViewModel::class) {
    private lateinit var binding: ActivityCreateUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_user)
        init()
        viewModel.getUsers()
    }

    private fun init() {
        binding.user = User()
        binding.back.setOnClickListener { onBackPressed() }
        binding.save.setOnClickListener {
            viewModel.validateAndCreateUser(binding.user ?: User()).observe(this, createUserObserver)
        }
        viewModel.userData.observe(this, userListObserver)
    }

    private val createUserObserver = Observer<Response<Any>> {
        if (it.success) onBackPressed()
        else showShortToast(it.error)
    }

    private val userListObserver = Observer<Response<List<User>>> {
        viewModel.loading.value = false
        if (!it.success) onBackPressed()
    }
}