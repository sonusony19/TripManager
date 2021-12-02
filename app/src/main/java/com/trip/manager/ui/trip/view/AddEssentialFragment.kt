package com.trip.manager.ui.trip.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.trip.manager.R
import com.trip.manager.baseclasses.BaseDialogFragment
import com.trip.manager.baseclasses.Response
import com.trip.manager.databinding.FragmentAddEssentialBinding
import com.trip.manager.ui.trip.model.Essential
import com.trip.manager.ui.trip.model.Member
import com.trip.manager.ui.trip.viewmodel.TripViewModel
import com.trip.manager.utils.showShortToast

private const val TRIP_ID = "TRIP_ID"

class AddEssentialFragment : BaseDialogFragment<TripViewModel>(TripViewModel::class) {
    private var tripId: String = ""
    private lateinit var binding: FragmentAddEssentialBinding

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
        init()
        viewModel.loading.value = true
        viewModel.getMembers(tripId, true)
        return binding.root
    }

    private fun init() {
        binding.essential = Essential()
        binding.back.setOnClickListener { onBackPressed() }
        binding.done.setOnClickListener {
            viewModel.validateAndAddEssentials(tripId, binding.essential!!).observe(viewLifecycleOwner, essentialAdditionObserver)
        }
        viewModel.memberData.observe(viewLifecycleOwner, memberListObserver)
    }

    private val memberListObserver = Observer<Response<List<Member>>> {
        viewModel.loading.value = false
        if (!it.success) dismiss()
        else {
            val memberNames = ArrayList<String>()
            memberNames.add(getString(R.string.select_member))
            it.data?.forEach { member -> memberNames.add(member.name) }
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, memberNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.responsiblePerson.adapter = adapter
            binding.responsiblePerson.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position != 0) binding.essential!!.responsible = memberNames[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private val essentialAdditionObserver = Observer<Response<Any>> {
        if (it.success) onBackPressed()
        else showShortToast(it.error)
    }
}