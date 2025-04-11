package com.example.practice.notesfirebase

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat


object CustomProgressDialog {
    private lateinit var dialog : Dialog

    // Initialize dots and animation variables
    private lateinit var dots : MutableList<View>
    private var currentDotIndex = 0
    private val handler = Handler()

    fun show(context : Context) {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_progress_animation)
        dialog.setCancelable(false)

        // Initialize dots
        dots = mutableListOf(
                dialog.findViewById(R.id.dot1) ,
                dialog.findViewById(R.id.dot2) ,
                dialog.findViewById(R.id.dot3) ,
                dialog.findViewById(R.id.dot4) ,
                dialog.findViewById(R.id.dot5)
                            )

        if (! dialog.isShowing) {
            dialog.show()
            animateDots()
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
            resetDots()
            currentDotIndex = 0 // Reset the index for future use
        }
    }

    private fun animateDots() {
        resetDots()
        currentDotIndex = 0

        val runnable = object : Runnable {
            override fun run() {
                if (currentDotIndex < dots.size) {
                    // Start bounce animation
                    val bounceAnim = AnimationUtils.loadAnimation(dialog.context, R.anim.dots_bounce)
                    dots[currentDotIndex].startAnimation(bounceAnim)
                    dots[currentDotIndex].setBackgroundColor(
                            ContextCompat.getColor(
                                    dialog.context ,
                                    android.R.color.holo_blue_light
                                                  )
                                                            )
                    currentDotIndex ++
                    handler.postDelayed(this , 500) // Delay for animation effect
                } else {
                    resetDots() // Reset after animation
                    currentDotIndex = 0 // Reset index
                    handler.postDelayed(this , 500) // Repeat the animation
                }
            }
        }
        handler.post(runnable)
    }

    private fun resetDots() {
        dots.forEach { dot ->
            dot.setBackgroundColor(
                    ContextCompat.getColor(
                            dialog.context ,
                            android.R.color.darker_gray
                                          )
                                  ) // Reset to default color
        }
    }
}
