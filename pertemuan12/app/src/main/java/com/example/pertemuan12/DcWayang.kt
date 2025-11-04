package com.example.pertemuan12

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DcWayang(
    var foto : String,
    var nama : String,
    var karakter : String,
    var deskripsi : String
):Parcelable
