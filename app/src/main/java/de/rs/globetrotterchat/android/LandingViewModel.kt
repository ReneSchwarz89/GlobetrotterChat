package de.rs.globetrotterchat.android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.rs.globetrotterchat.android.data.Repository
import de.rs.globetrotterchat.android.data.remote.FirebaseService
import kotlinx.coroutines.launch

class LandingViewModel(application: Application): AndroidViewModel(application) {

    private val repository = Repository(FirebaseService())

    private val _sessionState = MutableLiveData<SessionState>()
    val sessionState: LiveData<SessionState> get() = _sessionState

    enum class SessionState { LOGGED_IN, REGISTERED, FAILED }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val isSuccess = repository.createUser(email, password)
            _sessionState.value = if (isSuccess) SessionState.REGISTERED else SessionState.FAILED
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val isSuccess = repository.signInUser(email, password)
            _sessionState.value = if (isSuccess) SessionState.LOGGED_IN else SessionState.FAILED
        }
    }

    fun signOut() {
        repository.signOut()
    }
}