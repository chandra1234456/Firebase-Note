package com.example.practice.notesfirebase

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practice.notesfirebase.databinding.FragmentLoginSignUpBinding
import com.example.practice.notesfirebase.util.EncryptedSharedPreferencesManager.insertAndSaveData
import com.example.practice.notesfirebase.util.splitGetFirstString
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginSignUpFragment : Fragment() {
    private lateinit var loginSignUpBinding : FragmentLoginSignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth
    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        loginSignUpBinding = FragmentLoginSignUpBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
       /* loginSignUpBinding.btnLogin.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }*/
       loginSignUpBinding.btnLogin.setOnClickListener {
            val email = loginSignUpBinding.etEmailAddress.text.toString()
            val password = splitGetFirstString(email,"@")+1234
            if (validateInputs(email , password)) {
                //for Login
                loginUser(email , password)
                //for SigUp
                //registerUser(email, password)

            }
        }
        return loginSignUpBinding.root
    }

    private fun loginUser(email : String , password : String) {
        firebaseAuth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user : FirebaseUser? = firebaseAuth.currentUser
                        Log.d("Tag" , "loginUser: $user")
                        Toast.makeText(requireContext() , "Login Successful" , Toast.LENGTH_SHORT)
                                .show()
                        // Navigate to the main activity
                        insertAndSaveData(requireContext() , email , password)
                        Log.d("TAG" , "loginUser: ${task.result}")
                        Log.d("TAG" , "task: $task")
                        findNavController().navigate(R.id.homeFragment)
                    } else {
                        Log.d("TAg" , "loginUser: ${task.exception}")
                        Log.d("TAg" , "loginUser: ${task.exception?.message}")
                        Toast.makeText(
                                requireContext() ,
                                "Login Failed: ${task.exception?.message}" ,
                                Toast.LENGTH_SHORT
                                      ).show()
                    }
                }
    }

    private fun registerUser(email : String , password : String) {
        firebaseAuth.createUserWithEmailAndPassword(email , password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                                requireContext() ,
                                "Registration Successful" ,
                                Toast.LENGTH_SHORT
                                      ).show()
                        // Navigate to the login activity or main activity
                    } else {
                        Toast.makeText(
                                requireContext() ,
                                "Registration Failed: ${task.exception?.message}" ,
                                Toast.LENGTH_SHORT
                                      ).show()
                    }
                }
    }

    private fun validateInputs(email : String , password : String) : Boolean {
        // Check if email is valid
        if (! isValidEmail(email)) {
            Toast.makeText(requireContext() , "Invalid email format" , Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if password is valid
        if (! isValidPassword(password)) {
            Toast.makeText(
                    requireContext() ,
                    "Password must be at least 6 characters" ,
                    Toast.LENGTH_SHORT
                          ).show()
            return false
        }

        // If everything is valid, proceed with login logic
        // Toast.makeText(requireContext() , "Login successful!" , Toast.LENGTH_SHORT).show()
        // Proceed with authentication...
        return true
    }

    private fun isValidEmail(email : String) : Boolean {
        return ! TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
    }

    private fun isValidPassword(password : String) : Boolean {
        return password.length >= 6
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).hideTheToolBar(View.GONE)
            (requireActivity() as MainActivity).hideBottomNavigationView(View.GONE)
        }
    }
}