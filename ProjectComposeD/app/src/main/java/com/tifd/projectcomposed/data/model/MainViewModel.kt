package com.tifd.projectcomposed.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tifd.projectcomposed.local.Tugas
import com.tifd.projectcomposed.local.TugasRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public class MainViewModel(private val tugasRepository: TugasRepository) : ViewModel() {
    private val profileRepository = ProfileRepository()

    private val _tugasList = MutableLiveData<List<Tugas>>(emptyList())
    val tugasList: LiveData<List<Tugas>> get() = _tugasList
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchAllTugas()
    }

    private fun fetchAllTugas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val tugas = tugasRepository.getAllTugas()
                _tugasList.value = tugas.value ?: emptyList() // Safeguard for null
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                _tugasList.value = emptyList() // Safeguard for error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTugas(matkul: String, detailTugas: String, imageUri: String?) {
        val newTugas = Tugas(
            matkul = matkul,
            detailTugas = detailTugas,
            imageUri = imageUri,
            selesai = false,
            id = 0 // ID akan di-autogenerate oleh Room
        )
        viewModelScope.launch {
            tugasRepository.insert(newTugas)
            fetchAllTugas() // Refresh daftar tugas setelah menambahkan
        }
    }


    }
