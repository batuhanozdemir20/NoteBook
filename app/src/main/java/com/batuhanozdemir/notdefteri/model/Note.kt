package com.batuhanozdemir.notdefteri.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    @ColumnInfo(name = "note_title") var title: String,
    @ColumnInfo(name = "note_content") var content: String
): Serializable{
    @PrimaryKey(autoGenerate = true) var id = 0
    @ColumnInfo(name = "created_at") var createdAt: Long = System.currentTimeMillis()
}