package de.rs.globetrotterchat.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.rs.globetrotterchat.android.data.model.Conversation
import de.rs.globetrotterchat.android.databinding.ItemChatBinding

class ConversationAdapter(
    private var conversations: List<Conversation>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    class ConversationViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(conversation: Conversation){
            binding.tvNickName.text = conversation.displayName
        }
    }

    override fun getItemCount(): Int = conversations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        holder.bind(conversation)


        holder.itemView.setOnClickListener {
            conversation.conversationId?.let { id ->
                onItemClicked(id)
            }
        }
    }
    fun updateConversations(newConversations: List<Conversation>) {
        conversations = newConversations
        notifyDataSetChanged()
    }
}