package com.trip.manager.utils

import android.app.Activity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.trip.manager.R


fun Activity.showProgressDialog(previousLoader: BottomSheetDialog?): BottomSheetDialog {
    return if (previousLoader?.isShowing == true) previousLoader else {
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.loader_layout)
        dialog.dismissWithAnimation = true
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()
        dialog
    }
}
