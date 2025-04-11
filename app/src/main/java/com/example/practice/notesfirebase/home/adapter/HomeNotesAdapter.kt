package com.example.practice.notesfirebase.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.notesfirebase.databinding.LayoutUserNotesBinding
import com.example.practice.notesfirebase.home.data.NotesData


class HomeNotesAdapter(private val notesList : List<NotesData>) :
        RecyclerView.Adapter<HomeNotesAdapter.NotesViewHolder>() {

    // ViewHolder class using ViewBinding
    inner class NotesViewHolder(private val binding : LayoutUserNotesBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(note : NotesData) {
            binding.tvNotesTitle.text = note.title
            binding.tvDescription.text = note.description
            // Bind more fields if necessary, e.g., createDate, createdBy, remainderDate
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : NotesViewHolder {
        val binding =
            LayoutUserNotesBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder : NotesViewHolder , position : Int) {
        val note = notesList[position]
        holder.bind(note)
    }

    override fun getItemCount() = notesList.size
}
