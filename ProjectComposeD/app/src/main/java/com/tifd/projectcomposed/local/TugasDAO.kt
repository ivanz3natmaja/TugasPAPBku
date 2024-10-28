package com.tifd.projectcomposed.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TugasDao {
    @Query("SELECT * FROM Tugas")
    fun getAllTugas(): LiveData<List<Tugas>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tugas: Tugas)
}