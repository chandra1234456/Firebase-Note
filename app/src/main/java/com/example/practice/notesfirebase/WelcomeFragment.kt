package com.example.practice.notesfirebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.practice.notesfirebase.databinding.FragmentWelcomeBinding
import com.example.practice.notesfirebase.util.EncryptedSharedPreferencesManager.insertAndSaveData
import com.example.practice.notesfirebase.util.Progress
import com.google.firebase.auth.FirebaseAuth

class WelcomeFragment : Fragment() {
    private lateinit var welcomeBinding: FragmentWelcomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
                             ): View {
        welcomeBinding = FragmentWelcomeBinding.inflate(inflater)
        firebaseAuth = FirebaseAuth.getInstance()

        // Set the listener for the "Get Started" button
        welcomeBinding.btnGetStarted.setOnClickListener {
            Progress.show(requireContext())
            val email = welcomeBinding.etCustomName.text.toString() + "@gmail.com"
            val password = "userPassword"// Get the actual password input from the user
            if (validateInputs(email, password)) {
                handleEmailCheckResult(email, password)
            } else {
                Progress.dismiss()
            }
        }

        // Handle back press
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return welcomeBinding.root
    }

    private fun handleEmailCheckResult(email: String, password: String) {
        checkIfEmailExists(email) { exists ->
            if (exists) {
                Toast.makeText(requireContext(), "Email exists, proceeding to login", Toast.LENGTH_SHORT).show()
                // Perform login if email exists
                loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Email not found, please register", Toast.LENGTH_SHORT).show()
                // Perform registration if email does not exist
                registerUser(email, password)
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        // Validate email and password
        if (email.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please Enter Your Email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please Enter Your Password", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Please Enter a Valid Email", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    Progress.dismiss()  // Dismiss progress UI after task completes
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                        insertAndSaveData(requireContext(), email, password)
                        navigateToHomeFragment()
                    } else {
                        Toast.makeText(requireContext(), "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    Progress.dismiss()  // Dismiss progress UI after task completes
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                        insertAndSaveData(requireContext(), email, password)
                        navigateToHomeFragment()
                    } else {
                        if (task.exception?.message?.contains("email address is already in use") == true) {
                            // If the email is already in use, inform the user and proceed with login
                            Toast.makeText(requireContext(), "Email already in use, proceeding to login", Toast.LENGTH_SHORT).show()
                            loginUser(email, password)
                        } else {
                            Toast.makeText(requireContext(), "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
    }

    private fun checkIfEmailExists(email: String, onResult: (Boolean) -> Unit) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods
                        // If signInMethods is not empty, it means the email exists
                        onResult(! signInMethods.isNullOrEmpty())
                    } else {
                        onResult(false)
                    }
                }
    }

    private fun navigateToHomeFragment() {
        val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.welcomeFragment, true)
                .build()
        findNavController().navigate(R.id.homeFragment, null, navOptions)
    }



    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).hideTheToolBar(View.GONE)
            (requireActivity() as MainActivity).hideBottomNavigationView(View.GONE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }
}
