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

    /**
     * Definiert die möglichen Zustände einer Benutzersitzung.
     *
     * Diese versiegelte Schnittstelle (`sealed interface`) ermöglicht eine begrenzte Hierarchie von Zuständen,
     * die eine Benutzersitzung annehmen kann. Jeder Zustand gibt Auskunft über den aktuellen Stand der Sitzung,
     * wie zum Beispiel ob der Benutzer eingeloggt, angemeldet, ausgeloggt oder in einem neutralen Zustand ist.
     */
    sealed interface SessionState {

        /**
         * Basisklasse für Zustände, in denen der Benutzer eingeloggt oder angemeldet ist.
         * @param uid Die eindeutige Benutzer-ID.
         */
        sealed class LoggedInOrSignedUp (val uid: String) : SessionState

        /**
         * Zustand, wenn der Benutzer erfolgreich eingeloggt ist.
         * @param userId Die eindeutige Benutzer-ID des eingeloggten Benutzers.
         */
        data class LoggedIn(val userId: String) : LoggedInOrSignedUp(userId)

        /**
         * Zustand, wenn der Benutzer erfolgreich angemeldet ist.
         * @param userId Die eindeutige Benutzer-ID des angemeldeten Benutzers.
         */
        data class SignedUp(val userId: String) : LoggedInOrSignedUp(userId)

        /**
         * Zustand, wenn der Benutzer nicht eingeloggt ist.
         */
        data object LoggedOut : SessionState

        /**
         * Neutraler Zustand, typischerweise vor dem Login oder nach dem Logout.
         */
        data object Neutral : SessionState

        /**
         * Zustand, wenn der Login-Versuch fehlgeschlagen ist.
         */
        data object LoginFailed : SessionState

        /**
         * Zustand, wenn der Anmeldeversuch fehlgeschlagen ist.
         */
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