package com.example.practice.notesfirebase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.practice.notesfirebase.databinding.FragmentSplashBinding
import com.example.practice.notesfirebase.util.EncryptedSharedPreferencesManager.getLoadTheData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class SplashFragment : Fragment() {
    private lateinit var splashBinding : FragmentSplashBinding
    private val fragmentScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        splashBinding = FragmentSplashBinding.inflate(layoutInflater)
        checkAlreadyLogInOrNot()
        return splashBinding.root
    }

    private fun checkAlreadyLogInOrNot() {
        val email = "defaultEmail".trim().lowercase(Locale.ROOT) + "@gmail.com"
        Log.d("TAG" , "checkAlreadyLogInOrNot: $email")

        val (userEmail , userPassword) = getLoadTheData(requireContext())
            ?: Pair("defaultEmail@example.com" , "defaultPassword")
        Log.d("TAG" , "splash: $userEmail")
        fragmentScope.launch {
            delay(3000)
            // Inside your Fragment or Activity
            val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true) // Adjust the startFragment ID to where you want to pop up to.
                    .build()
            if (userEmail == "defaultEmail@example.com" || userEmail == null) {
                findNavController().navigate(R.id.welcomeFragment,null,navOptions)
            } else {
                findNavController().navigate(R.id.homeFragment,null,navOptions)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).hideTheToolBar(View.GONE)
            (requireActivity() as MainActivity).hideBottomNavigationView(View.GONE)
        }
    }

    override fun onPause() {
        fragmentScope.cancel()  // Cancel any ongoing coroutines when fragment is paused
        super.onPause()
    }
}