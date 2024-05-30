package de.rs.globetrotterchat.android.ui.landing

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

    private var email =  ""
    private var password = ""
    private var nickname = ""
    private var nativeLanguage = ""

    enum class SessionState { NEUTRAL, LOGGED_IN, SIGNED_UP, LOGIN_FAILED, SIGNUP_FAILED }

    init {
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
        if (repository.isLoggedIn){
            _sessionState.value = SessionState.LOGGED_IN
        } else {
            _sessionState.value = SessionState.NEUTRAL
        }
    }

    fun signUp() {
        viewModelScope.launch {
            val isSuccess = repository.createUser(email, password)
            _sessionState.value = if (isSuccess) SessionState.SIGNED_UP else SessionState.SIGNUP_FAILED
        }
    }

    fun signIn() {
        viewModelScope.launch {
            val isSuccess = repository.signInUser(email, password)
            _sessionState.value = if (isSuccess) SessionState.LOGGED_IN else SessionState.LOGIN_FAILED
        }
    }

    fun setEmail(email: String){
        this.email = email
    }

    fun setPassword(password: String){
        this.password = password
    }

    fun setNickname(nickname: String){
        this.nickname = nickname
    }

    fun setNativeLanguage(nativeLanguage: String){
        this.nativeLanguage = nativeLanguage
    }
}