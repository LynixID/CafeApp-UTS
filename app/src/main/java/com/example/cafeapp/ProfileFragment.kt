package com.example.cafeapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Tentang Kita
        val aboutUsButton: Button = view.findViewById(R.id.aboutUsButton)
        aboutUsButton.setOnClickListener {
            val intent = Intent(requireContext(), TentangKitaActivity::class.java)
            startActivity(intent)
        }

        // Tentang Aplikasi
        val aboutAppButton: Button = view.findViewById(R.id.aboutAppButton)
        aboutAppButton.setOnClickListener {
            val intent = Intent(requireContext(), TentangAplikasiActivity::class.java)
            startActivity(intent)
        }

        // Bantuan
        val helpButton: Button = view.findViewById(R.id.helpButton)
        helpButton.setOnClickListener {
            val intent = Intent(requireContext(), BantuanActivity::class.java)
            startActivity(intent)
        }

        // Mendapatkan referensi tombol logout
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)

        // Menetapkan listener untuk tombol logout
        logoutButton.setOnClickListener {
            // Panggil metode untuk mengarahkan pengguna ke LoginPageActivity
            navigateToLoginPage()
        }
        return view
    }

    private fun navigateToLoginPage() {
        // Arahkan pengguna ke LoginPageActivity
        val intent = Intent(requireActivity(), Login_page::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Menghapus activity sebelumnya
        startActivity(intent)
        requireActivity().finish() // Tutup activity saat ini
    }
}
