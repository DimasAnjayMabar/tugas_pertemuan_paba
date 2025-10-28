package test1.nrp.pertemuan10

import android.app.AlertDialog
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Recipe.newInstance] factory method to
 * create an instance of this fragment.
 */
class Recipe : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var data = mutableListOf<Pair<String, String>>()

    private lateinit var lvAdapter: ArrayAdapter<String>

    private lateinit var lv1: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false)
    }

    private fun showAddRecipeDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Ingredients")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val spinnerCategory = Spinner(requireContext())
        val categories = arrayOf("Utama", "Bumbu", "Tambahan")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter

        val etIngredient = EditText(requireContext())
        etIngredient.hint = "Enter ingredient name"

        layout.addView(spinnerCategory)
        layout.addView(etIngredient)

        builder.setView(layout)

        builder.setPositiveButton("Add") { dialog, _ ->
            val newIngredient = etIngredient.text.toString().trim()
            val selectedCategory = spinnerCategory.selectedItem as String

            if (newIngredient.isNotEmpty()) {
                data.add(selectedCategory to newIngredient)

                val displayData = data.map { (kategori, bahan) -> "$kategori: $bahan" }
                lvAdapter.clear()
                lvAdapter.addAll(displayData)
                lvAdapter.notifyDataSetChanged()

                Toast.makeText(
                    requireContext(),
                    "Added $newIngredient to $selectedCategory",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ingredient name cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun showActionDialog(
        position: Int,
        selectedItem: Pair<String, String>,
        data: MutableList<Pair<String, String>>,
        adapter: ArrayAdapter<String>
    ){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Item $selectedItem")
        builder.setMessage("What do you want to do with this data?")

        builder.setPositiveButton("Update"){_, _ ->
            showUpdateDialog(position, selectedItem, data, adapter)
        }
        builder.setNegativeButton("Delete"){_, _ ->
            data.removeAt(position)

            val updatedDisplay = data.map { (kategori, bahan) -> "$kategori: $bahan" }

            adapter.clear()
            adapter.addAll(updatedDisplay)
            adapter.notifyDataSetChanged()
            Toast.makeText(
                requireContext(),
                "$selectedItem deleted",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNeutralButton("Cancel") {dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun showUpdateDialog(
        position: Int,
        oldValue: Pair<String, String>,
        data: MutableList<Pair<String, String>>,
        adapter: ArrayAdapter<String>
    ) {
        val (oldCategory, oldIngredient) = oldValue

        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Update Ingredient")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        // Category Spinner
        val spinnerCategory = Spinner(requireContext())
        val categories = arrayOf("Utama", "Bumbu", "Tambahan")
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter

        // Set the spinner to old category
        val selectedIndex = categories.indexOf(oldCategory)
        if (selectedIndex >= 0) spinnerCategory.setSelection(selectedIndex)

        // Ingredient input
        val etIngredient = EditText(requireContext())
        etIngredient.hint = "Enter new ingredient"
        etIngredient.setText(oldIngredient)

        // Add views
        layout.addView(spinnerCategory)
        layout.addView(etIngredient)

        builder.setView(layout)

        builder.setPositiveButton("Save") { dialog, _ ->
            val newCategory = spinnerCategory.selectedItem as String
            val newIngredient = etIngredient.text.toString().trim()

            if (newIngredient.isNotEmpty()) {
                // Update the data pair
                data[position] = newCategory to newIngredient

                // Refresh displayed list
                val updatedDisplay = data.map { (cat, ing) -> "$cat: $ing" }
                adapter.clear()
                adapter.addAll(updatedDisplay)
                adapter.notifyDataSetChanged()

                Toast.makeText(
                    requireContext(),
                    "Updated to: $newCategory - $newIngredient",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Ingredient name cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lv1 = view.findViewById<ListView>(R.id.list_view_1)

        data.addAll(listOf(
            "Utama" to "Ayam",
            "Utama" to "Telur",
            "Bumbu" to "Bawang Merah",
            "Bumbu" to "Bawang Putih",
            "Bumbu" to "Kecap Manis",
            "Bumbu" to "Saus Tiram",
            "Tambahan" to "Garam",
            "Tambahan" to "Merica",
            "Tambahan" to "Minyak Goreng"
        ))

        val displayData = data.map { (kategori, bahan) ->
            "$kategori: $bahan"
        }

        lvAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            displayData
        )

        lv1.adapter = lvAdapter

        val addBtn = view.findViewById<Button>(R.id.add_button)
        addBtn.setOnClickListener {
            showAddRecipeDialog()
        }

        val gestureDetector = GestureDetector(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener(){
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val position = lv1.pointToPosition(e.x.toInt(), e.y.toInt())
                    if(position != ListView.INVALID_POSITION){
                        val selectedItem = data[position]
                        showActionDialog(position, selectedItem, data, lvAdapter)
                    }
                    return true
                }
            }
        )

        lv1.setOnTouchListener {_, event ->
            gestureDetector.onTouchEvent(event)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Recipe.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Recipe().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}