package com.trip.manager.ui.home.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.trip.manager.R
import com.trip.manager.adapters.TripAdapter
import com.trip.manager.baseclasses.BaseActivity
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.ActivityMainBinding
import com.trip.manager.ui.home.viewmodel.MainViewModel
import com.trip.manager.ui.login.view.LoginActivity
import com.trip.manager.ui.trip.model.Trip
import com.trip.manager.ui.trip.view.AddTripFragment
import com.trip.manager.utils.showShortToast
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class) {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
        getData()
    }

    private fun init() {
        binding.logout.setOnClickListener { logout() }
        val layoutManager: GridLayoutManager by inject()
        binding.trips.layoutManager = layoutManager
        viewModel.tripData.observe(this, tripObserver)
        binding.addTrip.setOnClickListener { AddTripFragment().show(supportFragmentManager, javaClass.simpleName) }
    }

    private fun getData() {
        viewModel.loading.value = true
        viewModel.getTrips()
    }

    private fun logout() {
        viewModel.removeListener()
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }

    private val tripObserver = Observer<Response<List<Trip>>> {
        viewModel.loading.value = false
        it.data?.let { trips ->
            binding.trips.adapter = TripAdapter(this, trips)
        } ?: showError(it.error ?: "")
    }

    private fun showError(error: String) {
        showShortToast(error)
    }
}