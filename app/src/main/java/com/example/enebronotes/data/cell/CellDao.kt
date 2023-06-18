package com.example.enebronotes.data.cell

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDao {
    @Query("SELECT * FROM cells WHERE note_id = :noteId ORDER BY id ASC")
    fun getAllCellsByNote(noteId: Long): Flow<List<Cell>>

    @Query("SELECT * from cells WHERE id = :id")
    fun getCell(id: Long): Flow<Cell>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Cell): Long

    @Update
    suspend fun update(item: Cell)

    @Delete
    suspend fun delete(item: Cell)
}