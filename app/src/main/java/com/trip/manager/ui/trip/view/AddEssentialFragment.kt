package com.trip.manager.ui.trip.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseDialogFragment
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.FragmentAddEssentialBinding
import com.trip.manager.ui.trip.model.Essential
import com.trip.manager.ui.trip.viewmodel.TripViewModel
import com.trip.manager.utils.showShortToast
import org.koin.androidx.viewmodel.ext.android.getViewModel

private const val TRIP_ID = "TRIP_ID"

class AddEssentialFragment : BaseDialogFragment() {
    private var tripId: String = ""
    private lateinit var binding: FragmentAddEssentialBinding
    private lateinit var viewModel: TripViewModel

    companion object {
        fun newInstance(tripID: String) = AddEssentialFragment().apply {
            arguments = Bundle().apply { putString(TRIP_ID, tripID) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { tripId = it.getString(TRIP_ID) ?: "" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_essential, container, false)
        viewModel = getViewModel(TripViewModel::class)
        init()
        return binding.root
    }

    private fun init() {
        binding.essential = Essential()
        binding.back.setOnClickListener { onBackPressed() }
        binding.done.setOnClickListener {
            viewModel.validateAndAddEssentials(tripId, binding.essential!!).observe(viewLifecycleOwner, essentialAdditionObserver)
        }
    }

    private val essentialAdditionObserver = Observer<Response<Any>> {
        if (it.success) onBackPressed()
        else showShortToast(it.error)
    }
}