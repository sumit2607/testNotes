package com.example.testnotes.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.testnotes.R
import com.example.testnotes.data.model.Note
import com.example.testnotes.databinding.ActivityNoteEntryBinding
import com.example.testnotes.viewModels.NoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteEntryActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityNoteEntryBinding
    private val viewModel: NoteViewModel by viewModels()
    private var noteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        noteId = intent.getIntExtra("NOTE_ID", -1)
        val noteTitle = intent.getStringExtra("NOTE_TITLE") ?: ""
        val noteDescription = intent.getStringExtra("NOTE_DESCRIPTION") ?: ""

        if (noteId != -1) {
            viewModel.setTitle(noteTitle)
            viewModel.setDescription(noteDescription)
        }

        saveNoteAutomatically()
    }

    private fun saveNoteAutomatically() {
        lifecycleScope.launch {
            while (true) {
                delay(5000)
                withContext(Dispatchers.IO) {
                    viewModel.saveNote()
                }
            }
        }
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                viewModel.saveNote()
            }
            super.onBackPressed()
        }
    }
}