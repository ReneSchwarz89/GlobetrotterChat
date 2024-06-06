package de.rs.globetrotterchat.android.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.databinding.ActivityMainBinding
import de.rs.globetrotterchat.android.ui.landing.LandingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.fragmentContainerViewMain.findNavController().navigateUp()
            }
        })
    }

    fun navigateToLanding() {
        val intent = Intent(this, LandingActivity::class.java).apply {
            putExtra(LandingActivity.SHOULD_LOGOUT_KEY,true)
        }
        startActivity(intent)
    }

}
