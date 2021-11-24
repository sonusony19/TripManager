package com.trip.manager.ui.trip.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.adapters.UserAdapter
import com.trip.manager.baseclasses.BaseDialogFragment
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.FragmentAddTripBinding
import com.trip.manager.ui.trip.model.Trip
import com.trip.manager.ui.trip.viewmodel.TripViewModel
import com.trip.manager.ui.user.model.User
import com.trip.manager.ui.user.view.CreateUserActivity
import com.trip.manager.utils.showShortToast
import org.koin.androidx.viewmodel.ext.android.getViewModel

class AddTripFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentAddTripBinding
    private lateinit var viewModel: TripViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_trip, container, false)
        viewModel = getViewModel(TripViewModel::class)
        init()
        viewModel.getUsers()
        return binding.root
    }

    private fun init() {
        binding.trip = Trip()
        viewModel.userData.observe(viewLifecycleOwner, userListObserver)
        binding.back.setOnClickListener { onBackPressed() }
        binding.addUser.setOnClickListener { startActivity(Intent(requireContext(), CreateUserActivity::class.java)) }
        binding.done.setOnClickListener {
            val trip = binding.trip ?: Trip()
            trip.members = (binding.users.adapter as? UserAdapter)?.selectedUsers ?: arrayListOf()
            viewModel.validateAndCreateTrip(trip).observe(viewLifecycleOwner, tripCreationObserver)
        }
    }

    private val tripCreationObserver = Observer<Response<Any>> {
        if (it.success) onBackPressed()
        else showShortToast(it.error)
    }

    private val userListObserver = Observer<Response<List<User>>> {
        binding.users.adapter = UserAdapter(requireContext(), it.data ?: listOf())
    }
}