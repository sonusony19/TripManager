@file:Suppress("SpellCheckingInspection")

package com.trip.manager.dialogx

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import com.kaptain.hr.dialogx.DialogxBuilder
import com.trip.manager.R
import com.trip.manager.databinding.DialogxLayoutBinding
import com.trip.manager.listeners.DialogxListener


class Dialogx {
    private val dialog: AppCompatDialog
    private var positiveListener: DialogxListener? = null
    private var negativeListener: DialogxListener? = null
    private var negativeExist = false
    private val context: Context?
    private val builder: DialogxBuilder
    var dialogxBinding: DialogxLayoutBinding


    constructor(builder: DialogxBuilder, context: Context?, message: String?, cancelable: Boolean) {
        negativeExist = false
        this.context = context
        this.builder = builder
        dialog = AppCompatDialog(context, R.style.DialogxStyle)
        dialog.setContentView(R.layout.dialogx_layout)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialogxBinding = DialogxLayoutBinding.bind(dialog.findViewById(R.id.dialog_layout)!!)
        dialog.setCancelable(cancelable)
        setMessage(message)
        initEvents()
    }

    constructor(builder: DialogxBuilder, context: Context?) {
        this.builder = builder
        this.context = context
        dialog = AppCompatDialog(context, R.style.DialogxStyle)
        dialog.setContentView(R.layout.dialogx_layout)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialogxBinding = DialogxLayoutBinding.bind(dialog.findViewById(R.id.dialog_layout)!!)
    }

    private fun setMessage(message: String?) {
        if (message == null || message.isEmpty()) {
            dialogxBinding.message.visibility = View.GONE
        } else {
            dialogxBinding.message.text = message
        }
    }

    fun getDialog(): AppCompatDialog {
        return dialog
    }

    fun setPositive(okLabel: String, listener: DialogxListener?) {
        positiveListener = listener
        setPositiveLabel(okLabel)
    }

    fun setNegative(koLabel: String, listener: DialogxListener?) {
        negativeListener = listener
        negativeExist = true
        setNegativeLabel(koLabel)
    }

    fun setPositiveAction(listener: DialogxListener?) {
        positiveListener = listener
        builder.positiveListener = listener
    }

    fun setNegativeAction(listener: DialogxListener?) {
        negativeListener = listener
        builder.negativeListener = listener
        negativeExist = true
    }

    fun show() {
        dialogxBinding.negativeButton.visibility = if (negativeExist) View.VISIBLE else View.GONE
        dialogxBinding.positiveButton.visibility = if (dialogxBinding.positiveButton.text.isNotEmpty()) View.VISIBLE else View.GONE
        if (context != null && !(context as Activity).isFinishing && !context.isDestroyed) dialog.show()
    }

    fun dismiss() {
        if (context != null && !(context as Activity).isFinishing && !context.isDestroyed) dialog.dismiss()
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    private fun setPositiveLabel(positive: String) {
        dialogxBinding.positiveButton.text = positive
    }

    private fun setNegativeLabel(negative: String) {
        dialogxBinding.negativeButton.text = negative
    }

    fun setBoldPositiveLabel(bold: Boolean) {
        dialogxBinding.positiveButton.setTypeface(null, if (bold) Typeface.BOLD else Typeface.NORMAL)
    }

    fun setTypefaces(font: Typeface?) {
        if (font == null) return
        dialogxBinding.message.typeface = font
        dialogxBinding.positiveButton.typeface = font
        dialogxBinding.negativeButton.typeface = font
    }

    fun setType(type: Type) {
        dialogxBinding.animation.setAnimation(when (type) {
            Type.CONFIRMATION -> R.raw.confirmation
            else -> R.raw.success
        })
    }

    private fun initEvents() {
        dialogxBinding.positiveButton.setOnClickListener { takePositiveAction() }
        dialogxBinding.negativeButton.setOnClickListener { takeNegativeAction() }
    }

    private fun takeNegativeAction() {
        if (negativeListener != null) negativeListener!!.onClick(this)
    }

    private fun takePositiveAction() {
        if (positiveListener != null) positiveListener!!.onClick(this)
    }

    enum class Type {
        CONFIRMATION, SUCCESS
    }
}