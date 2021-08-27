package com.omran.sqlitedatabaseandroidkotlin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val id: Int = -1, val name: String, val email: String, val password: String):Parcelable
