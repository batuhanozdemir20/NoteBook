package com.batuhanozdemir.notdefteri.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.batuhanozdemir.notdefteri.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDAO

}