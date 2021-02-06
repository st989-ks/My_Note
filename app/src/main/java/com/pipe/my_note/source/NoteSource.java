package com.pipe.my_note.source;

import android.content.res.Resources;

import com.pipe.my_note.R;

import java.util.ArrayList;

public class NoteSource implements NoteInterface {
    private final ArrayList<Note> notes;
    private final Resources res;

    public NoteSource(Resources resources) {
        this.notes = new ArrayList<>(3);
        this.res = resources;
    }

    public NoteSource init() {
        String[] notesArray = res.getStringArray(R.array.title);
        for (int i = 0; i < notesArray.length; i++) {
            notes.add(createNewNote(i));
        }
        return this;
    }

    private Note createNewNote(int index) {
        Note note = new Note(res.getStringArray(R.array.title)[index],
                res.getStringArray(R.array.tags)[index],
                res.getIntArray(R.array.key)[index],
                Long.parseLong(res.getStringArray(R.array.date)[index]),
                res.getIntArray(R.array.related_cards)[index],
                res.getStringArray(R.array.text)[index]);
        return note;


    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int size() {
        return notes.size();
    }

    @Override
    public void addNote(Note note) {
        notes.add(note);
    }

    @Override
    public void updateNote(int position, Note note) {
        notes.set(position, note);
    }

    @Override
    public void deleteNote(int position) {
        notes.remove(position);
    }

    @Override
    public void clearNotes() {
        notes.clear();
    }
}
