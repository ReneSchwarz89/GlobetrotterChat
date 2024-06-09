package de.rs.globetrotterchat.android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.rs.globetrotterchat.android.data.model.Profile
import de.rs.globetrotterchat.android.databinding.ItemFriendBinding

class FriendsHubAdapter(
    private val profiles: List<Profile>,
    private val onProfileClicked: (String) -> Unit
): RecyclerView.Adapter<FriendsHubAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(private val binding: ItemFriendBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(profile: Profile){
            binding.tvName.text = profile.nickname
        }
    }

    override fun getItemCount(): Int = profiles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.bind(profile)
        holder.itemView.setOnClickListener { onProfileClicked(profile.uid!!) }
    }
}