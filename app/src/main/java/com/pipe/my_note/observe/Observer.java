package com.pipe.my_note.observe;


import com.pipe.my_note.source.Note;

public interface Observer {
    void updateNotes(Note note);
}
