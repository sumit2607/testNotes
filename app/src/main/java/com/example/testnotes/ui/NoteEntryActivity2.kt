package com.example.testnotes.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

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

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                runnable?.let { handler.removeCallbacks(it) }
                runnable = Runnable {
                    val note = Note(
                        title = binding.noteTitle.text.toString(),
                        description = binding.noteDescription.text.toString() )
                    viewModel.insert(note)
                }
                handler.postDelayed(runnable!!, 5000)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.noteTitle.addTextChangedListener(textWatcher)
        binding.noteDescription.addTextChangedListener(textWatcher)

    }


    override fun onBackPressed() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                    val note = Note(
                        title = binding.noteTitle.text.toString(),
                        description = binding.noteDescription.text.toString() )
                    viewModel.insert(note)
                }
            }
            super.onBackPressed()
        }
    }
