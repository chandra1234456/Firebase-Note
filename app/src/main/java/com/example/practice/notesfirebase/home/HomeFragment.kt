package com.example.practice.notesfirebase.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.practice.notesfirebase.MainActivity
import com.example.practice.notesfirebase.R
import com.example.practice.notesfirebase.databinding.FragmentHomeBinding
import com.example.practice.notesfirebase.home.adapter.HomeNotesAdapter
import com.example.practice.notesfirebase.home.data.NotesData
import com.example.practice.notesfirebase.remainder.ReminderReceiver
import com.example.practice.notesfirebase.util.EncryptedSharedPreferencesManager.getLoadTheData
import com.example.practice.notesfirebase.util.Progress
import com.example.practice.notesfirebase.util.setOnSingleClickListener
import com.example.practice.notesfirebase.util.toastMessage
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment(),HomeListener {
    private lateinit var homeBinding : FragmentHomeBinding
    private lateinit var firebaseStorage : FirebaseStorage
    private var email = ""
    private lateinit var notesAdapter : HomeNotesAdapter
    private var fetchedNotesData :List<NotesData> = ArrayList()
    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        firebaseStorage = FirebaseStorage.getInstance()
        homeBinding.addNotes.setOnSingleClickListener {
            findNavController().navigate(R.id.addNoteFragment)
        }

      /*  // Start a custom trace
        val customTrace = FirebasePerformance.getInstance().newTrace("custom_trace")
        customTrace.start()
      // Stop the custom trace when the operation is completed
          customTrace.stop()*/
        return homeBinding.root
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val (userEmail , userPassword) = getLoadTheData(requireContext())
            ?: Pair("defaultEmail@example.com" , "defaultPassword")
        if (userEmail != null) {
            email = userEmail
            fetchAllUserDataALL(email)
            Progress.show(requireContext())
            val versionName = try {
                val packageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
                packageInfo.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                "Unknown" // Default value in case of error
            }
            (activity as? HomeFragmentListener)?.updateDrawerData(email,versionName)

        }
        activity?.let {
            val actionBar = (it as AppCompatActivity).supportActionBar
            actionBar?.title = "DashBoard"
            actionBar?.setDisplayHomeAsUpEnabled(true)  // Hide the back arrow
            actionBar?.setHomeAsUpIndicator(R.drawable.ic_menu) // Set a custom icon if needed
        }
    }

    private fun fetchAllUserDataALL(email : String) {
        // val usersRef = firebaseStorage.reference.child("users/") // Change to the correct directory
        val sanitizedEmail = email.replace("." , "_").replace("@" , "_")
        val usersRef = firebaseStorage.reference.child(email)
        usersRef.listAll().addOnSuccessListener { listResult ->
            // Map over the items to fetch data
            val fetchTasks = listResult.items.map { fileRef ->
                // Download each file as bytes
                fileRef.getBytes(Long.MAX_VALUE).continueWith { task ->
                    if (task.isSuccessful) {
                        val jsonData = String(task.result)

                        try {
                            // Parse JSON data into a list of NotesData objects
                            val outerListType = object : TypeToken<NotesData>() {}.type
                            val userNotesData : NotesData =
                                Gson().fromJson(jsonData , outerListType)
                            Log.d("TAG" , "fetchAllUserDataALL: $userNotesData")

                            return@continueWith userNotesData // Return the parsed data
                        } catch (e : JsonSyntaxException) {
                            e.printStackTrace()  // Log the error for debugging
                            return@continueWith null // Return null in case of parsing error
                        }
                    } else {
                        task.exception?.printStackTrace() // Log the download failure
                        return@continueWith null // Return null if download failed
                    }
                }
            }

            // Once all tasks are complete, process the results
            Tasks.whenAllComplete(fetchTasks).addOnCompleteListener {
                // Collect the successful results (i.e., non-null results)
                 fetchedNotesData = fetchTasks.mapNotNull { it.result }
                Log.d("TAG" , "Fetched NotesData: $fetchedNotesData")
                Progress.dismiss()
                if (fetchedNotesData.isNotEmpty()) {
                    notesAdapter = HomeNotesAdapter(fetchedNotesData)
                    homeBinding.apply {
                        notesRecyclerview.visibility = View.VISIBLE
                        tvNoNotes.visibility = View.GONE
                        notesRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
                        notesRecyclerview.adapter = notesAdapter
                    }
                } else {
                    homeBinding.apply {
                        notesRecyclerview.visibility = View.GONE
                        tvNoNotes.visibility = View.VISIBLE
                    }
                }
            }
        }.addOnFailureListener { exception ->
            // If listing files fails, display a message
            Toast.makeText(
                    requireContext() ,
                    "Fetch failed: ${exception.message}" ,
                    Toast.LENGTH_SHORT
                          ).show()
        }

    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).hideTheToolBar(View.VISIBLE)
            (requireActivity() as MainActivity).hideBottomNavigationView(View.VISIBLE)
        }
    }

    override fun sortOptionDialog() {
        toastMessage(requireContext(),"HOME")
        if (fetchedNotesData.isNotEmpty()) {
            notesAdapter = HomeNotesAdapter(fetchedNotesData)
            homeBinding.apply {
                notesRecyclerview.visibility = View.VISIBLE
                tvNoNotes.visibility = View.GONE
                notesRecyclerview.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
                notesRecyclerview.adapter = notesAdapter
            }
        } else {
            homeBinding.apply {
                notesRecyclerview.visibility = View.GONE
                tvNoNotes.visibility = View.VISIBLE
            }
        }

    }
}