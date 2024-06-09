package de.rs.globetrotterchat.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.rs.globetrotterchat.android.data.model.Conversation
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.databinding.ItemChatBinding
import kotlinx.coroutines.processNextEventInCurrentThread
import kotlinx.coroutines.tasks.await

class ConversationAdapter(
    private val conversations: List<Conversation>
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    class ConversationViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(conversation: Conversation){
            binding.tvNickName.text = "test"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        holder.bind(conversation)
    }

    override fun getItemCount(): Int = conversations.size
}