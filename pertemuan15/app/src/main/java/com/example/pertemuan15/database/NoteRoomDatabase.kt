package com.example.pertemuan15.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun funNoteDao() : NoteDAO

    companion object{
        @Volatile
        private var INSTANCE : NoteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context) : NoteRoomDatabase {
            if (INSTANCE == null){
                synchronized(NoteRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NoteRoomDatabase::class.java,
                        "note_db"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as NoteRoomDatabase
        }
    }
}