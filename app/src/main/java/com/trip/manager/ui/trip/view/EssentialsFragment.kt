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

private const val TRIP_ID = "TRIP_ID"

class EssentialsFragment : BaseFragment<TripViewModel>(TripViewModel::class) {

    private var tripId: String = ""
    private lateinit var binding: FragmentEssentialsBinding


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
            binding.essentials.visibility = View.VISIBLE
            binding.essentials.adapter = EssentialAdapter(requireContext(), it.data) { view ->
                EssentialDetailsFragment.newInstance(tripId, view.tag as? String ?: "").show(childFragmentManager, "")
            }
        } else {
            binding.essentials.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        }
    }
}