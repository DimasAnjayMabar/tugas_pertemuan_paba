package com.example.test.ui.dua

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.FragmentNomor2Binding
import com.example.test.databinding.ItemCharacterBinding

class Nomor2Fragment : Fragment() {

    private var _binding: FragmentNomor2Binding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: Nomor2ViewModel
    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(Nomor2ViewModel::class.java)
        _binding = FragmentNomor2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupButtonClickListener()
        observeViewModel()

        // Observe title text
        viewModel.text.observe(viewLifecycleOwner) {
            binding.fragmentNomor2.text = it
        }
    }

    private fun setupRecyclerView() {
        characterAdapter = CharacterAdapter()
        binding.rvCharacters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = characterAdapter
        }
    }

    private fun setupButtonClickListener() {
        binding.btnCalculate.setOnClickListener {
            val input = binding.etInput.text.toString()
            viewModel.processInput(input)
        }
    }

    private fun observeViewModel() {
        viewModel.resultText.observe(viewLifecycleOwner) { result ->
            binding.tvResult.text = result
        }

        viewModel.characterCount.observe(viewLifecycleOwner) { characterMap ->
            val characterList = characterMap.map { (char, count) ->
                CharacterItem(char, count)
            }.sortedBy { it.char }

            characterAdapter.submitList(characterList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Data class untuk item karakter
    data class CharacterItem(val char: Char, val count: Int)

    // Adapter untuk RecyclerView - VERSI YANG DIPERBAIKI
    class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

        private var characterList: List<CharacterItem> = emptyList()

        fun submitList(newList: List<CharacterItem>) {
            characterList = newList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
            val binding = ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CharacterViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
            holder.bind(characterList[position])
        }

        override fun getItemCount(): Int = characterList.size

        class CharacterViewHolder(
            private val binding: ItemCharacterBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: CharacterItem) {
                binding.tvCharacter.text = when (item.char) {
                    ' ' -> "Spasi"
                    '\t' -> "Tab"
                    '\n' -> "New Line"
                    else -> item.char.toString()
                }
                binding.tvCount.text = item.count.toString()
            }
        }
    }
}