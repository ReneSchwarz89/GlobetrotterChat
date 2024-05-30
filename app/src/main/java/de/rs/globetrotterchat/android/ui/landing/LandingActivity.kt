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
        binding = ActivityLandingBinding.inflate(layoutInflater,null,false)
        val view = binding.root
        setContentView(view)

        viewModel.sessionState.observe(this){ sessionState ->
            showDebugMessage(sessionState)
            when (sessionState){
                LandingViewModel.SessionState.LOGGED_IN,
                LandingViewModel.SessionState.SIGNED_UP -> {
                    proceedToMainApp()
                }
                else -> {}
            }
        }
    }

    private fun proceedToMainApp(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showDebugMessage(state: LandingViewModel.SessionState) {
        val message = when (state) {
            LandingViewModel.SessionState.NEUTRAL -> "Welcome"
            LandingViewModel.SessionState.LOGGED_IN -> "You successfully logged in!"
            LandingViewModel.SessionState.SIGNED_UP -> "You successfully registered!"
            LandingViewModel.SessionState.LOGIN_FAILED -> "Login failed!"
            LandingViewModel.SessionState.SIGNUP_FAILED -> "SignUp Failed"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}