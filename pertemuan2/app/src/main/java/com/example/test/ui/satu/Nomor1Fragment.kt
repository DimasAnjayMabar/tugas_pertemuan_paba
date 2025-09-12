package com.example.test.ui.satu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.test.databinding.FragmentNomor1Binding

class Nomor1Fragment : Fragment() {

    private var _binding: FragmentNomor1Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: Nomor1ViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(Nomor1ViewModel::class.java)
        _binding = FragmentNomor1Binding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observasi hasil LiveData
        viewModel.text.observe(viewLifecycleOwner) {
            binding.fragmentNomor1.text = it
        }

        // Aksi ketika tombol ditekan
        binding.btnCheck.setOnClickListener {
            val input = binding.inputText.text.toString()
            viewModel.setHasil(input)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
