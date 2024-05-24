package com.example.testnotes.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testnotes.R
import com.example.testnotes.data.model.Note
import com.example.testnotes.databinding.ActivityMainBinding
import com.example.testnotes.ui.adapter.NoteAdapter
import com.example.testnotes.viewModels.NoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: NoteViewModel by viewModels()
    private val adapter = NoteAdapter { note -> onNoteSelected(note) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel // Setting the viewModel in binding
        binding.lifecycleOwner = this  // Setting the lifecycle owner

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        /*viewModel.allNotes.observe(this, Observer { notes ->
            notes?.let { adapter.submitList(it) }
        })*/

        lifecycleScope.launch {
            viewModel.allNotes.collectLatest {
               adapter.submitList(it)
            }
        }

        binding.addNoteButton.setOnClickListener {
            val intent = Intent(this, NoteEntryActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun onNoteSelected(note: Note) {
        val intent = Intent(this, NoteEntryActivity2::class.java).apply {
            putExtra("NOTE_ID", note.id)
            putExtra("NOTE_TITLE", note.title)
            putExtra("NOTE_DESCRIPTION", note.description)
        }
        startActivity(intent)
    }
}