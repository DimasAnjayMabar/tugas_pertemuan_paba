package com.example.test.ui.tiga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.test.databinding.FragmentNomor3Binding

class Nomor3Fragment : Fragment() {

    private var _binding: FragmentNomor3Binding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: Nomor3ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(Nomor3ViewModel::class.java)

        _binding = FragmentNomor3Binding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.fragmentNomor3
        val inputNumber = binding.inputNumber
        val btnGenerate = binding.btnGenerate

        // Set properties untuk text view
        textView.textSize = 16f
        textView.typeface = android.graphics.Typeface.MONOSPACE

        // Handle tombol generate
        btnGenerate.setOnClickListener {
            val inputText = inputNumber.text.toString()
            if (inputText.isNotEmpty()) {
                val n = inputText.toIntOrNull()
                if (n != null && n > 0) {
                    viewModel.updatePyramid(n)
                } else {
                    Toast.makeText(requireContext(), "Masukkan angka positif", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Masukkan angka terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe perubahan pada text
        viewModel.text.observe(viewLifecycleOwner) { pyramidText ->
            textView.text = pyramidText
        }

        // Generate piramida awal
        viewModel.updatePyramid(3)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}