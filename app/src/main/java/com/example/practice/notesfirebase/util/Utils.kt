package com.example.practice.notesfirebase.util

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

fun toastMessage(context : Context , message : String) {
    Toast.makeText(context , message , Toast.LENGTH_SHORT).show()
}

fun splitGetFirstString(splitName :String,whereToSplit :String):String{
    val split = splitName.split(whereToSplit)
    return split[0]
}

fun isValidEmail(email : String) : Boolean {
    return ! TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
}
