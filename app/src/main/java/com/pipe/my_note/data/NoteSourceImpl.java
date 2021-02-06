package com.pipe.my_note.data;

import android.content.res.Resources;

import com.pipe.my_note.R;

import java.util.ArrayList;
import java.util.List;

public class NoteSourceImpl implements NoteSource {
    private final List<NoteData> noteData;
    private final Resources resources;

    public NoteSourceImpl(Resources resources) {
        noteData = new ArrayList<>(3);
        this.resources = resources;
    }

    public NoteSourceImpl init() {
        String[] notesArray = resources.getStringArray(R.array.title);
        for (int i = 0; i < notesArray.length; i++) {
            noteData.add(createNewNote(i));
        }
        return this;
    }

    private NoteData createNewNote(int index) {
        int likeInt = Integer.parseInt(resources.getStringArray(R.array.like)[index]);
        Boolean like = likeInt == 1;
        NoteData note = new NoteData(
                resources.getStringArray(R.array.title)[index],
                resources.getStringArray(R.array.tags)[index],
                resources.getIntArray(R.array.key)[index],
                Long.parseLong(resources.getStringArray(R.array.date)[index]),
                resources.getIntArray(R.array.related_cards)[index],
                resources.getStringArray(R.array.text)[index],
                like);

        return note;


    }

    @Override
    public NoteData getNoteData(int position) {
        return noteData.get(position);
    }

    @Override
    public int size() {
        return noteData.size();
    }

    @Override
    public void addNoteData(NoteData note) {
        noteData.add(note);
    }

    @Override
    public void updateNoteData(int position, NoteData note) {
        noteData.set(position, note);
    }

    @Override
    public void deleteNoteData(int position) {
        noteData.remove(position);
    }

    @Override
    public void clearNoteData() {
        noteData.clear();
    }
}
