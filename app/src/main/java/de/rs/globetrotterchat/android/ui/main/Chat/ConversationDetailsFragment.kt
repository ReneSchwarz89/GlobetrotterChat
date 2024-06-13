package de.rs.globetrotterchat.android.ui.main.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.rs.globetrotterchat.android.adapter.ConversationDetailsAdapter
import de.rs.globetrotterchat.android.databinding.FragmentConversationDetailsBinding
import de.rs.globetrotterchat.android.ui.main.MainViewModel

class ConversationDetailsFragment : Fragment() {

    private lateinit var binding: FragmentConversationDetailsBinding
    private val viewModel: MainViewModel by activityViewModels()

    private val args: ConversationDetailsFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConversationDetailsBinding.inflate(inflater, container, false)
        viewModel.loadMessages(this.args.conversationId)
        return binding.root
    }


    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = ConversationDetailsAdapter(mutableListOf(),viewModel)
        binding.rvMessages.adapter = adapter

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            adapter.messages = messages.toMutableList()
            adapter.notifyDataSetChanged()
        }

        binding.btSend.setOnClickListener{
            val messageText = binding.etMessage.text.toString()

            viewModel.sendMessage(messageText,args.conversationId)
        }


        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }

    }

}