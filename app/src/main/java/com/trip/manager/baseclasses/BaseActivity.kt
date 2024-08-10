package com.trip.manager.baseclasses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kaptain.hr.dialogx.DialogxBuilder
import com.trip.manager.utils.showProgressDialog
import org.koin.androidx.viewmodel.ext.android.viewModelForClass
import kotlin.reflect.KClass

open class BaseActivity<V : BaseViewModel>(private val viewModelClass: KClass<V>) : AppCompatActivity() {
    lateinit var viewModel: V
    private var loader: BottomSheetDialog? = null
    var dialog: DialogxBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelForClass(clazz = viewModelClass).value
        init()
    }

    private fun init() {
        viewModel.loading.observe(this, loadingObserver)
    }

    private val loadingObserver = Observer<Boolean> {
        if (it) {
            loader = showProgressDialog(loader)
        } else loader?.dismiss()
    }

    override fun onDestroy() {
        viewModel.loading.value = false
        super.onDestroy()
    }

}