package com.example.cafeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafeapp.databinding.ItemTeamMemberBinding

class MemberAdapter(private val teamMembers: List<TeamMember>) :
        RecyclerView.Adapter<MemberAdapter.TeamMemberViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamMemberViewHolder {
            val binding = ItemTeamMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TeamMemberViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TeamMemberViewHolder, position: Int) {
            holder.bind(teamMembers[position])
        }

        override fun getItemCount() = teamMembers.size

        class TeamMemberViewHolder(private val binding: ItemTeamMemberBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(teamMember: TeamMember) {
                binding.nameTextView.text = teamMember.name
                binding.nimTextView.text = teamMember.nim
                binding.photoImageView.setImageResource(teamMember.photoResId)
            }
        }
    }