package com.trip.manager.ui.trip.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.adapters.TransactionAdapter
import com.trip.manager.baseclasses.BaseDialogFragment
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.FragmentTransactionBinding
import com.trip.manager.ui.trip.model.Transaction
import com.trip.manager.ui.trip.viewmodel.TripViewModel

private const val TRIP_ID = "TRIP_ID"
private const val MEMBER_ID = "MEMBER_ID"

class TransactionFragment : BaseDialogFragment<TripViewModel>(TripViewModel::class) {
    private var tripId: String = ""
    private var memberId: String = ""
    private lateinit var binding: FragmentTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tripId = it.getString(TRIP_ID) ?: ""
            memberId = it.getString(MEMBER_ID) ?: ""
        }
    }

    companion object {
        fun newInstance(tripID: String, memberId: String) = TransactionFragment().apply {
            arguments = Bundle().apply {
                putString(TRIP_ID, tripID)
                putString(MEMBER_ID, memberId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false)
        init()
        viewModel.getTransaction(tripId, memberId)
        return binding.root
    }

    private fun init() {
        binding.back.setOnClickListener { onBackPressed() }
        viewModel.transactionData.observe(viewLifecycleOwner, transactionObserver)
    }

    private val transactionObserver = Observer<Response<List<Transaction>>> {
        viewModel.loading.value = false
        if (it.success && !it.data.isNullOrEmpty()) {
            binding.noData.visibility = View.GONE
            binding.transactions.visibility = View.VISIBLE
            binding.transactions.adapter = TransactionAdapter(requireContext(), it.data)
        } else {
            binding.transactions.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        }
    }

}