package de.rs.globetrotterchat.android.ui.main

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import de.rs.globetrotterchat.android.data.remote.FirestoreProfileService
import de.rs.globetrotterchat.android.data.remote.FirestoreStorageService
import de.rs.globetrotterchat.android.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding
    private val loggedInUserId = viewModel.userProfile.value!!.uid!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userProfile.observe(viewLifecycleOwner){userProfile ->
            userProfile?.let {
                binding.etNickName.setText(it.nickname)
                binding.etNativeLanguage.setText(it.nativeLanguage)
            }
        }

        binding.ivProfile.setOnClickListener {
            //showImagePickerOptions()
        }

        binding.btnSaveProfile.setOnClickListener{

            viewModel.setProfile(
                binding.etNickName.text.toString(),
                binding.etNativeLanguage.text.toString())
        }

        binding.btnLogout.setOnClickListener {
            (activity as MainActivity).navigateToLanding()
        }
    }

}