package com.example.livret.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDatabaseDao {

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * from note_table WHERE noteId = :key")
    suspend fun get(key: Long): Note?

    @Query("DELETE FROM note_table")
    suspend fun clear()

    @Query("SELECT * FROM note_table ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * from note_table WHERE noteId = :key")
    fun getLive(key: Long): LiveData<Note>

    @Query("SELECT * FROM note_table ORDER BY noteId DESC LIMIT 1")
    suspend fun getLastAdded(): Note?
}