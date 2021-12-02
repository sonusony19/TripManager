package com.trip.manager.listeners

import com.trip.manager.dialogx.Dialogx
import java.util.*

fun interface DateTimeListener {
    fun onSelect(date: Date)
}


fun interface DialogxListener {
    fun onClick(dialog: Dialogx)
}