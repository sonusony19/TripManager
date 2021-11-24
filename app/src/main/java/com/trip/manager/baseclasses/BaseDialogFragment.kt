package com.trip.manager.baseclasses

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.trip.manager.R

open class BaseDialogFragment : BottomSheetDialogFragment(), DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    lateinit var mContext: Context

    override fun getTheme(): Int = R.style.BottomSheetDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
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

    fun onBackPressed() {
        dismiss()
    }

    override fun onShow(dialog: DialogInterface?) {
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}