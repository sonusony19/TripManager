package com.trip.manager.ui.home.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = getViewModel(MainViewModel::class)
        init()
        viewModel.getTrips()
    }

    override fun onResume() {
        super.onResume()
        Log.d("Sonu", "onResume: ${viewModel.getCount()}")
    }

    private fun init() {
        binding.logout.setOnClickListener { logout() }
        val layoutManager: GridLayoutManager by inject()
        binding.trips.layoutManager = layoutManager
        viewModel.tripData.observe(this, tripObserver)
        binding.addTrip.setOnClickListener { AddTripFragment().show(supportFragmentManager, javaClass.simpleName) }
    }

    private fun logout() {
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }

    private val tripObserver = Observer<Response<List<Trip>>> {
        it.data?.let { trips ->
            binding.trips.adapter = TripAdapter(this, trips)
        } ?: showError(it.error ?: "")
    }

    private fun showError(error: String) {
        showShortToast(error)
    }
}