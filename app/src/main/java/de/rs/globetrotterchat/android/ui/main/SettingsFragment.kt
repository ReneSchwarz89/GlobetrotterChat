package de.rs.globetrotterchat.android.ui.main

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
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.data.remote.FirebaseService
import de.rs.globetrotterchat.android.databinding.ActivityMainBinding
import de.rs.globetrotterchat.android.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater,null,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.native_language_options, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val autoCompleteTextView = binding.spNativeLanguage
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedLanguage = adapter.getItem(position)
            viewModel.setNativeLanguage(selectedLanguage.toString())
        }

        binding.btnSaveProfile.setOnClickListener{

        }



        binding.btnLogout.setOnClickListener {
            (activity as MainActivity).navigateToLanding()
        }
    }
}