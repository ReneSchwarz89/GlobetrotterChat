package de.rs.globetrotterchat.android.ui.landing.loginSignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import de.rs.globetrotterchat.android.databinding.FragmentSignupBinding
import de.rs.globetrotterchat.android.ui.landing.LandingViewModel

class SignUpFragment : Fragment() {

    private val viewModel: LandingViewModel by activityViewModels()
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            viewModel.setEmail(binding.etEmail.text.toString())
            viewModel.setPassword(binding.etPassword.text.toString())
            viewModel.setNickname(binding.etNickName.text.toString())
            viewModel.setNativeLanguage(binding.spNativeLanguage.toString())
            viewModel.signUp()
        }

        binding.btnBackToLogin.setOnClickListener {

        }
    }
}