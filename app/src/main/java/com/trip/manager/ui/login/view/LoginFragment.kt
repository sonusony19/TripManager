package com.trip.manager.ui.login.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseDialogFragment
import com.trip.manager.databinding.FragmentLoginBinding
import com.trip.manager.listeners.FirebaseAuthListener
import com.trip.manager.ui.home.view.MainActivity
import com.trip.manager.ui.login.model.LoginRequest
import com.trip.manager.ui.login.viewmodel.LoginViewModel
import com.trip.manager.utils.showShortToast

class LoginFragment(private val listener: View.OnClickListener) : BaseDialogFragment<LoginViewModel>(LoginViewModel::class) {

    private lateinit var binding: FragmentLoginBinding
    private var loggedIn: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        init()
        return binding.root
    }

    override fun onShow(dialog: DialogInterface?) {
        super.onShow(dialog)
        (dialog as BottomSheetDialog).setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
    }

    private fun init() {
        binding.loginRequest = LoginRequest(email = "sonusony1997@gmail.com", password = "12345678")
        binding.login.setOnClickListener {
            viewModel.validateCredentials(binding.loginRequest!!).observe(this, validationObserver)
        }
    }

    private val validationObserver = Observer<String> {
        if (it.isEmpty()) {
            viewModel.loading.value = true
            viewModel.loginUser(binding.loginRequest!!, authListener)
        } else showShortToast(it)
    }

    private val authListener = object : FirebaseAuthListener {
        override fun onAuthSuccess() {
            loggedIn = true
            viewModel.loading.value = false
            startActivity(Intent(requireContext(), MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            requireActivity().finish()
        }

        override fun onAuthFailure(error: String) {
            viewModel.loading.value = false
            showShortToast(error)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!loggedIn)
            listener.onClick(binding.root)
    }

}