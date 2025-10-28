package test1.nrp.pertemuan10

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import test1.nrp.pertemuan10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.drawer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = v.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = systemBars.top
            v.layoutParams = params
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.navView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(v.paddingLeft, systemBars.top, v.paddingRight, v.paddingBottom)
            insets
        }

        setSupportActionBar(binding!!.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_layout) as NavHostFragment
        navController = navHostFragment.navController

        val topLevelDestination = setOf(
            R.id.homeFragment,
            R.id.profileFragment,
            R.id.settingsFragment,
            R.id.recipe
        )

        appBarConfiguration = AppBarConfiguration(
            topLevelDestination,
            binding!!.drawer
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding!!.navView.setupWithNavController(navController)

        /* Legacy Code */
//        ViewCompat.setOnApplyWindowInsetsListener(binding!!.toolbar) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            val params = v.layoutParams as ViewGroup.MarginLayoutParams
//            params.topMargin = systemBars.top
//            v.layoutParams = params
//            insets
//        }
//
//        ViewCompat.setOnApplyWindowInsetsListener(binding!!.navView) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(v.paddingLeft, systemBars.top, v.paddingRight, v.paddingBottom)
//            insets
//        }
//
//        binding?.apply {
//            setSupportActionBar(toolbar)
//            navView.bringToFront()
//        }
//
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_layout) as NavHostFragment
//        navController = navHostFragment.navController
//        binding!!.navView.setupWithNavController(navController)
//
//
//
//        appBarConfiguration = AppBarConfiguration(
//            navController.graph,
//            binding!!.drawer
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}