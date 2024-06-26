package com.example.testnotes.viewModels

// NoteViewModel.kt


import android.app.Application
import androidx.databinding.InverseMethod
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testnotes.data.model.Note
import com.example.testnotes.data.room.NoteDatabase
import com.example.testnotes.repo.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    val allNotes: Flow<List<Note>>

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }

    fun setTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun setDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    // Function to delete a note
// Function to delete notes by their IDs
    fun deleteNotes(noteIds: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNotes(noteIds)
        }
    }




    fun saveNote() = viewModelScope.launch(Dispatchers.IO) {
        val newNote = Note(
            // Change this appropriately if you're updating
            title = _title.value ?: "",
            description = _description.value ?: ""
        )
        repository.insert(newNote)
    }
}

