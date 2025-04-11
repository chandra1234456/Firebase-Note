package com.example.practice.notesfirebase.home.data


import com.google.gson.annotations.SerializedName

data class NotesData(
    @SerializedName("createDate")
    var createDate: String,
    @SerializedName("createdBy")
    var createdBy: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("remainderDate")
    var remainderDate: String,
    @SerializedName("title")
    var title: String
)