package com.example.enebronotes.data.folder

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Note] from a given data source.
 */
interface FolderRepository {
    /**
     * Retrieve all the notes from the the given data source.
     */
    fun getAllFoldersStream(): Flow<List<Folder>>

    /**
     * Retrieve a note from the given data source that matches with the [id].
     */
    fun getFolderStream(id: Long): Flow<Folder?>

    /**
     * Insert note in the data source
     */
    suspend fun insertFolder(item: Folder)

    /**
     * Delete note from the data source
     */
    suspend fun deleteFolder(item: Folder)

    /**
     * Update note in the data source
     */
    suspend fun updateFolder(item: Folder)
}