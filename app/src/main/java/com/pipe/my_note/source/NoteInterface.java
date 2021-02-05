package com.pipe.my_note.source;

public interface NoteInterface {
    Note getNote(int position);

    int size();

    void addNote(Note note);

    void updateNote(int position, Note note);

    void deleteNote(int position);

    void clearNotes();
}
