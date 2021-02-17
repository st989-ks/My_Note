package com.pipe.my_note.data;

public interface NoteSource {

    NoteSource init(NotesSourceResponse noteSourceResponse);

    NoteData getNoteData(int note);
    int getNoteDataPosition(NoteData findData);
    int size();
    void deleteNoteData(int position);
    void updateNoteData(int position, NoteData note);
    void addNoteData(NoteData noteData);
    void clearNoteData();
}
