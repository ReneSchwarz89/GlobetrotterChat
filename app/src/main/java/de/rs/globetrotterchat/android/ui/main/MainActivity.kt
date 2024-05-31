package de.rs.globetrotterchat.android.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.databinding.ActivityMainBinding
import de.rs.globetrotterchat.android.ui.landing.LandingActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)

        intent.getStringExtra("uid")?.run(viewModel::setUid)
            ?: run(::navigateToLanding)
    }

    private fun navigateToLanding(){
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
    }

}