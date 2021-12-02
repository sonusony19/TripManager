package com.trip.manager.ui.home.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.adapters.TripAdapter
import com.trip.manager.baseclasses.BaseActivity
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.ActivityMainBinding
import com.trip.manager.ui.home.model.Trips
import com.trip.manager.ui.home.viewmodel.MainViewModel
import com.trip.manager.ui.login.view.LoginActivity
import com.trip.manager.ui.trip.view.AddTripFragment
import com.trip.manager.utils.confirmAction
import com.trip.manager.utils.showShortToast

class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class) {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
        getData()
    }

    private fun init() {
        binding.user = viewModel.getCurrentUser()
        binding.logout.setOnClickListener { logout() }
        viewModel.tripData.observe(this, tripObserver)
        binding.addTrip.setOnClickListener { AddTripFragment().show(supportFragmentManager, javaClass.simpleName) }
    }

    private fun getData() {
        viewModel.loading.value = true
        viewModel.getTrips()
    }

    private fun logout() {
        dialog = confirmAction(getString(R.string.please_confirm_logout), getString(R.string.yes), dialog)
        dialog?.instance?.setPositiveAction {
            it.dismiss()
            viewModel.removeListener()
            viewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        }

    }

    private val tripObserver = Observer<Response<Trips>> {
        viewModel.loading.value = false
        it.data?.let { trips ->
            binding.upcomingTrips.visibility = if (trips.upcomingTrips.isEmpty()) View.GONE else View.VISIBLE
            binding.upcomingLabel.visibility = if (trips.upcomingTrips.isEmpty()) View.GONE else View.VISIBLE
            binding.pastTrips.visibility = if (trips.pastTrips.isEmpty()) View.GONE else View.VISIBLE
            binding.pastLabel.visibility = if (trips.pastTrips.isEmpty()) View.GONE else View.VISIBLE
            binding.currentLabel.visibility = if (trips.currentTrips.isEmpty()) View.GONE else View.VISIBLE
            binding.currentTrips.visibility = if (trips.currentTrips.isEmpty()) View.GONE else View.VISIBLE
            binding.upcomingTrips.adapter = TripAdapter(this, trips.upcomingTrips)
            binding.pastTrips.adapter = TripAdapter(this, trips.pastTrips)
            binding.currentTrips.adapter = TripAdapter(this, trips.currentTrips)
            if (trips.currentTrips.isEmpty() && trips.pastTrips.isEmpty() && trips.upcomingTrips.isEmpty()) {
                binding.noData.visibility = View.VISIBLE
            } else binding.noData.visibility = View.GONE
        } ?: showError(it.error ?: "")
    }

    private fun showError(error: String) {
        showShortToast(error)
    }
}