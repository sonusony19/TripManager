package com.trip.manager.ui.trip.model

import com.trip.manager.R
import com.trip.manager.utils.TransactionType
import com.trip.manager.utils.getStringResource


data class Trip(
        var id: String = "",
        var name: String? = null,
        var createdAt: Long = 0L,
        var members: List<String> = arrayListOf()
)

data class Member(
        var id: String = "",
        var name: String = "",
        var profilePic: String = "",
        var balance: Double = 0.0,
) {
    fun getTotalBalance() = String.format(getStringResource(R.string.balance), (balance).toString())
}

data class Essential(
        var id: String = "",
        var name: String = "",
        var createdAt: Long = 0L,
        var handledAt: Long = 0L,
        var updatedAt: Long = 0L,
        var responsible: String = "",
        var handledBy: String = ""
)

data class Transaction(
        var id: String = "",
        var amount: String = "",
        var title: String = "",
        var description: String = "",
        var createdAt: Long = 0L,
        var type: String = TransactionType.CREDIT
)