package com.pipe.my_note.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotesSourceFirebaseImpl implements NoteSource {

    private static final String NOTE_COLLECTION = "notes";
    private static final String TAG = "[NoteSourceFirebaseImpl]";

    // База данных Firestore
    private FirebaseFirestore store = FirebaseFirestore.getInstance();

    // Коллекция документов
    private CollectionReference collection = store.collection(NOTE_COLLECTION);

    // Загружаемый список карточек
    private List<NoteData> notesData = new ArrayList<>();

    private Exception exception;

    @Override
    public NoteSource init(NotesSourceResponse notesSourceResponse) {
        collection.orderBy(NoteDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    // При удачном считывании данных загрузим список карточек
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            notesData = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                NoteData noteData = NoteDataMapping.toCardData(id, doc);
                                notesData.add(noteData);
                            }
                            Log.d(TAG, "success " + notesData.size() + " qnt");
                            notesSourceResponse.initialized(NotesSourceFirebaseImpl.this);
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            clearNoteData();
                            exception = task.getException();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                        clearNoteData();
                        exception = e;
                    }
                });
        return this;
    }

    @Override
    public NoteData getNoteData(int position) {
        return notesData.get(position);
    }

    @Override
    public int size() {
        if (notesData == null) {
            return 0;
        }
        return notesData.size();
    }

    @Override
    public void deleteNoteData(int position) {
        // Удалить документ с определённым идентификатором
        collection.document(notesData.get(position).getId()).delete()/*.addOnSuccessListener()*/;
        notesData.remove(position);
    }

    @Override
    public void updateNoteData(int position, NoteData noteData) {
        String id = noteData.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(NoteDataMapping.toDocument(noteData))/*.addOnSuccessListener()*/;
        notesData.set(position, noteData);
    }

    @Override
    public void addNoteData(NoteData noteData) {
        // Добавить документ
        collection.add(NoteDataMapping.toDocument(noteData)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                noteData.setId(documentReference.getId());
            }
        })/*.addOnFailureListener()*/;
    }

    @Override
    public void clearNoteData() {
        for (NoteData noteData : notesData) {
            collection.document(noteData.getId()).delete()/*.addOnSuccessListener()*/;
        }
        notesData = new ArrayList<>();
    }

    public Exception getException() {
        return exception;
    }
}
