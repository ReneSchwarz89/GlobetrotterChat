package de.rs.globetrotterchat.android.ui.main.Chat

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.adapter.ConversationDetailsAdapter
import de.rs.globetrotterchat.android.databinding.FragmentConversationDetailsBinding
import de.rs.globetrotterchat.android.ui.main.MainActivity
import de.rs.globetrotterchat.android.ui.main.MainViewModel

class ConversationDetailsFragment : Fragment() {

    private lateinit var binding: FragmentConversationDetailsBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val args: ConversationDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConversationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavigation()
        viewModel.startListeningForMessages(args.conversationId)

        val adapter = ConversationDetailsAdapter(mutableListOf(),viewModel)
        binding.rvMessages.adapter = adapter

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            //binding.rvMessages.scrollToPosition(adapter.itemCount - 1)

            val screenHeight = binding.root.height
            val keypadHeight = screenHeight - rect.bottom
            if (keypadHeight > screenHeight * 0.3) {
                binding.rvMessages.post {
                    binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.conversation.observe(viewLifecycleOwner){ conversation ->
            val currentConversation = conversation.find { it.conversationId == args.conversationId }
            binding.tvDisplayName.text = currentConversation?.displayName
            if (currentConversation != null) {
                Glide.with(binding.ivProfile)
                    .load(currentConversation.displayPictureUrl)
                    .placeholder(R.drawable.profile)
                    .into(binding.ivProfile)
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) { messages ->

            (binding.rvMessages.adapter as ConversationDetailsAdapter).messages = messages.toMutableList()
            binding.rvMessages.adapter?.notifyDataSetChanged()
            // Scrolle zum Ende der Nachrichtenliste
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
        }

        binding.btSend.setOnClickListener{
            val messageText = binding.etMessage.text.toString()
            if (messageText.isNotEmpty()){
                viewModel.sendMessage(messageText,args.conversationId)
                binding.etMessage.text?.clear()
                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }

        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomNavigation()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(null)
    }

}