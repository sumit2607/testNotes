package com.example.testnotes.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testnotes.data.model.Note
import com.example.testnotes.databinding.ActivityMainBinding
import com.example.testnotes.ui.adapter.NoteAdapter
import com.example.testnotes.viewModels.NoteViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: NoteViewModel by viewModels()
    private var adapter = NoteAdapter(
        onNoteSelected = { note -> onNoteSelected(note) },
        onDeleteSelected = {

                selectedNotes ->
            selcetedNotid
            binding.deleteButton.visibility = View.VISIBLE
            binding.addNoteButton.visibility = View.GONE
            binding.deleteButton.setOnClickListener {

                deleteNotes(selectedNotes)
                binding.deleteButton.visibility = View.GONE
                binding.addNoteButton.visibility = View.VISIBLE
            }
        }
    )
    private var selcetedNotid: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        binding.deleteButton.visibility = View.GONE
        binding.addNoteButton.visibility = View.VISIBLE


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allNotes.collectLatest { adapter?.submitList(it) }
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


    private fun deleteNotes(selectedNotes: Note) {
        viewModel.deleteNotes(selectedNotes.id)
    }
}
