package de.rs.globetrotterchat.android.ui.main.friendsHub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.adapter.FriendsHubAdapter
import de.rs.globetrotterchat.android.databinding.FragmentFriendsHubBinding
import de.rs.globetrotterchat.android.ui.main.MainViewModel

class FriendsHubFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentFriendsHubBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFriendsHubBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        viewModel.getProfiles()

        viewModel.profiles.observe(viewLifecycleOwner){ profiles ->
            binding.rvFriendsHub.adapter = FriendsHubAdapter(profiles)
        }

         */
    }
}