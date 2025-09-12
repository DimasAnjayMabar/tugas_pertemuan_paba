package com.example.test3

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pegawai(
    val nip : Int,
    val nama : String?,
    val dept : String?
) : Parcelable
