package com.batuhanozdemir.notdefteri.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.batuhanozdemir.notdefteri.databinding.RecyclerRowBinding
import com.batuhanozdemir.notdefteri.model.Note
import com.batuhanozdemir.notdefteri.view.DetailsActivity

class NoteAdapter(val noteList: List<Note>): RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    class NoteHolder(val binding: RecyclerRowBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NoteHolder(binding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.binding.recyclerRowTitleText.text = noteList[position].title
        holder.binding.recyclerRowContentText.text = noteList[position].content
        holder.itemView.setOnClickListener {
            val showNote = Intent(it.context,DetailsActivity::class.java)
            showNote.putExtra("selectedNote",noteList[position])
            showNote.putExtra("new",false)
            it.context.startActivity(showNote)
        }
    }

}