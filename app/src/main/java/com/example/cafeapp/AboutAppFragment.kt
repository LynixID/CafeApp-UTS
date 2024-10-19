package com.example.cafeapp

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import com.example.cafeapp.databinding.FragmentAboutAppBinding

    class AboutAppFragment : Fragment() {
        private var _binding: FragmentAboutAppBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            _binding = FragmentAboutAppBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.appDescriptionTextView.text = """
            GRID CAFE adalah aplikasi inovatif yang menghubungkan pecinta kopi dengan kafe-kafe terbaik di sekitar mereka. 
            Fitur utama kami meliputi:
            - Pencarian kafe terdekat
            - Menu digital dan pemesanan online
            - Program loyalitas pelanggan
            - Ulasan dan rating kafe
            
            Versi Aplikasi: 1.0.0
            Dikembangkan oleh: Tim GRID CAFE
        """.trimIndent()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }