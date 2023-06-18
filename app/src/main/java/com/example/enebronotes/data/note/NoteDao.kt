package com.example.enebronotes.data.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface NoteDao {

    @Query("SELECT * from notes WHERE folderId = :folderId ORDER BY title ASC")
    fun getAllNotesByFolder(folderId: Long): Flow<List<Note>>

    @Query("SELECT * from notes ORDER BY title ASC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * from notes WHERE id = :id")
    fun getNote(id: Long): Flow<Note>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Note): Long

    @Update
    suspend fun update(item: Note)

    @Delete
    suspend fun delete(item: Note)
}
