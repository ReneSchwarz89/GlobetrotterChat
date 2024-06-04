package de.rs.globetrotterchat.android.ui.landing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import de.rs.globetrotterchat.android.ui.main.MainActivity
import de.rs.globetrotterchat.android.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding
    private val viewModel: LandingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.getBooleanExtra(SHOULD_LOGOUT_KEY,false)){
            viewModel.logoutUser()
        } else{
            viewModel.checkUserLoginStatus()
        }

        viewModel.sessionState.observe(this){ sessionState ->
            showDebugMessage(sessionState)
            when (sessionState){
                is LandingViewModel.SessionState.LoggedInOrSignedUp -> {
                    proceedToMainApp(sessionState.uid)
                } else -> {}
            }
        }

        binding = ActivityLandingBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)
    }

    private fun proceedToMainApp(uid: String){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("uid", uid)
        }
        startActivity(intent)
    }

    private fun showDebugMessage(state: LandingViewModel.SessionState) {
        val message = when (state) {
            is LandingViewModel.SessionState.Neutral -> "Welcome"
            is LandingViewModel.SessionState.LoggedIn -> "You successfully logged in!"
            is LandingViewModel.SessionState.SignedUp -> "You successfully registered!"
            is LandingViewModel.SessionState.LoggedOut -> "You logged out."
            is LandingViewModel.SessionState.LoginFailed -> "Login failed!"
            is LandingViewModel.SessionState.SignupFailed -> "SignUp Failed"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    companion object{
        const val SHOULD_LOGOUT_KEY = "SHOULD_LOGOUT"
    }
}
