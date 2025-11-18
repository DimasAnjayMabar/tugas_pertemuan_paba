package test1.nrp.pertemuan10

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var wishlistPreferences: SharedPreferences
    private lateinit var boughtPreferences: SharedPreferences

    private val WISHLIST_KEY = "dt_cart"
    private val BOUGHT_KEY = "dt_bought"

    private lateinit var rvCart: RecyclerView
    private lateinit var rvBought: RecyclerView

    private var wishlistData: MutableList<Triple<String, String, String>> = mutableListOf()
    private var boughtData: MutableList<Triple<String, String, String>> = mutableListOf()

    private lateinit var cartAdapter: CartAdapter
    private lateinit var boughtAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        // Initialize SharedPreferences
        wishlistPreferences = requireContext().getSharedPreferences("wishlist_prefs", Context.MODE_PRIVATE)
        boughtPreferences = requireContext().getSharedPreferences("bought_prefs", Context.MODE_PRIVATE)

        // Initialize RecyclerViews
        rvCart = view.findViewById(R.id.rv_cart)
        rvBought = view.findViewById(R.id.rv_bought)

        rvCart.layoutManager = LinearLayoutManager(requireContext())
        rvBought.layoutManager = LinearLayoutManager(requireContext())

        // Load data
        wishlistData = loadWishlist()
        boughtData = loadBought()

        // Setup adapters
        cartAdapter = CartAdapter(wishlistData) { position, item ->
            moveToBought(position, item)
        }

        boughtAdapter = CartAdapter(boughtData) { position, item ->
            // No action for bought items, or you can implement remove functionality
        }

        rvCart.adapter = cartAdapter
        rvBought.adapter = boughtAdapter

        updateUI()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mark_as_bought -> {
                moveAllToBought()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadWishlist(): MutableList<Triple<String, String, String>> {
        val gson = Gson()
        val json = wishlistPreferences.getString(WISHLIST_KEY, null)
        val type = object : TypeToken<MutableList<Triple<String, String, String>>>() {}.type

        return if (json != null) {
            gson.fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }

    private fun loadBought(): MutableList<Triple<String, String, String>> {
        val gson = Gson()
        val json = boughtPreferences.getString(BOUGHT_KEY, null)
        val type = object : TypeToken<MutableList<Triple<String, String, String>>>() {}.type

        return if (json != null) {
            gson.fromJson(json, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }
    }

    private fun saveWishlist() {
        val gson = Gson()
        val json = gson.toJson(wishlistData)
        wishlistPreferences.edit().putString(WISHLIST_KEY, json).apply()
    }

    private fun saveBought() {
        val gson = Gson()
        val json = gson.toJson(boughtData)
        boughtPreferences.edit().putString(BOUGHT_KEY, json).apply()
    }

    private fun moveToBought(position: Int, item: Triple<String, String, String>) {
        // Remove from wishlist
        wishlistData.removeAt(position)
        saveWishlist()

        // Add to bought
        boughtData.add(item)
        saveBought()

        // Update UI
        cartAdapter.notifyItemRemoved(position)
        boughtAdapter.notifyItemInserted(boughtData.size - 1)

        updateUI()
    }

    private fun moveAllToBought() {
        if (wishlistData.isNotEmpty()) {
            // Add all to bought
            boughtData.addAll(wishlistData)
            saveBought()

            // Clear wishlist
            val movedCount = wishlistData.size
            wishlistData.clear()
            saveWishlist()

            // Update UI
            cartAdapter.notifyDataSetChanged()
            boughtAdapter.notifyDataSetChanged()

            updateUI()
        }
    }

    private fun updateUI() {
        // Show/hide RecyclerViews based on data availability
        if (wishlistData.isEmpty()) {
            rvCart.visibility = View.GONE
        } else {
            rvCart.visibility = View.VISIBLE
        }

        if (boughtData.isEmpty()) {
            rvBought.visibility = View.GONE
        } else {
            rvBought.visibility = View.VISIBLE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class CartAdapter(
    private val data: MutableList<Triple<String, String, String>>,
    private val onActionClick: (Int, Triple<String, String, String>) -> Unit,
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_recycler, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val (category, ingredient, picture) = data[position]
        holder.tvCategory.text = category
        holder.tvRecipe.text = ingredient
        if (picture.isNotEmpty()) {
            Picasso.get()
                .load(picture)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivPicture)
        } else {
            holder.ivPicture.setImageResource(R.drawable.ic_launcher_foreground)
        }
        holder.btnMark.setOnClickListener {
            onActionClick(position, data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.cart_tv_kategori)
        val tvRecipe: TextView = view.findViewById(R.id.cart_tv_recipe)
        val btnMark: Button = view.findViewById(R.id.btn_delete_wishlist)
        val ivPicture: ImageView = view.findViewById(R.id.iv_recipe)
    }
}