package com.trip.manager.baseclasses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModelForClass
import kotlin.reflect.KClass

open class BaseFragment<V : BaseViewModel>(private val viewModelClass: KClass<V>) : Fragment() {
    lateinit var viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelForClass(clazz = viewModelClass).value
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel.loading.observe(viewLifecycleOwner) { (requireActivity() as BaseActivity<*>).viewModel.loading.value = it }
    }
}
