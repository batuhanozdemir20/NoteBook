package com.batuhanozdemir.notdefteri.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.room.Room
import androidx.room.RoomDatabase
import com.batuhanozdemir.notdefteri.databinding.ActivityDetailsBinding
import com.batuhanozdemir.notdefteri.model.Note
import com.batuhanozdemir.notdefteri.room.NoteDAO
import com.batuhanozdemir.notdefteri.room.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var db: NoteDatabase
    private lateinit var noteDAO: NoteDAO
    private var selectedNote: Note? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = Room.databaseBuilder(applicationContext,NoteDatabase::class.java,"Notes").build()
        noteDAO = db.noteDao()

        val newNote = intent.getBooleanExtra("new",true)

        if (!newNote){ // Selected Note
            binding.saveBtn.visibility = View.GONE
            binding.editBtn.visibility = View.GONE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                selectedNote = intent.getSerializableExtra("selectedNote",Note::class.java)
            }else{
                selectedNote = intent.getSerializableExtra("selectedNote") as Note
            }
            selectedNote?.let {
                binding.titleText.setText(it.title)
                binding.noteContentText.setText(it.content)
            }

            binding.noteContentText.doAfterTextChanged {
                binding.editBtn.visibility = View.VISIBLE
            }
            binding.titleText.doAfterTextChanged {
                binding.editBtn.visibility = View.VISIBLE
            }

        }else{ // New Note
            binding.titleText.setText("")
            
            binding.noteContentText.setText("")
            binding.saveBtn.visibility = View.GONE
            binding.editBtn.visibility = View.GONE
            binding.deleteBtn.visibility = View.GONE

            binding.noteContentText.doAfterTextChanged {
                binding.saveBtn.visibility = View.VISIBLE
            }
        }


    }

    fun save(view: View){
        val note = Note(
            binding.titleText.text.toString(),
            binding.noteContentText.text.toString()
        )
        CoroutineScope(Dispatchers.Main).launch{
            noteDAO.addNote(note)
            backToMain()
            Toast.makeText(this@DetailsActivity,"Not EKlendi",Toast.LENGTH_LONG).show()
        }
    }

    fun edit(view: View){
        selectedNote?.let {
            val updatedNote = Note(
                binding.titleText.text.toString(),
                binding.noteContentText.text.toString()
            )
            updatedNote.id = it.id
            CoroutineScope(Dispatchers.Main).launch {
                noteDAO.updateNote(updatedNote)
                backToMain()
                Toast.makeText(this@DetailsActivity,"Not Kaydedildi",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun delete(view: View){
        val alert = AlertDialog.Builder(this)
            .setMessage("Bu notu silmek istediğinize emin misiniz?")
            .setPositiveButton("Evet", DialogInterface.OnClickListener { dialogInterface, i ->
                selectedNote?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        noteDAO.deleteNote(it)
                        backToMain()
                        Toast.makeText(this@DetailsActivity,"Not Silindi",Toast.LENGTH_LONG).show()
                    }
                }
            })
            .setNegativeButton("Hayır",object : OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    println("Notu silmekten vaz geçildi")
                }
            })

        alert.show()

    }

    fun backToMain(){
        val backToList = Intent(this@DetailsActivity,MainActivity::class.java)
        backToList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(backToList)
    }
}