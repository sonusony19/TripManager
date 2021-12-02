@file:Suppress("SpellCheckingInspection")

package com.kaptain.hr.dialogx

import android.content.Context
import android.graphics.Typeface
import com.trip.manager.dialogx.Dialogx
import com.trip.manager.listeners.DialogxListener

class DialogxBuilder(private val context: Context) {
    private var tf: Typeface? = null
    private var bold = true
    private var cancelable = true
    private var message: String? = null
    private var positiveButton: String? = null
    private var negativeButton: String? = null
    internal var positiveListener: DialogxListener? = null
    internal var negativeListener: DialogxListener? = null
    private var type = Dialogx.Type.CONFIRMATION

    var instance: Dialogx
        private set

    fun setType(type: Dialogx.Type): DialogxBuilder {
        this.type = type
        return this
    }

    fun setMessage(message: String?): DialogxBuilder {
        this.message = message
        return this
    }

    fun boldPositiveButton(bold: Boolean): DialogxBuilder {
        this.bold = bold
        return this
    }

    fun setFont(font: Typeface?): DialogxBuilder {
        tf = font
        return this
    }

    fun setCancelable(cancelable: Boolean): DialogxBuilder {
        this.cancelable = cancelable
        return this
    }

    fun setNegativeButton(negativeButton: String?, listener: DialogxListener?): DialogxBuilder {
        negativeListener = listener
        this.negativeButton = negativeButton
        return this
    }

    fun setPositiveButton(positiveButton: String?, listener: DialogxListener?): DialogxBuilder {
        positiveListener = listener
        this.positiveButton = positiveButton
        return this
    }

    fun setPositiveButton(positiveButton: String?) {
        this.positiveButton = positiveButton
    }

    fun setNegativeButton(negativeButton: String?) {
        this.negativeButton = negativeButton
    }

    fun build(): Dialogx {
        instance = Dialogx(this, context, message, cancelable)
        instance.setBoldPositiveLabel(bold)
        instance.setTypefaces(tf)
        instance.setType(type)
        if (negativeButton != null) {
            instance.setNegative(negativeButton!!, negativeListener)
        }
        if (positiveButton != null) {
            instance.setPositive(positiveButton!!, positiveListener)
        }
        return instance
    }

    init {
        instance = Dialogx(this@DialogxBuilder, context)
    }
}