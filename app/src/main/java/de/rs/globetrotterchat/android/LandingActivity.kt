package de.rs.globetrotterchat.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.rs.globetrotterchat.android.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater,null,false)
        val view = binding.root
        setContentView(view)
    }
}