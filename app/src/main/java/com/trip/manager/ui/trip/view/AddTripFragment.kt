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
import com.trip.manager.utils.openDatePicker
import com.trip.manager.utils.showShortToast

class AddTripFragment : BaseDialogFragment<TripViewModel>(TripViewModel::class) {

    private lateinit var binding: FragmentAddTripBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_trip, container, false)
        init()
        getUsers()
        return binding.root
    }


    private fun init() {
        binding.trip = Trip()
        viewModel.userData.observe(viewLifecycleOwner, userListObserver)
        binding.back.setOnClickListener { onBackPressed() }
        binding.startDate.setOnClickListener { selectStartDate() }
        binding.endDate.setOnClickListener { selectEndDate() }
        binding.addUser.setOnClickListener { startActivity(Intent(requireContext(), CreateUserActivity::class.java)) }
        binding.selectToggle.setOnCheckedChangeListener { _, checked -> (binding.users.adapter as? UserAdapter)?.selectDeselect(checked) }
        binding.done.setOnClickListener {
            val trip = binding.trip ?: Trip()
            val members = arrayListOf<String>()
            val selectedUsers = (binding.users.adapter as? UserAdapter)?.selectedUsers ?: arrayListOf()
            selectedUsers.forEach { members.add(it.uid) }
            trip.members = members
            viewModel.validateAndCreateTrip(trip, selectedUsers).observe(viewLifecycleOwner, tripCreationObserver)
        }
    }

    private fun selectStartDate() {
        requireContext().openDatePicker(pastAllowed = true, futureAllowed = true, currentDate = binding.trip?.startAt, minDate = null, maxDate = binding.trip?.endAt) {
            binding.trip?.startAt = it.time
            binding.invalidateAll()
        }
    }

    private fun selectEndDate() {
        requireContext().openDatePicker(pastAllowed = true, futureAllowed = true, currentDate = binding.trip?.endAt, minDate = binding.trip?.startAt, maxDate = null) {
            binding.trip?.endAt = it.time
            binding.invalidateAll()
        }
    }

    private fun getUsers() {
        viewModel.loading.value = true
        viewModel.getUsers()
    }

    private val tripCreationObserver = Observer<Response<Any>> {
        if (it.success) onBackPressed()
        else showShortToast(it.error)
    }

    private val userListObserver = Observer<Response<List<User>>> {
        viewModel.loading.value = false
        binding.users.adapter = UserAdapter(requireContext(), it.data ?: listOf())
    }
}