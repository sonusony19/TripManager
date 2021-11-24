package com.trip.manager.ui.trip.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseFragment
import com.trip.manager.databinding.FragmentMembersBinding
import com.trip.manager.ui.trip.viewmodel.TripViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

private const val TRIP_ID = "TRIP_ID"

class MembersFragment : BaseFragment() {

    private var tripID: String = ""
    private lateinit var binding: FragmentMembersBinding
    private lateinit var viewModel: TripViewModel

    companion object {
        fun newInstance(tripID: String) = MembersFragment().apply {
            arguments = Bundle().apply { putString(TRIP_ID, tripID) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { tripID = it.getString(TRIP_ID) ?: "" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_members, container, false)
        viewModel = getViewModel(TripViewModel::class)
        init()
        return binding.root
    }

    private fun init() {}

}