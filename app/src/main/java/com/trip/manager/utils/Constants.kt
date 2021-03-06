package com.trip.manager.utils

const val TAG = "Sonu"
const val authError = "Firebase authorization failure"
const val databaseError = "Firebase database failure"
const val storageError = "Firebase storage failure"

object FirebasePaths {
    const val users = "Users"
    const val trips = "Trips"
    const val essentials = "Essentials"
    const val members = "Members"
    const val transactions = "Transactions"
}

object TransactionType {
    const val DEBIT = "debit"
    const val CREDIT = "credit"
    fun getCredit() = CREDIT
    fun getDebit() = DEBIT
}