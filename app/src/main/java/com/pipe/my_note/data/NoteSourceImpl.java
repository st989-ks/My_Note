package com.pipe.my_note.data;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.pipe.my_note.R;

import java.util.ArrayList;
import java.util.Calendar;

public class NoteSourceImpl implements NoteSource, Parcelable {
    public static final Creator<NoteSourceImpl> CREATOR = new Creator<NoteSourceImpl>() {
        @Override
        public NoteSourceImpl createFromParcel(Parcel in) {
            return new NoteSourceImpl(in);
        }

        @Override
        public NoteSourceImpl[] newArray(int size) {
            return new NoteSourceImpl[size];
        }
    };
    private ArrayList<NoteData> notes;
    private Resources resources;

    public NoteSourceImpl(Resources resources) {
        this.resources = resources;
        notes = new ArrayList<>();
    }

    // Коллекция документов

    protected NoteSourceImpl(Parcel in) {
        notes = in.createTypedArrayList(NoteData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(notes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

//    public NoteSourceImpl init() {
//        String[] notesArray = resources.getStringArray(R.array.title);
//        for (int i = 0; i < notesArray.length; i++) {
//            notes.add(createNewNote(i));
//        }
//        return this;
//    }

    private NoteData createNewNote(int index) {
        int likeInt = resources.getIntArray(R.array.like)[index];
        Boolean like = likeInt == 1;
        NoteData note = new NoteData(resources.getStringArray(R.array.title)[index],
                resources.getStringArray(R.array.tags)[index],
                resources.getStringArray(R.array.key)[index],
                resources.getStringArray(R.array.date_create)[index],
                resources.getStringArray(R.array.related_cards)[index],
                resources.getStringArray(R.array.text)[index],
                like);
        return note;
    }

    @Override
    public NoteSource init(NoteSourceResponse noteSourceResponse) {

        String[] name = resources.getStringArray(R.array.title);
        String[] tags = resources.getStringArray(R.array.tags);
        String[] key = resources.getStringArray(R.array.key);
        String[] created = resources.getStringArray(R.array.date_create);
        String[] linkCard = resources.getStringArray(R.array.related_cards);
        String[] text = resources.getStringArray(R.array.text);
        int[] likeInt = resources.getIntArray(R.array.like);
        // заполнение источника данных
        for (int i = 0; i < key.length; i++) {
            notes.add(new NoteData(name[i], tags[i],
                    key[i], created[i], linkCard[i],text[i],likeInt[i] == 1));
        }

        if (noteSourceResponse != null) {
            noteSourceResponse.initialized(this);
        }

        return this;
    }

    @Override
    public NoteData getNoteData(int position) {
        return notes.get(position);
    }

    @Override
    public int size() {
        return notes.size();
    }

    @Override
    public void addNoteData(NoteData note) {
        notes.add(note);
    }

    @Override
    public void updateNoteData(int position, NoteData note) {
        notes.set(position, note);
    }

    @Override
    public void deleteNoteData(int position) {
        notes.remove(position);
    }

    @Override
    public void clearNoteData() {
        notes.clear();
    }
}
