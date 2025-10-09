package com.example.pertemuan8_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager

class fDua : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_f_dua, container, false)

        val num1 = arguments?.getString("num1") ?: ""
        val num2 = arguments?.getString("num2") ?: ""
        val result = arguments?.getString("result") ?: ""
        val operation = arguments?.getString("operation") ?: ""

        val tvHasil = view.findViewById<TextView>(R.id.tv_hasil)
        tvHasil.text = "Math Operation:\n$num1 $operation $num2 = $result"

        val btnBack = view.findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener{
            (activity as? MainActivity)?.resetInputs()
            requireActivity().onBackPressed()
        }
        return view
    }
}
