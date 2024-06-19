package de.rs.globetrotterchat.android.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding

    private var imageUri: Uri? = null
    private val languageCodes = arrayOf("en", "zh", "hi", "es", "fr", "ar", "bn", "ru", "pt", "ur", "id", "de", "ja", "sw", "tr", "it", "pl", "nl", "th", "gu")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        } else {
            // Starte den Launcher, da die Berechtigungen bereits erteilt wurden
            pickImageLauncher.launch("image/*")
        }

        viewModel.userProfile.observe(viewLifecycleOwner){userProfile ->
            userProfile?.let {
                binding.etNickName.setText(it.nickname)
                val languages = resources.getStringArray(R.array.native_language_options)
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
                (binding.spNativeLanguage).apply {
                    setAdapter(adapter)
                    setOnItemClickListener { _, _, position, _ ->
                        val selectedLanguageCode = languageCodes[position]
                        binding.spNativeLanguage.setText(selectedLanguageCode)
                    }
                }
                if(it.profilePictureUrl != null){
                    Glide.with(this)
                        .load(userProfile.profilePictureUrl)
                        .into(binding.ivProfile)
                }
            }
        }

        binding.ivProfile.setOnClickListener {
            imageUri?.let { imageUri ->
                onImageSelected(imageUri)
            } ?: run {
                // Starte den Bildauswahlprozess, wenn keine URI vorhanden ist
                pickImageLauncher.launch("image/*")
            }

        }

        binding.btnSaveProfile.setOnClickListener{
            viewModel.setProfile(
                binding.etNickName.text.toString(),
                binding.spNativeLanguage.text.toString())
        }

        binding.btnLogout.setOnClickListener {
            (activity as MainActivity).navigateToLanding()
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
        binding.ivProfile.setImageURI(uri)
        if (uri != null) {
            onImageSelected(uri)
        }
    }

    private fun onImageSelected(imageUri: Uri) {
        viewModel.uploadProfileImage(imageUri)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                pickImageLauncher.launch("image/*")
            } else {
                // Berechtigungen wurden nicht erteilt, handle den Fall entsprechend
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}