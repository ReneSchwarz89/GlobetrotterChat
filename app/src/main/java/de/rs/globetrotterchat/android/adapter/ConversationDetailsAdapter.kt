package de.rs.globetrotterchat.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.data.model.Message
import de.rs.globetrotterchat.android.databinding.ItemChatInBinding
import de.rs.globetrotterchat.android.databinding.ItemChatOutBinding

class ConversationDetailsAdapter(private var messages: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_IN = 1
        const val VIEW_TYPE_OUT = 2
    }

    inner class MessageInViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemChatInBinding.bind(view)
        val tvMessageIn: TextView = view.findViewById(R.id.tvChatIn)
    }

    inner class MessageOutViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemChatOutBinding.bind(view)
        val tvMessageOut: TextView = view.findViewById(R.id.tvChatOut)
    }

    fun sendMessage(message: Message){
        messages.add(message)
        notifyItemInserted(messages.size-1)
    }



    override fun getItemCount() = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].incoming) VIEW_TYPE_IN else VIEW_TYPE_OUT
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_IN) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_in, parent, false)
            MessageInViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_out, parent, false)
            MessageOutViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messages = messages[position]
        when (holder) {
            is MessageInViewHolder -> {
                //holder.tvMessageIn.text = messages.text

            }
            is MessageOutViewHolder -> {
                //holder.tvMessageOut.text = messages.text

            }
        }
    }
}