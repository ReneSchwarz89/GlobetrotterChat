package de.rs.globetrotterchat.android.ui.landing.loginSignUp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.rs.globetrotterchat.android.R
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


        //binding.spNativeLanguage.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, viewModel)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                binding.ivProfile.setImageURI(uri)
                viewModel.setProfileImage(uri.toString())
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.ivProfile.setOnClickListener{
            val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            pickMedia.launch(request)
        }

        binding.btnSignUp.setOnClickListener {
            viewModel.setEmail(binding.etEmail.text.toString())
            viewModel.setPassword(binding.etPassword.text.toString())
            viewModel.setNickname(binding.etNickName.text.toString())
            viewModel.setNativeLanguage(binding.spNativeLanguage.toString())
            viewModel.setProfileImage(binding.ivProfile.toString())
            viewModel.signUp()
        }

        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

    }
}