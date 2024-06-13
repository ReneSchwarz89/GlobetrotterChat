package de.rs.globetrotterchat.android.ui.main.Chat

import de.rs.globetrotterchat.android.ui.main.MainViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.rs.globetrotterchat.android.adapter.ConversationAdapter
import de.rs.globetrotterchat.android.databinding.FragmentConversationsBinding

class ConversationsFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var binding : FragmentConversationsBinding
    private lateinit var conversationAdapter : ConversationAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConversationsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadConversations()


        conversationAdapter = ConversationAdapter(listOf()) { conversationId ->
            val action = ConversationsFragmentDirections.toConversationDetailsFragment(conversationId)
            findNavController().navigate(action)
        }

        binding.rvChats.adapter = conversationAdapter
        binding.rvChats.layoutManager = LinearLayoutManager(context)

        viewModel.conversation.observe(viewLifecycleOwner) { conversations ->
            // Aktualisiere den Adapter mit den neuen Konversationen
            conversationAdapter.updateConversations(conversations)
        }
    }
}