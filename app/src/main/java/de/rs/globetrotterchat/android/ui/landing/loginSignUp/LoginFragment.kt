package de.rs.globetrotterchat.android.ui.landing.loginSignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.databinding.FragmentLoginBinding
import de.rs.globetrotterchat.android.ui.landing.LandingViewModel

class LoginFragment : Fragment() {

    private val viewModel: LandingViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater,null,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            viewModel.setEmail(binding.etEmail.text.toString())
            viewModel.setPassword(binding.etPassword.text.toString())
            viewModel.signIn()
        }

        binding.btnSignUp.setOnClickListener {
            viewModel.setEmail(binding.etEmail.text.toString())
            viewModel.setPassword(binding.etPassword.text.toString())
            navigateToSignUp()
        }
    }

    private fun navigateToSignUp() {
        findNavController().navigate(R.id.signUpFragment)
    }
}