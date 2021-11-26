package com.trip.manager.ui.trip.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.adapters.EssentialAdapter
import com.trip.manager.baseclasses.BaseFragment
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.FragmentEssentialsBinding
import com.trip.manager.ui.trip.model.Essential
import com.trip.manager.ui.trip.viewmodel.TripViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

private const val TRIP_ID = "TRIP_ID"

class EssentialsFragment : BaseFragment() {

    private var tripId: String = ""
    private lateinit var binding: FragmentEssentialsBinding
    private lateinit var viewModel: TripViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { tripId = it.getString(TRIP_ID) ?: "" }
    }

    companion object {
        fun newInstance(tipId: String) = EssentialsFragment().apply {
            arguments = Bundle().apply { putString(TRIP_ID, tipId) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_essentials, container, false)
        viewModel = getViewModel(TripViewModel::class)
        init()
        viewModel.getEssentials(tripId)
        return binding.root
    }

    private fun init() {
        viewModel.essentialData.observe(viewLifecycleOwner, essentialsObserver)
        binding.addEssentials.setOnClickListener {
            AddEssentialFragment.newInstance(tripId).show(childFragmentManager, javaClass.simpleName)
        }
    }

    private val essentialsObserver = Observer<Response<List<Essential>>> {
        if (it.success && !it.data.isNullOrEmpty()) {
            binding.noData.visibility = View.GONE
            binding.essentials.adapter = EssentialAdapter(requireContext(), it.data) {}
        } else {
            binding.noData.visibility = View.VISIBLE
        }
    }
}