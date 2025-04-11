package com.example.practice.notesfirebase.addNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practice.notesfirebase.MainActivity
import com.example.practice.notesfirebase.R
import com.example.practice.notesfirebase.databinding.FragmentAddNoteBinding
import com.example.practice.notesfirebase.home.data.NotesData
import com.example.practice.notesfirebase.util.EncryptedSharedPreferencesManager.getLoadTheData
import com.example.practice.notesfirebase.util.Progress
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import java.io.ByteArrayInputStream


class AddNoteFragment : Fragment() {
    private lateinit var addNoteBinding : FragmentAddNoteBinding
    private lateinit var firebaseStorage : FirebaseStorage
    private var email = ""
    private lateinit var callback : OnBackPressedCallback

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        addNoteBinding = FragmentAddNoteBinding.inflate(layoutInflater)
        firebaseStorage = FirebaseStorage.getInstance()
        addNoteBinding.saveNotes.setOnClickListener {
            Progress.show(requireContext())
            val user = NotesData(
                    addNoteBinding.nameInput.text.toString() ,
                    addNoteBinding.emailInput.text.toString() ,
                    addNoteBinding.nameInput.text.toString() ,
                    addNoteBinding.nameInput.text.toString() ,
                    addNoteBinding.nameInput.text.toString()
                                )
            uploadUserDataToFirebase(user)

        }
        (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
                .setNavigationOnClickListener {
                    findNavController().navigate(R.id.homeFragment)
                }

        // This callback will only be called when MyFragment is at least Started.
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                findNavController().navigate(R.id.homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner , callback)

        return addNoteBinding.root
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val (userEmail , userPassword) = getLoadTheData(requireContext())
            ?: Pair("defaultEmail@example.com" , "defaultPassword")
        email = userEmail !!
    }

    private fun uploadUserDataToFirebase(user : NotesData) {
        // Convert user data to JSON
        val jsonData = Gson().toJson(user)
        val byteArrayInputStream = ByteArrayInputStream(jsonData.toByteArray())

        // Create a reference to the storage
        val storageRef = firebaseStorage.reference
        val sanitizedEmail = user.createdBy.replace("." , "_").replace("@" , "_")
        val userRef =
            storageRef.child("$email/${sanitizedEmail}.json") // Use email as file name or adjust accordingly

        // Upload the JSON data
        val uploadTask = userRef.putStream(byteArrayInputStream)

        uploadTask.addOnSuccessListener {
            Progress.dismiss()
            Toast.makeText(
                    requireContext() ,
                    "User data uploaded successfully!" ,
                    Toast.LENGTH_SHORT
                          ).show()
            findNavController().navigate(R.id.homeFragment)
        }.addOnFailureListener { exception ->
            Toast.makeText(
                    requireContext() ,
                    "Upload failed: ${exception.message}" ,
                    Toast.LENGTH_SHORT
                          ).show()
        }
    }


      override fun onResume() {
          super.onResume()
          requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
          if (requireActivity() is MainActivity) {
              (requireActivity() as MainActivity).hideBottomNavigationView(View.GONE)
          }
      }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.remove()
    }


}