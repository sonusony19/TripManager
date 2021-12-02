package com.trip.manager.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kaptain.hr.dialogx.DialogxBuilder
import com.trip.manager.R
import com.trip.manager.dialogx.Dialogx
import com.trip.manager.listeners.DateTimeListener
import java.util.*


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

fun Context.openDatePicker(pastAllowed: Boolean, futureAllowed: Boolean, currentDate: Long?, minDate: Long?, maxDate: Long?, listener: DateTimeListener) {
    val current = Calendar.getInstance()
    currentDate?.let { current.timeInMillis = it }
    val picker = DatePickerDialog(this, R.style.DateTimePickerTheme, { _, year, month, dayOfMonth ->
        val selectedDate = Calendar.getInstance()
        selectedDate[Calendar.DAY_OF_MONTH] = dayOfMonth
        selectedDate[Calendar.MONTH] = month
        selectedDate[Calendar.YEAR] = year
        selectedDate[Calendar.HOUR_OF_DAY] = 0
        selectedDate[Calendar.MINUTE] = 0
        selectedDate[Calendar.SECOND] = 0
        listener.onSelect(selectedDate.time)
    }, current[Calendar.YEAR], current[Calendar.MONTH], current[Calendar.DAY_OF_MONTH])
    //picker.window?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.white_rounded_background))
    if (!pastAllowed) picker.datePicker.minDate = Calendar.getInstance().timeInMillis
    if (!futureAllowed) picker.datePicker.maxDate = Calendar.getInstance().timeInMillis
    minDate?.let { picker.datePicker.minDate = it }
    maxDate?.let { picker.datePicker.maxDate = it }
    picker.show()
}

fun Context.openTimePicker(currentTime: Long?, listener: DateTimeListener) {
    val current = Calendar.getInstance()
    currentTime?.let { current.timeInMillis = it }
    val picker = TimePickerDialog(this, R.style.DateTimePickerTheme, { _, hour, minute ->
        val selectedDate = Calendar.getInstance()
        selectedDate[Calendar.HOUR_OF_DAY] = hour
        selectedDate[Calendar.MINUTE] = minute
        selectedDate[Calendar.SECOND] = 0
        listener.onSelect(selectedDate.time)
    }, current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE), false)
    picker.window?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.white_rounded_background))
    picker.show()
}

fun Context.confirmAction(content: String, positiveButton: String = getString(R.string.yes), previousDialog: DialogxBuilder?): DialogxBuilder {
    if (previousDialog != null && previousDialog.instance.isShowing()) return previousDialog
    val dialog = DialogxBuilder(this)
    dialog.setMessage(content)
    dialog.setType(Dialogx.Type.CONFIRMATION)
    dialog.setCancelable(false)
    dialog.setNegativeButton(getString(android.R.string.cancel)) { it.dismiss() }
    dialog.setPositiveButton(positiveButton) { it.dismiss() }
    dialog.build().show()
    return dialog
}

