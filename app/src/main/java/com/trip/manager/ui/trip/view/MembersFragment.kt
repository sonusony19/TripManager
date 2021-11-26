package com.trip.manager.ui.trip.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.adapters.MemberAdapter
import com.trip.manager.baseclasses.BaseFragment
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.FragmentMembersBinding
import com.trip.manager.ui.trip.model.Member
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
        viewModel.getMembers(tripID)
        return binding.root
    }

    private fun init() {
        viewModel.memberData.observe(viewLifecycleOwner, memberObserver)
        binding.addTransaction.setOnClickListener {
            AddTransactionFragment.newInstance(tripID).show(childFragmentManager, javaClass.simpleName)
        }
    }

    private val memberObserver = Observer<Response<List<Member>>> {
        if (it.success && !it.data.isNullOrEmpty()) {
            binding.noData.visibility = View.GONE
            binding.members.adapter = MemberAdapter(requireContext(), it.data)
        } else {
            binding.noData.visibility = View.VISIBLE
        }
    }

}