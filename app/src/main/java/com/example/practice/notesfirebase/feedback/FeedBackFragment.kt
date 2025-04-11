package com.example.practice.notesfirebase.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.practice.notesfirebase.MainActivity
import com.example.practice.notesfirebase.R
import com.example.practice.notesfirebase.databinding.FragmentFeedBackBinding
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class FeedBackFragment : Fragment() {
    private lateinit var feedBackBinding : FragmentFeedBackBinding
    private lateinit var firebaseFireStore : FirebaseFirestore
    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        feedBackBinding = FragmentFeedBackBinding.inflate(layoutInflater)
        firebaseFireStore = FirebaseFirestore.getInstance()
        /**
         * Default Code  if u write code mainActivity it will affect all Fragments  if u write particular fragment  it will automatically u will get Notification
         * This Will Be The Notification for Feedback ..then Click on That Notification..it will redirect the feed back Thing
         */

        /*  Firebase.appDistribution.showFeedbackNotification(
                  // Text providing notice to your testers about collection and
                  // processing of their feedback data
                  "R.string.additionalFormText",
                  // The level of interruption for the notification
                  InterruptionLevel.HIGH)*/
        (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
                .setNavigationOnClickListener {
                    findNavController().navigate(R.id.homeFragment)
                }
        //This For Custom Notification In Particular Fragment
        Firebase.appDistribution.startFeedback("Please Write Ur Feedback and Comments!!")
        return feedBackBinding.root

    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).hideBottomNavigationView(View.GONE)
        }
    }
}