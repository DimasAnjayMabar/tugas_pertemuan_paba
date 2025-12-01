package com.example.pertemuan15.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    @Query("UPDATE note SET judul = :isi_judul, deskripsi = :isi_deskripsi WHERE id = :isi_id")
    fun update(isi_judul: String, isi_deskripsi: String, isi_id: Int)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM note ORDER BY id ASC")
    fun selectAll(): MutableList<Note>

    // Tambahkan ini untuk get note by ID
    @Query("SELECT * FROM note WHERE id = :id")
    fun getNoteById(id: Int): Note?

    // Optional: Untuk delete by ID
    @Query("DELETE FROM note WHERE id = :id")
    fun deleteById(id: Int)
}