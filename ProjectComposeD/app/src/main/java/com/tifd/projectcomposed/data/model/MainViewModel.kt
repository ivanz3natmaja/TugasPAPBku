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

class MainViewModel(private val tugasRepository: TugasRepository) : ViewModel() {
    private val profileRepository = ProfileRepository()

    private val _tugasList = MutableLiveData<List<Tugas>>()
    val tugasList: LiveData<List<Tugas>> get() = _tugasList

    private val _user = MutableLiveData<Profile?>(null)
    val user: LiveData<Profile?> = _user

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchAllTugas()
    }

    private fun fetchAllTugas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val tugas = tugasRepository.getAllTugas()
                _tugasList.value = tugas.value
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                _tugasList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTugas(matkul: String, detailTugas: String) {
        val newTugas = Tugas(
            matkul = matkul,
            detailTugas = detailTugas,
            selesai = false,
            id = 0 // ID akan di-autogenerate oleh Room
        )
        viewModelScope.launch {
            tugasRepository.insert(newTugas)
            fetchAllTugas() // Refresh daftar tugas setelah menambahkan
        }
    }

    fun getProfileUser(username: String) {viewModelScope.launch {
        _isLoading.value = true
        try {
            val profile = profileRepository.getProfile(username)
            _user.value = profile
            _error.value = null
        } catch (e: Exception) {
            _error.value = e.message
            _user.value = null
        } finally {
            _isLoading.value = false
        }
    }
    }
}