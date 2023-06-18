package com.example.enebronotes.data.folder

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
interface FolderDao {

    @Query("SELECT * from folders ORDER BY title ASC")
    fun getAllFolders(): Flow<List<Folder>>

    @Query("SELECT * from folders WHERE id = :id")
    fun getFolder(id: Long): Flow<Folder>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Folder)

    @Update
    suspend fun update(item: Folder)

    @Delete
    suspend fun delete(item: Folder)
}
