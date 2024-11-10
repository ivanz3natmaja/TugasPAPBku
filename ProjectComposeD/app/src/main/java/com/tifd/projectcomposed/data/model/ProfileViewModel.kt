package com.tifd.projectcomposed.data.model;
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
public class ProfileViewModel : ViewModel() {
    private val profileRepository = ProfileRepository()
    private val _user = MutableStateFlow<Profile?>(null)
    val user: StateFlow<Profile?> = _user
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun getProfileUser(username: String) {
        viewModelScope.launch {
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