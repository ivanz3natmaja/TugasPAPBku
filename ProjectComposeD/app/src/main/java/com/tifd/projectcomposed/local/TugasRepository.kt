package com.tifd.projectcomposed.local

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TugasRepository(application: Application)
    {private val mTugasDao: TugasDao
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        val db = TugasDB.getDatabase(application)
        mTugasDao = db.tugasDao()
    }

    fun getAllTugas(): LiveData<List<Tugas>> = mTugasDao.getAllTugas()

    fun insert(tugas: Tugas) {
        coroutineScope.launch {
            mTugasDao.insert(tugas)
        }
    }
}