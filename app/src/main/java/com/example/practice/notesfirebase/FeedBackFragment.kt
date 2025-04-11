package com.example.practice.notesfirebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practice.notesfirebase.databinding.FragmentFeedBackBinding
import com.example.practice.notesfirebase.util.toastMessage
import com.google.firebase.firestore.FirebaseFirestore

class FeedBackFragment : Fragment() {
    private lateinit var feedBackBinding : FragmentFeedBackBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        feedBackBinding = FragmentFeedBackBinding.inflate(layoutInflater)
        firebaseFirestore = FirebaseFirestore.getInstance()
        (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
                .setNavigationOnClickListener {
                    findNavController().navigate(R.id.homeFragment)
                }
        feedBackBinding.btnFeedBack.setOnClickListener {
            val feedback = feedBackBinding.etFeedBackNote.text.toString().trim()
            if (feedback.isNotEmpty()) {
                submitFeedback(feedback)
            } else {
                toastMessage(requireContext(),"Feed Back")
            }
        }
        return feedBackBinding.root

    }
    private fun submitFeedback(feedback: String) {
        val feedbackData = hashMapOf(
                "feedback" to feedback,
                "timestamp" to System.currentTimeMillis()
                                    )

        firebaseFirestore.collection("appFeedback")
                .add(feedbackData)
                .addOnSuccessListener {
                    toastMessage(requireContext(), "Feedback submitted successfully!")
                    findNavController().navigate(R.id.homeFragment)
                }
                .addOnFailureListener {
                    toastMessage(requireContext(),"Failed to submit feedback")
                }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).hideBottomNavigationView(View.GONE)
        }
    }
}