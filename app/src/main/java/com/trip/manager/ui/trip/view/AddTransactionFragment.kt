package com.trip.manager.ui.trip.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.adapters.MemberAdapter
import com.trip.manager.baseclasses.BaseDialogFragment
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.FragmentAddTransactionBinding
import com.trip.manager.ui.trip.model.Member
import com.trip.manager.ui.trip.model.Transaction
import com.trip.manager.ui.trip.viewmodel.TripViewModel
import com.trip.manager.utils.TransactionType
import com.trip.manager.utils.showShortToast
import org.koin.androidx.viewmodel.ext.android.getViewModel

private const val TRIP_ID = "TRIP_ID"

class AddTransactionFragment : BaseDialogFragment() {
    private var tripId: String = ""
    private lateinit var binding: FragmentAddTransactionBinding
    private lateinit var viewModel: TripViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { tripId = it.getString(TRIP_ID) ?: "" }
    }

    companion object {
        fun newInstance(tripId: String) = AddTransactionFragment().apply {
            arguments = Bundle().apply { putString(TRIP_ID, tripId) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_transaction, container, false)
        viewModel = getViewModel(TripViewModel::class)
        init()
        getMembers()
        return binding.root
    }

    private fun getMembers() {
        viewModel.getMembers(tripId)
    }

    private fun init() {
        binding.back.setOnClickListener { dismiss() }
        binding.transaction = Transaction()
        binding.transactionTypes = TransactionType
        viewModel.memberData.observe(viewLifecycleOwner, memberObserver)
        binding.done.setOnClickListener {
            val members = (binding.members.adapter as? MemberAdapter)?.selectedMembers ?: arrayListOf()
            val transaction = binding.transaction ?: Transaction()
            viewModel.validateAndAddTransaction(tripId, transaction, members).observe(viewLifecycleOwner, transactionCreationObserver)
        }
    }

    private val memberObserver = Observer<Response<List<Member>>> {
        if (it.success && !it.data.isNullOrEmpty()) {
            binding.members.adapter = MemberAdapter(requireContext(), it.data).setSelectionMode(true)
        }
    }

    private val transactionCreationObserver = Observer<Response<Any>> {
        if (it.success) dismiss()
        else showShortToast(it.error)
    }
}