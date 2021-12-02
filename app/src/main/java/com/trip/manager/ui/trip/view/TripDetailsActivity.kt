package com.trip.manager.ui.trip.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.adapters.PagerAdapter
import com.trip.manager.baseclasses.BaseActivity
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.ActivityTripDetailsBinding
import com.trip.manager.ui.trip.model.Trip
import com.trip.manager.ui.trip.viewmodel.TripViewModel
import com.trip.manager.utils.showShortToast

class TripDetailsActivity : BaseActivity<TripViewModel>(TripViewModel::class) {
    private lateinit var binding: ActivityTripDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_details)
        init()
        getData()
    }

    private fun init() {
        viewModel.tripData.observe(this, tripDetailsObserver)
        val pagerAdapter = PagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(MembersFragment.newInstance(intent.getStringExtra("ID") ?: ""), getString(R.string.member))
        pagerAdapter.addFragment(EssentialsFragment.newInstance(intent.getStringExtra("ID") ?: ""), getString(R.string.essentials))
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun getData() {
        binding.back.setOnClickListener { onBackPressed() }
        viewModel.getTripDetails(intent.getStringExtra("ID") ?: "")
    }

    private val tripDetailsObserver = Observer<Response<Trip>> {
        if (it.success) binding.trip = it.data!!
        else showShortToast(it.error)
    }
}