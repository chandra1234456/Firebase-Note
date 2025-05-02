package com.example.practice.notesfirebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.practice.notesfirebase.databinding.ActivityMainBinding
import com.example.practice.notesfirebase.home.HomeFragmentListener
import com.example.practice.notesfirebase.util.EncryptedSharedPreferencesManager.getLoadTheData
import com.example.practice.notesfirebase.util.splitGetFirstString
import com.example.practice.notesfirebase.util.toastMessage
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() , HomeFragmentListener {
    private lateinit var mainBinding : ActivityMainBinding
    private lateinit var navController : NavController
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var logoutReceiver : BroadcastReceiver
      private lateinit var analytics: FirebaseAnalytics

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        // Obtain the FirebaseAnalytics instance.
         analytics = com.google.firebase.Firebase.analytics
         val bundle = Bundle().apply {
             putString(FirebaseAnalytics.Param.ITEM_ID, "id123")
             putString(FirebaseAnalytics.Param.ITEM_NAME, "My Item")
             putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
         }

          analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        // Obtain the FirebaseAnalytics instance.
        // throw RuntimeException("Test Crash")
        // Set up the Toolbar as ActionBar
        setSupportActionBar(mainBinding.toolbar)


        // Initialize the receiver for listening to logout notifications
        logoutReceiver = object : BroadcastReceiver() {
            override fun onReceive(context : Context , intent : Intent) {
                // Navigate to the LoginFragment
                navigateToLoginFragment()
            }
        }

        // Register the receiver to listen for the broadcast from FirebaseMessagingService
        val filter = IntentFilter("com.example.app.ACTION_LOGOUT")
        registerReceiver(logoutReceiver , filter , RECEIVER_NOT_EXPORTED)

        val result = getLoadTheData(this)
        Log.d("TAG" , "Result from getLoadTheData: $result")
        val (userEmail , userPassword) = result ?: let {
            Log.d("TAG" , "onViewCreated: Data is null, using default values.")
            return@let Pair("defaultEmail@example.com" , "defaultPassword")
        }
        // Initialize NavHostFragment and NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup Bottom Navigation
        bottomNavView = findViewById(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavView , navController)

        // Use OnBackPressedDispatcher to handle back press
        onBackPressedDispatcher.addCallback(this@MainActivity) {
            // Custom back press handling
            if (navController.currentDestination?.id == R.id.homeFragment) {
                // If already on the HomeFragment, exit the activity or handle it accordingly
                Log.d("MainActivity" , "Back pressed on HomeFragment")
                finish() // Exit the activity or do something else (e.g. show confirmation)
            } else {
                // Navigate to HomeFragment when back button is pressed
                bottomNavView.selectedItemId =
                    R.id.nav_home // Ensure Home Fragment is selected in Bottom Navigation
                navController.popBackStack(R.id.homeFragment , false) // Navigate to home fragment
            }
        }

        // Setup Navigation Drawer
        drawerLayout = findViewById(R.id.drawerLayout)
        val navigationView : NavigationView = findViewById(R.id.navigation_drawer)
        NavigationUI.setupWithNavController(navigationView , navController)
        val view = navigationView.getHeaderView(0)
        val userNameNew = view.findViewById<MaterialTextView>(R.id.user_name)
        val userEmailNew = view.findViewById<MaterialTextView>(R.id.user_email)
        val appVersion = view.findViewById<MaterialTextView>(R.id.nav_version)
        if (userEmail != null) {
            userEmailNew.text = userEmail
            userNameNew.text = "Hello ${splitGetFirstString(userEmail , "@")}"
            //    val versionName = BuildConfig.VERSION_NAME  // Get the version name from BuildConfig
            val versionName = try {
                val packageInfo = applicationContext.packageManager.getPackageInfo(
                        applicationContext.packageName ,
                        0
                                                                                  )
                packageInfo.versionName
            } catch (e : PackageManager.NameNotFoundException) {
                e.printStackTrace()
                "Unknown" // Default value in case of error
            }
            Log.d("TAG" , "versionName: $versionName")
            appVersion.text = "App Version : $versionName"
        }

        val mDrawerToggle = ActionBarDrawerToggle(
                this , drawerLayout ,
                mainBinding.toolbar , R.string.navigation_drawer_open ,
                R.string.navigation_drawer_close
                                                 )
        drawerLayout.setDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
        drawerLayout.closeDrawers()

        // Set up ActionBar with NavController
        NavigationUI.setupActionBarWithNavController(this , navController , drawerLayout)

        // Handle navigation item selections in the NavigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_feedback -> {
                    navController.navigate(R.id.feedBackFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.nav_logout -> {
                    // navController.navigate(R.id.feedBackFragment)
                    toastMessage(this@MainActivity , "Logout")
                    // Get FirebaseAuth instance
                    val auth = FirebaseAuth.getInstance()
                    // Sign out the user
                    auth.signOut()
                    navController.navigate(R.id.welcomeFragment)
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
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                // Send this token to your server or save in Firestore under the user's ID
                Log.d("FCM" , "Token: $token")
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver when the activity is destroyed
        unregisterReceiver(logoutReceiver)
    }

    private fun navigateToLoginFragment() {
        navController.navigate(R.id.welcomeFragment)
    }

    private fun openLogoutDialog() {
        toastMessage(this , "Logout")
    }

    override fun onSupportNavigateUp() : Boolean {
        // Handle navigation when the up button is pressed
        return NavigationUI.navigateUp(navController , drawerLayout) || super.onSupportNavigateUp()
    }

    fun hideTheToolBar(visibility : Int) {
        if (visibility == View.VISIBLE) {
            mainBinding.toolbar.visibility = View.VISIBLE
        } else {
            mainBinding.toolbar.visibility = View.GONE
        }
    }

    fun hideBottomNavigationView(visibility : Int) {
        if (visibility == View.VISIBLE) {
            mainBinding.bottomNavigation.visibility = View.VISIBLE
        } else {
            mainBinding.bottomNavigation.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
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

    private fun replaceFragment(someFragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment , someFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun updateDrawerData(email : String , versionName : String) {
        val navHeader = mainBinding.navigationDrawer.getHeaderView(0)
        navHeader.findViewById<MaterialTextView>(R.id.user_email).text = email
        navHeader.findViewById<MaterialTextView>(R.id.user_name).text =
            "Hello ${splitGetFirstString(email , "@")}"
        navHeader.findViewById<MaterialTextView>(R.id.nav_version).text =
            "App Version : $versionName"
    }


}
