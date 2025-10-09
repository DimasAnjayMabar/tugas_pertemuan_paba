package com.example.pertemuan8_2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class fSatu : Fragment() {

    interface OnOperationSelectedListener {
        fun onOperationSelected(operation: String)
    }

    private var listener: OnOperationSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOperationSelectedListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_f_satu, container, false)

        view.findViewById<Button>(R.id.btn_plus).setOnClickListener { listener?.onOperationSelected("+") }
        view.findViewById<Button>(R.id.btn_minus).setOnClickListener { listener?.onOperationSelected("-") }
        view.findViewById<Button>(R.id.btn_times).setOnClickListener { listener?.onOperationSelected("x") }
        view.findViewById<Button>(R.id.btn_divide).setOnClickListener { listener?.onOperationSelected("/") }

        return view
    }
}
