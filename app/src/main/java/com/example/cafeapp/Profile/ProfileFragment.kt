package com.example.cafeapp.Profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cafeapp.Login.Login_page
import com.example.cafeapp.databinding.FragmentProfileBinding // Adjust this import

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using View Binding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Tentang Kita
        binding.aboutUsButton.setOnClickListener {
            val intent = Intent(requireContext(), TentangKitaActivity::class.java)
            startActivity(intent)
        }

        // Tentang Aplikasi
        binding.aboutAppButton.setOnClickListener {
            val intent = Intent(requireContext(), TentangAplikasiActivity::class.java)
            startActivity(intent)
        }

        // Bantuan
        binding.helpButton.setOnClickListener {
            val intent = Intent(requireContext(), BantuanActivity::class.java)
            startActivity(intent)
        }

        // Logout
        binding.logoutButton.setOnClickListener {
            navigateToLoginPage()
        }

        return binding.root
    }

    private fun navigateToLoginPage() {
        val intent = Intent(requireActivity(), Login_page::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear previous activities
        startActivity(intent)
        requireActivity().finish() // Close current activity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference to prevent memory leaks
    }
}