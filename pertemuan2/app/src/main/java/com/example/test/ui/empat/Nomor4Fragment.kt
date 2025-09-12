package com.example.test.ui.empat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.test.databinding.FragmentNomor4Binding
import com.example.test.ui.satu.Nomor4ViewModel

class Nomor4Fragment : Fragment() {

    private var _binding: FragmentNomor4Binding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: Nomor4ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(Nomor4ViewModel::class.java)

        _binding = FragmentNomor4Binding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setup observer untuk title
        viewModel.text.observe(viewLifecycleOwner) {
            binding.titleText.text = it
        }

        // Setup click listener untuk tombol
        binding.generateButton.setOnClickListener {
            findMissingNumber()
        }

        // Contoh input default
        binding.inputEdittext.setText("1,2,4,5")

        return root
    }

    private fun findMissingNumber() {
        val input = binding.inputEdittext.text.toString().trim()

        if (input.isEmpty()) {
            showError("Silakan masukkan angka")
            return
        }

        val result = viewModel.processInput(input)

        if (result.isSuccess) {
            val missingNumber = result.getOrNull() ?: 0
            binding.resultText.text = "Angka yang hilang: $missingNumber"
            binding.errorText.visibility = View.GONE
        } else {
            val errorMessage = result.exceptionOrNull()?.message ?: "Terjadi kesalahan"
            showError(errorMessage)
        }
    }

    private fun showError(message: String) {
        binding.errorText.text = message
        binding.errorText.visibility = View.VISIBLE
        binding.resultText.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}