package com.example.enebronotes.data.cell

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.enebronotes.data.note.Note

@Entity(
    tableName = "cells",
    foreignKeys = [ForeignKey(
        entity = Note::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("note_id"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["note_id"])]
)
data class Cell(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    @ColumnInfo(name = "media_uri") val mediaUri: String?,
    @ColumnInfo(name = "note_id") val noteId: Long,
)