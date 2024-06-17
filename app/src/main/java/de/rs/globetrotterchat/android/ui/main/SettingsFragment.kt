package de.rs.globetrotterchat.android.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding

    private val languageCodes = arrayOf("en", "zh", "hi", "es", "fr", "ar", "bn", "ru", "pt", "ur", "id", "de", "ja", "sw", "tr", "it", "pl", "nl", "th", "gu")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userProfile.observe(viewLifecycleOwner){userProfile ->
            userProfile?.let {
                binding.etNickName.setText(it.nickname)
                binding.spNativeLanguage.setText(it.nativeLanguage)
            }
        }

        binding.ivProfile.setOnClickListener {
        }

        val languages = resources.getStringArray(R.array.native_language_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, languages)
        (binding.spNativeLanguage).apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                val selectedLanguageCode = languageCodes[position]
                // Hier kannst du den ausgewählten Sprachcode speichern oder verwenden
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

}