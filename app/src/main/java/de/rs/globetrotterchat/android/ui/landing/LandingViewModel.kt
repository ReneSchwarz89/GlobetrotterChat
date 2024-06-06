package de.rs.globetrotterchat.android.ui.landing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.rs.globetrotterchat.android.data.remote.FirebaseAuthService
import kotlinx.coroutines.launch

class LandingViewModel(application: Application): AndroidViewModel(application) {

    private val authService = FirebaseAuthService()

    private val _sessionState = MutableLiveData<SessionState>()
    val sessionState: LiveData<SessionState> get() = _sessionState

    private var email =  ""
    private var password = ""

    sealed interface SessionState {
        sealed class LoggedInOrSignedUp (val uid: String) : SessionState
        data class LoggedIn(val userId: String) : LoggedInOrSignedUp(userId)
        data class SignedUp(val userId: String) : LoggedInOrSignedUp(userId)
        data object LoggedOut : SessionState
        data object Neutral : SessionState
        data object LoginFailed : SessionState
        data object SignupFailed : SessionState
    }

    fun checkUserLoginStatus() {
            _sessionState.value = authService.userId
                ?.let { uid -> SessionState.LoggedIn(uid) }
                ?: SessionState.Neutral
    }

    fun signUp() {
        viewModelScope.launch {
            authService.createUserWithEmailAndPassword(email, password)
            _sessionState.value = authService.userId
                ?.let { uid -> SessionState.SignedUp(uid) }
                ?: SessionState.SignupFailed
        }
    }

    fun signIn() {
        viewModelScope.launch {
            authService.signInWithEmailAndPassword(email, password)
            _sessionState.value = authService.userId
                ?.let { uid -> SessionState.LoggedIn(uid) }
                ?: SessionState.LoginFailed
        }
    }

    fun logoutUser(){
        viewModelScope.launch {
            authService.signOut()
            _sessionState.value = SessionState.LoggedOut
        }
    }

    fun setEmail(email: String){
        this.email = email
    }

    fun setPassword(password: String){
        this.password = password
    }
}