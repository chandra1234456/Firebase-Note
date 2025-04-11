package com.example.practice.notesfirebase.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.example.practice.notesfirebase.R

object Progress {
    private var dialog : Dialog? = null
    fun show(context : Context) : Dialog {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.layout_progress , null)
        dialog = Dialog(context , R.style.CustomProgressBarTheme)
        if (dialog !!.window != null) {
            dialog !!.window !!.setBackgroundDrawableResource(R.color.bg_color)
        }
        dialog !!.setContentView(view)
        dialog?.show()
        dialog !!.setCancelable(false)
        return dialog !!
    }

    fun dismiss() {
        dialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }
}