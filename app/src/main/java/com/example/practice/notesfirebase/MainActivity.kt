package com.example.practice.notesfirebase

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.practice.notesfirebase.databinding.ActivityMainBinding
import com.example.practice.notesfirebase.util.EncryptedSharedPreferencesManager.getLoadTheData
import com.example.practice.notesfirebase.util.splitGetFirstString
import com.example.practice.notesfirebase.util.toastMessage
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.Firebase
import com.google.firebase.appdistribution.InterruptionLevel
import com.google.firebase.appdistribution.appDistribution


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        // throw RuntimeException("Test Crash")
        // Set up the Toolbar as ActionBar
        setSupportActionBar(mainBinding.toolbar)

        val result = getLoadTheData(this)
        Log.d("TAG", "Result from getLoadTheData: $result")
        val (userEmail, userPassword) = result ?: let {
            Log.d("TAG", "onViewCreated: Data is null, using default values.")
            return@let Pair("defaultEmail@example.com", "defaultPassword")
        }
        // Initialize NavHostFragment and NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup Bottom Navigation
        bottomNavView = findViewById(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavView, navController)

        // Use OnBackPressedDispatcher to handle back press
        onBackPressedDispatcher.addCallback(this@MainActivity) {
            // Custom back press handling
            if (navController.currentDestination?.id == R.id.homeFragment) {
                // If already on the HomeFragment, exit the activity or handle it accordingly
                Log.d("MainActivity", "Back pressed on HomeFragment")
                finish() // Exit the activity or do something else (e.g. show confirmation)
            } else {
                // Navigate to HomeFragment when back button is pressed
                bottomNavView.selectedItemId = R.id.nav_home // Ensure Home Fragment is selected in Bottom Navigation
                navController.popBackStack(R.id.homeFragment, false) // Navigate to home fragment
            }
        }

        // Setup Navigation Drawer
        drawerLayout = findViewById(R.id.drawerLayout)
        val navigationView: NavigationView = findViewById(R.id.navigation_drawer)
        NavigationUI.setupWithNavController(navigationView, navController)
        val view = navigationView.getHeaderView(0)
        val userNameNew = view.findViewById<MaterialTextView>(R.id.user_name)
        val userEmailNew = view.findViewById<MaterialTextView>(R.id.user_email)
        val appVersion = view.findViewById<MaterialTextView>(R.id.nav_version)
        if (userEmail!=null){
            userEmailNew.text = userEmail
            userNameNew.text = "Hello ${splitGetFirstString(userEmail ,"@")}"
            val versionName = "1.0" //BuildConfig.VERSION_NAME  // Get the version name from BuildConfig
            appVersion.text = "App Version : $versionName"
        }

        val mDrawerToggle = ActionBarDrawerToggle(this, drawerLayout,
                mainBinding.toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawerLayout.setDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
        drawerLayout.closeDrawers()

        // Set up ActionBar with NavController
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        // Handle navigation item selections in the NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_feedback -> {
                    navController.navigate(R.id.feedBackFragment)
                    drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_logout -> {
                    // Handle logout
                    openLogoutDialog() //need to clear the SharedPreference Data.....
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
        bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    //replaceFragment(HomeFragment())
                    navController.navigate(R.id.homeFragment)
                }
                R.id.nav_saved -> {
                   // replaceFragment(SavedFragment())
                    navController.navigate(R.id.savedFragment)
                }
                R.id.nav_deleted -> {
                    //replaceFragment(DeletedFragment())
                    navController.navigate(R.id.deletedFragment)
                }
            }
            true
        }
    }

    private fun openLogoutDialog() {
        toastMessage(this,"Logout")
    }

    override fun onSupportNavigateUp(): Boolean {
        // Handle navigation when the up button is pressed
        return NavigationUI.navigateUp(navController, drawerLayout) || super.onSupportNavigateUp()
    }
    fun hideTheToolBar(visibility : Int){
        if (visibility == View.VISIBLE){
           mainBinding.toolbar.visibility = View.VISIBLE
        }else{
            mainBinding.toolbar.visibility = View.GONE
        }
    }
    fun hideBottomNavigationView(visibility : Int){
        if (visibility == View.VISIBLE){
            mainBinding.bottomNavigation.visibility = View.VISIBLE
        }else{
            mainBinding.bottomNavigation.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle your menu items here first
        when (item.itemId) {
            R.id.ivSearch -> {
                navController.navigate(R.id.searchFragment) // Navigate to searchFragment
                return true // Indicate the action was handled
            }
            else -> {
                return super.onOptionsItemSelected(item) // Delegate other cases to the super class
            }
        }
    }
     private fun replaceFragment(someFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment , someFragment)
        transaction.addToBackStack(null);
        transaction.commit()
    }


}
