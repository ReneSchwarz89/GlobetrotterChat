package de.rs.globetrotterchat.android.ui.main.Chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import de.rs.globetrotterchat.android.databinding.ActivityMainBinding
import de.rs.globetrotterchat.android.databinding.FragmentChatsBinding
import de.rs.globetrotterchat.android.ui.main.MainViewModel

class ChatsFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()
    private lateinit var binding : FragmentChatsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatsBinding.inflate(inflater,null,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setProfile()

    }
}