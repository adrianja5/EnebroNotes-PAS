package com.example.enebronotes.data.cell

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Cell] from a given data source.
 */
interface CellRepository {
    /**
     * Retrieve all the cells by note from the the given data source.
     */
    fun getAllCellsByNoteStream(noteId: Long): Flow<List<Cell>>

    /**
     * Retrieve a cell from the given data source that matches with the [id].
     */
    fun getCellStream(id: Long): Flow<Cell?>

    /**
     * Insert cell in the data source
     */
    suspend fun insertCell(item: Cell): Long

    /**
     * Update cell in the data source
     */
    suspend fun updateCell(item: Cell)

    /**
     * Delete cell from the data source
     */
    suspend fun deleteCell(item: Cell)
}