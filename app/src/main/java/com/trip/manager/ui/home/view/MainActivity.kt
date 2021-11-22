package com.trip.manager.ui.home.view

import android.os.Bundle
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseActivity
import com.trip.manager.ui.home.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = getViewModel(MainViewModel::class)
    }
}