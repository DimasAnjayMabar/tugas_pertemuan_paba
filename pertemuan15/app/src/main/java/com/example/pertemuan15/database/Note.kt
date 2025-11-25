package com.example.pertemuan15.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0,

    @ColumnInfo(name = "judul")
    var judul : String? = null,

    @ColumnInfo(name = "deskripsi")
    var deskripsi : String? = null,

    @ColumnInfo(name = "tanggal")
    var tanggal : String? = null
)
