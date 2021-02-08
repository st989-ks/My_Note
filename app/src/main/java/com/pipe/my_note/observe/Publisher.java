package com.pipe.my_note.observe;

import com.pipe.my_note.data.NoteData;
import com.pipe.my_note.data.NoteSourceImpl;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    public void unsubscribeAll() {
        observers.clear();
    }

    public void notifySingle(NoteData noteData) {
        for (Observer observer : observers) {
            observer.updateNotes(noteData);
            unsubscribe(observer);
        }
    }
}
