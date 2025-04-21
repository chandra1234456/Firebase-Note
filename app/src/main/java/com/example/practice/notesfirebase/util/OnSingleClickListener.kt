package com.example.practice.notesfirebase.util

import android.view.View

class OnSingleClickListener(private val delay : Long , private val block : () -> Unit) :
        View.OnClickListener {
    private var lastClickTime = 0L

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v : View?) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime < delay) {
            return
        }
        lastClickTime = currentTime
        block()
    }
}

fun View.setOnSingleClickListener(delay : Long = 500L , block : () -> Unit) {
    setOnClickListener(OnSingleClickListener(delay , block))
}
