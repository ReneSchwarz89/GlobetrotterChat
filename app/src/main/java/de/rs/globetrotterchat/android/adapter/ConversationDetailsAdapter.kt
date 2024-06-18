package de.rs.globetrotterchat.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.rs.globetrotterchat.android.R
import de.rs.globetrotterchat.android.data.model.Message
import de.rs.globetrotterchat.android.databinding.ItemChatInBinding
import de.rs.globetrotterchat.android.databinding.ItemChatOutBinding
import de.rs.globetrotterchat.android.ui.main.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class ConversationDetailsAdapter(
    var messages: MutableList<Message> = mutableListOf(),
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_IN = 1
        const val VIEW_TYPE_OUT = 2
    }

    inner class MessageInViewHolder(val binding: ItemChatInBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.btnSetVisibilityIn.setOnClickListener{
                binding.tvOptionalIn.visibility = if (binding.tvOptionalIn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                binding.btnSetVisibilityIn.alpha = if (binding.tvOptionalIn.visibility == View.VISIBLE) 0.7f else 0.3f
            }
        }
    }

    inner class MessageOutViewHolder(val binding: ItemChatOutBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.btnSetVisibilityOut.setOnClickListener{
                binding.tvOptionalOut.visibility = if (binding.tvOptionalOut.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                binding.btnSetVisibilityOut.alpha = if (binding.tvOptionalOut.visibility == View.VISIBLE) 0.7f else 0.3f
            }
        }
    }

    override fun getItemCount() = messages.size

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.senderId == viewModel.userProfile.value!!.uid) VIEW_TYPE_OUT else VIEW_TYPE_IN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_IN) {
            val binding = ItemChatInBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MessageInViewHolder(binding)
        } else {
            val binding = ItemChatOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MessageOutViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        when (holder) {
            is MessageInViewHolder -> {
                with(holder.binding){
                    tvChatIn.text = message.translatedText
                    tvTimeStamp.text = dateFormat.format(Date(message.timestamp))
                    tvOptionalIn.text = message.senderText
                }
            }
            is MessageOutViewHolder -> {
                with(holder.binding){
                    tvChatOut.text = message.senderText
                    tvTimeStamp.text = dateFormat.format(Date(message.timestamp))
                    tvOptionalOut.text = message.translatedText
                }
            }
        }
    }
}
