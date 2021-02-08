package com.pipe.my_note.observe;


import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSourceImpl;

public interface Observer {
    void updateNotes(NoteData noteData);
}

