package com.trip.manager.baseclasses

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kaptain.hr.dialogx.DialogxBuilder
import com.trip.manager.R
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

open class BaseDialogFragment<V : BaseViewModel>(private val viewModelClass: KClass<V>) : BottomSheetDialogFragment(), DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    lateinit var viewModel: V
    var dialog: DialogxBuilder? = null

    override fun getTheme(): Int = R.style.BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
        viewModel = getViewModel(viewModelClass)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener(this)
        dialog.setOnKeyListener { _: DialogInterface?, keyCode: Int, _: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) onBackPressed()
            true
        }
        return dialog
    }

    private fun init() {
        viewModel.loading.observe(viewLifecycleOwner) { (requireActivity() as BaseActivity<*>).viewModel.loading.value = it }
    }

    open fun showOnFullScreen(dialog: BottomSheetDialog) {
        val bottomSheet = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)!!
        val sheetBehavior = BottomSheetBehavior.from<View?>(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val windowHeight = displayMetrics.heightPixels
        if (layoutParams != null) layoutParams.height = windowHeight
        sheetBehavior.peekHeight = windowHeight
        bottomSheet.layoutParams = layoutParams
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    open fun showActualSize(dialog: BottomSheetDialog) {
        val bottomSheet = dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)!!
        val sheetBehavior = BottomSheetBehavior.from<View?>(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        bottomSheet.layoutParams = layoutParams
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun onBackPressed() = dismiss()

    override fun onShow(dialog: DialogInterface?) {
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}