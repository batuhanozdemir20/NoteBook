package com.batuhanozdemir.notdefteri.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.batuhanozdemir.notdefteri.adapter.NoteAdapter
import com.batuhanozdemir.notdefteri.databinding.ActivityMainBinding
import com.batuhanozdemir.notdefteri.model.Note
import com.batuhanozdemir.notdefteri.room.NoteDAO
import com.batuhanozdemir.notdefteri.room.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NoteDatabase
    private lateinit var noteDao: NoteDAO
    private lateinit var noteAdapter: NoteAdapter
    private var noteList: List<Note> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = Room.databaseBuilder(applicationContext,NoteDatabase::class.java,"Notes").build()
        noteDao = db.noteDao()

        CoroutineScope(Dispatchers.Main).launch {
            noteDao.getAllNotes().collect{notes ->
                binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                noteAdapter = NoteAdapter(notes)
                binding.recyclerView.adapter = noteAdapter
                if (notes.isEmpty()){
                    binding.infoText.visibility = View.VISIBLE
                }
            }
        }
    }

    fun addNote(view: View){
        val intent = Intent(this@MainActivity,DetailsActivity::class.java)
        startActivity(intent)
    }
}