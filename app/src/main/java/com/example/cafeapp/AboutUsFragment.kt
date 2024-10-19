package com.example.cafeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cafeapp.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {
    private lateinit var binding: FragmentAboutUsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teamMembers = listOf(
            TeamMember("John Doe", "12345678", R.drawable.member1),
            TeamMember("Jane Smith", "23456789", R.drawable.member2),
            TeamMember("Bob Johnson", "34567890", R.drawable.member3),
            TeamMember("Alice Brown", "45678901", R.drawable.member4)
        )

        val adapter = MemberAdapter(teamMembers)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter
    }
}

data class TeamMember(val name: String, val nim: String, val photoResId: Int)