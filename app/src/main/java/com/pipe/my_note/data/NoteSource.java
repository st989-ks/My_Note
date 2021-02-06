package com.pipe.my_note.data;

public interface NoteSource {
    NoteData getNoteData(int position);

    int size();

    void deleteNoteData(int position);

    void updateNoteData(int position, NoteData note);

    void addNoteData(NoteData note);

    void clearNoteData();
}
