package de.rs.globetrotterchat.android.ui.main.friendsHub

import de.rs.globetrotterchat.android.ui.main.MainViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.rs.globetrotterchat.android.adapter.FriendsHubAdapter
import de.rs.globetrotterchat.android.databinding.FragmentFriendsHubBinding

class FriendsHubFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentFriendsHubBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFriendsHubBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProfiles()

        viewModel.profiles.observe(viewLifecycleOwner){profiles ->
            val adapter = FriendsHubAdapter(profiles){ clickedProfileUid ->
                val displayName = profiles.find { it.uid == clickedProfileUid }?.nickname!!
                viewModel.createOrGetChatRoom(clickedProfileUid, displayName)
                viewModel.currentConversationId.observe(viewLifecycleOwner) { conversationId ->
                    if (conversationId.isNotEmpty()) {
                        val action = FriendsHubFragmentDirections.friendsHubFragmentToConversationDetailsFragment(conversationId)
                        findNavController().navigate(action)
                    }
                }
            }
            binding.rvFriendsHub.adapter = adapter
        }
    }
}