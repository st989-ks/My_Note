package com.pipe.my_note.observe;


import com.pipe.my_note.data.NoteData;

public interface Observer {
    void updateNotes(NoteData noteData);
}

