package com.pipe.my_note.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

class NoteDataMapping {

    public static class Fields {
        public final static String NAME = "nameTitle";
        public final static String TAG = "tag";
        public final static String DATE = "created";
        public final static String LINC_CARD = "linkCard";
        public final static String TEXT = "text";
        public final static String LIKE = "like";
    }

    public static NoteData toCardData(String id, Map<String, Object> doc) {
        Timestamp timeStamp = (Timestamp) doc.get(Fields.DATE);
        NoteData answer = new NoteData(
                (String) doc.get(Fields.NAME),
                (String) doc.get(Fields.TAG),
                (String) doc.get(id),
                timeStamp.toDate(),
                (String) doc.get(Fields.LINC_CARD),
                (String) doc.get(Fields.TEXT),
                (boolean) doc.get(Fields.LIKE)
                );
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(NoteData noteData) {
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.NAME, noteData.getTitle());
        answer.put(Fields.TAG, noteData.getTitle());
        answer.put(Fields.DATE, noteData.getDate());
        answer.put(Fields.LINC_CARD, noteData.getTitle());
        answer.put(Fields.TEXT, noteData.getTitle());
        answer.put(Fields.LIKE, noteData.getLike());
        return answer;
    }
}
