package com.example.enebronotes.data.cell

import kotlinx.coroutines.flow.Flow

class OfflineCellRepository(private val cellDao: CellDao) : CellRepository {
    override fun getAllCellsByNoteStream(noteId: Long): Flow<List<Cell>> =
        cellDao.getAllCellsByNote(noteId)

    override fun getCellStream(id: Long): Flow<Cell?> = cellDao.getCell(id)

    override suspend fun insertCell(item: Cell): Long = cellDao.insert(item)

    override suspend fun updateCell(item: Cell) = cellDao.update(item)

    override suspend fun deleteCell(item: Cell) = cellDao.delete(item)
}