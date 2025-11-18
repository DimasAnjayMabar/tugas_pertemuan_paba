package test1.nrp.pertemuan10

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import test1.nrp.pertemuan10.databinding.FragmentRecipeBinding

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

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var wishlistPreferences: SharedPreferences

    private val WISHLIST_KEY = "dt_cart"

    private val DATA_KEY = "dt_recipe"

    private var binding: FragmentRecipeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)

        sharedPreferences = requireContext().getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
        wishlistPreferences = requireContext().getSharedPreferences("wishlist_prefs", Context.MODE_PRIVATE)

        loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_cart -> {
                findNavController().navigate(
                    R.id.action_recipe_to_cartFragment
                )
                true
            }
            R.id.action_add_recipe -> {
                showAddRecipeDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addToWishlist(item: Triple<String, String, String>){
        val gson = Gson()

        val existingWishlist = loadWishlist()

        val isAlreadyInWishlist = existingWishlist.any { it.second == item.second}

        if(isAlreadyInWishlist){
            Toast.makeText(
                requireContext(),
                "${item.second} already in wishlist",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            existingWishlist.add(item)

            val editor = wishlistPreferences.edit()
            val json = gson.toJson(existingWishlist)
            editor.putString(WISHLIST_KEY, json)
            editor.apply()

            Toast.makeText(
                requireContext(),
                "${item.second} added to wishlist",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadWishlist(): MutableList<Triple<String, String, String>>{
        val gson = Gson()
        val json = wishlistPreferences.getString(WISHLIST_KEY, null)
        val type = object : TypeToken<MutableList<Triple<String, String, String>>>() {}.type

        return if (json != null){
            gson.fromJson(json, type)
        }else{
            mutableListOf()
        }
    }

    private fun saveData(){
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(data)
        editor.putString(DATA_KEY, json)
        editor.apply()
    }

    private fun loadData(){
        val gson = Gson()
        val json = sharedPreferences.getString(DATA_KEY, null)
        val type = object : TypeToken<MutableList<Triple<String, String, String>>>() {}.type

        if(json != null){
            data = gson.fromJson(json, type)
        }else{
            data.addAll(listOf(
                Triple("Utama", "Ayam", ""),
                Triple("Utama", "Telur", ""),
                Triple("Bumbu", "Bawang Merah", "https://example.com/images/bawang_merah.jpg"),
                Triple("Bumbu", "Bawang Putih", "https://example.com/images/bawang_putih.jpg"),
                Triple("Bumbu", "Kecap Manis", "https://example.com/images/kecap_manis.jpg"),
                Triple("Bumbu", "Saus Tiram", "https://example.com/images/saus_tiram.jpg"),
                Triple("Tambahan", "Garam", "https://example.com/images/garam.jpg"),
                Triple("Tambahan", "Merica", "https://example.com/images/merica.jpg"),
                Triple("Tambahan", "Minyak Goreng", "https://example.com/images/minyak_goreng.jpg"
            )))
            saveData()
        }
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
                saveData()
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
    ){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Item $selectedItem")
        builder.setMessage("What do you want to do with this data?")

        builder.setPositiveButton("Update"){_, _ ->
            showUpdateDialog(position, selectedItem)
        }
        builder.setNegativeButton("Delete"){_, _ ->
            data.removeAt(position)
            rvAdapter.notifyItemRemoved(position)
            saveData()
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
        oldValue: Triple<String, String, String>,
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
                saveData()
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

        rvAdapter = RecipeAdapter(
            data,
            onActionClick = { position, selectedItem ->
                showActionDialog(position, selectedItem)
            },
            onWishlistClick = { item ->
                addToWishlist(item)
            }
        )

        rvRecipe.adapter = rvAdapter
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
    private val data : MutableList<Triple<String, String, String>>,
    private val onActionClick : (Int, Triple<String, String, String>) -> Unit,
    private val onWishlistClick: (Triple<String, String, String>) -> Unit
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
        if (picture.isNotEmpty()){
            Picasso.get()
                .load(picture)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivPicture)
        }else{
            holder.ivPicture.setImageResource(R.drawable.ic_launcher_foreground)
        }
        holder.btnAction.setOnClickListener{
            onActionClick(position, data[position])
        }
        holder.btnCart.setOnClickListener {
            onWishlistClick(data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvCategory : TextView = view.findViewById(R.id.tv_kategori)
        val tvRecipe: TextView = view.findViewById(R.id.tv_recipe)
        val btnAction: Button = view.findViewById(R.id.btn_action)
        val btnCart: Button = view.findViewById(R.id.btn_add_to_wishlist)
        val ivPicture: ImageView = view.findViewById(R.id.iv_recipe)
    }
}