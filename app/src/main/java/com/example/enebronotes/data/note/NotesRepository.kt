package com.example.enebronotes.data.note

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Note] from a given data source.
 */
interface NotesRepository {
    /**
     * Retrieve all the notes from the the given data source.
     */
    fun getAllNotesByFolderStream(folderId: Long): Flow<List<Note>>

    /**
     * Retrieve all the notes from the the given data source.
     */
    fun getAllNotesStream(): Flow<List<Note>>

    /**
     * Retrieve a note from the given data source that matches with the [id].
     */
    fun getNoteStream(id: Long): Flow<Note?>

    /**
     * Insert note in the data source
     */
    suspend fun insertNote(item: Note): Long

    /**
     * Delete note from the data source
     */
    suspend fun deleteNote(item: Note)

    /**
     * Update note in the data source
     */
    suspend fun updateNote(item: Note)
}