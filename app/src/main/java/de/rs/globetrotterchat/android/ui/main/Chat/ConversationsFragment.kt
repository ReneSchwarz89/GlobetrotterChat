package de.rs.globetrotterchat.android.ui.main.Chat

import de.rs.globetrotterchat.android.ui.main.MainViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import de.rs.globetrotterchat.android.adapter.ConversationAdapter
import de.rs.globetrotterchat.android.databinding.FragmentConversationsBinding

class ConversationsFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var binding : FragmentConversationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConversationsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadConversations()


        viewModel.conversation.observe(viewLifecycleOwner) { conversation ->
            binding.rvChats.adapter = ConversationAdapter(conversation)

        }
    }
}