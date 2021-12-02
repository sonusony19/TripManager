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
import com.trip.manager.databinding.FragmentEssentialDetailsBinding
import com.trip.manager.ui.trip.viewmodel.TripViewModel
import com.trip.manager.utils.confirmAction
import com.trip.manager.utils.showShortToast
import java.util.*

private const val TRIP_ID = "TRIP_ID"
private const val ESSENTIAL_ID = "ESSENTIAL_ID"

class EssentialDetailsFragment : BaseDialogFragment<TripViewModel>(TripViewModel::class) {

    private var tripID: String = ""
    private var essentialID: String = ""
    private lateinit var binding: FragmentEssentialDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tripID = it.getString(TRIP_ID) ?: ""
            essentialID = it.getString(ESSENTIAL_ID) ?: ""
        }
    }

    companion object {
        fun newInstance(tripID: String, essentialId: String) = EssentialDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(TRIP_ID, tripID)
                putString(ESSENTIAL_ID, essentialId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_essential_details, container, false)
        init()
        getEssentialDetails()
        return binding.root
    }

    private fun init() {
        binding.back.setOnClickListener { onBackPressed() }
        binding.cancel.setOnClickListener { onBackPressed() }
        binding.delete.setOnClickListener { confirmDeletion() }
        binding.save.setOnClickListener {
            viewModel.validateAndSaveEssential(tripID, binding.essential!!).observe(viewLifecycleOwner, essentialUpdationObserver)
        }
        binding.handledCheckbox.setOnCheckedChangeListener { _, isChecked ->
            run {
                binding.essential!!.handledAt = if (isChecked) Date().time else null
                binding.handledBy.visibility = if (isChecked) View.VISIBLE else View.GONE
            }
        }
    }

    private fun confirmDeletion() {
        dialog = requireContext().confirmAction(getString(R.string.are_you_sure_you_want_to_delete_this_essential), previousDialog = dialog)
        dialog?.instance?.setPositiveAction { dialog ->
            dialog.dismiss()
            viewModel.deleteEssential(tripID, essentialID).observe(viewLifecycleOwner) {
                viewModel.loading.value = false
                if (it.success) onBackPressed()
                else showShortToast(it.error)
            }
        }
    }

    private fun getEssentialDetails() {
        viewModel.loading.value = true
        viewModel.getEssentialDetails(tripID, essentialID).observe(viewLifecycleOwner) {
            viewModel.loading.value = false
            if (!it.success) onBackPressed()
            else it.data?.let { essential -> binding.essential = essential }
        }
    }

    private val essentialUpdationObserver = Observer<Response<String>> {
        viewModel.loading.value = false
        if (it.success) onBackPressed()
        else showShortToast(it.error)
    }
}