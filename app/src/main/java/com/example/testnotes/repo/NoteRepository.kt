package com.example.testnotes.repo

// NoteRepository.kt


import androidx.lifecycle.LiveData
import com.example.testnotes.data.model.Note
import com.example.testnotes.data.room.NoteDao
import kotlinx.coroutines.flow.Flow


class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    // Function to delete notes by their IDs
    suspend fun deleteNotes(noteIds: Int) {
        noteDao.deleteNotesByIds(noteIds)
    }
}
