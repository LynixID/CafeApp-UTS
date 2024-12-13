package com.example.cafeapp.Profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cafeapp.Login.Login_page
import com.example.cafeapp.databinding.FragmentProfileBinding // Adjust this import

// Fragment untuk menampilkan profil pengguna dan opsi terkait
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!! // Mengakses binding

    // Fungsi yang dipanggil saat fragment dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Fungsi untuk membuat tampilan fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Meng-inflate layout fragment menggunakan ViewBinding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Tombol "Tentang Kita"
        binding.aboutUsButton.setOnClickListener {
            val intent = Intent(requireContext(), TentangKitaActivity::class.java)
            startActivity(intent)
        }

        // Tombol "Tentang Aplikasi"
        binding.aboutAppButton.setOnClickListener {
            val intent = Intent(requireContext(), TentangAplikasiActivity::class.java)
            startActivity(intent)
        }

        // Tombol "Bantuan"
        binding.helpButton.setOnClickListener {
            val intent = Intent(requireContext(), BantuanActivity::class.java)
            startActivity(intent)
        }

        // Tombol "Logout"
        binding.logoutButton.setOnClickListener {
            navigateToLoginPage() // Fungsi untuk navigasi ke halaman login
        }

        return binding.root
    }

    // Fungsi untuk menavigasi ke halaman login
    private fun navigateToLoginPage() {
        val intent = Intent(requireActivity(), Login_page::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear previous activities
        startActivity(intent)
        requireActivity().finish() // Menutup aktivitas saat ini
    }

    // Fungsi yang dipanggil saat view fragment dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Membersihkan referensi binding untuk mencegah kebocoran memori
    }
}