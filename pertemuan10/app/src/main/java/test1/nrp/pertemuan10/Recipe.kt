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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    var data = mutableListOf<Triple<String, String, String>>()

    private lateinit var rvRecipe : RecyclerView

    private lateinit var rvAdapter: RecipeAdapter

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

        val etPicture = EditText(requireContext())
        etIngredient.hint = "Enter picture url"

        layout.addView(spinnerCategory)
        layout.addView(etIngredient)
        layout.addView(etPicture)

        builder.setView(layout)

        builder.setPositiveButton("Add") { dialog, _ ->
            val newIngredient = etIngredient.text.toString().trim()
            val selectedCategory = spinnerCategory.selectedItem as String
            val pictureUrl = etPicture.text.toString().trim()

            if (newIngredient.isNotEmpty()) {
                data.add(Triple(selectedCategory, newIngredient, pictureUrl))
                rvAdapter.notifyItemInserted(data.size - 1)
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
        selectedItem: Triple<String, String, String>,
        data: MutableList<Triple<String, String, String>>
    ){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Item $selectedItem")
        builder.setMessage("What do you want to do with this data?")

        builder.setPositiveButton("Update"){_, _ ->
            showUpdateDialog(position, selectedItem, data)
        }
        builder.setNegativeButton("Delete"){_, _ ->
            data.removeAt(position)
            Toast.makeText(
                requireContext(),
                "$selectedItem deleted",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNeutralButton("Add to Wishlist"){_, _ ->
            // to do later
        }
        builder.setNeutralButton("Cancel") {dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun showUpdateDialog(
        position: Int,
        oldValue: Triple<String, String, String>,
        data: MutableList<Triple<String, String, String>>,
    ) {
        val (oldCategory, oldIngredient, oldPicture) = oldValue

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

        val etPicture = EditText(requireContext())
        etPicture.hint = "Enter new url for picture"
        etPicture.setText(oldPicture)

        // Add views
        layout.addView(spinnerCategory)
        layout.addView(etIngredient)
        layout.addView(etPicture)

        builder.setView(layout)

        builder.setPositiveButton("Save") { dialog, _ ->
            val newCategory = spinnerCategory.selectedItem as String
            val newIngredient = etIngredient.text.toString().trim()
            val newPicture = etPicture.text.toString().trim()

            if (newIngredient.isNotEmpty()) {
                // Update the data pair
                data[position] = Triple(newCategory, newIngredient, newPicture)
                rvAdapter.notifyItemChanged(position)
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

        rvRecipe = view.findViewById(R.id.rv_recipe)
        rvRecipe.layoutManager = LinearLayoutManager(requireContext())

        rvAdapter = RecipeAdapter(data) {position, selectedItem, data ->
            showActionDialog(position, selectedItem, data)
        }
        rvRecipe.adapter = rvAdapter

        data.addAll(listOf(
            Triple("Utama", "Ayam", "https://example.com/images/ayam.jpg"),
            Triple("Utama", "Telur", "https://example.com/images/telur.jpg"),
            Triple("Bumbu", "Bawang Merah", "https://example.com/images/bawang_merah.jpg"),
            Triple("Bumbu", "Bawang Putih", "https://example.com/images/bawang_putih.jpg"),
            Triple("Bumbu", "Kecap Manis", "https://example.com/images/kecap_manis.jpg"),
            Triple("Bumbu", "Saus Tiram", "https://example.com/images/saus_tiram.jpg"),
            Triple("Tambahan", "Garam", "https://example.com/images/garam.jpg"),
            Triple("Tambahan", "Merica", "https://example.com/images/merica.jpg"),
            Triple("Tambahan", "Minyak Goreng", "https://example.com/images/minyak_goreng.jpg")
        ))

        val addBtn = view.findViewById<Button>(R.id.add_button)
        addBtn.setOnClickListener {
            showAddRecipeDialog()
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

class RecipeAdapter(
    private val data : List<Triple<String, String, String>>,
    private val onActionClick : (Int, Triple<String, String, String>, MutableList<Triple<String, String, String>>) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeAdapter.RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_recycler, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeAdapter.RecipeViewHolder, position: Int) {
        val (category, ingredient, picture) = data[position]
        holder.tvCategory.text = category
        holder.tvRecipe.text = ingredient
        holder.btnAction.setOnClickListener{
            onActionClick(position, data[position], data.toMutableList())
        }
    }

    override fun getItemCount(): Int = data.size

    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvCategory : TextView = view.findViewById(R.id.tv_kategori)
        val tvRecipe: TextView = view.findViewById(R.id.tv_recipe)
        val btnAction: Button = view.findViewById(R.id.btn_action)
    }
}