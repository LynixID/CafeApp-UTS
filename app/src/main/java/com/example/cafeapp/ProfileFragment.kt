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
        val intent = Intent(requireActivity(), LoginPage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Menghapus activity sebelumnya
        startActivity(intent)
        requireActivity().finish() // Tutup activity saat ini
    }
}
