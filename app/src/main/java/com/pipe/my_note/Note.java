package com.pipe.my_note;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class Note implements Parcelable {
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
    private String title;
    private String tag;
    private int key;
    private long created;
    private int linkCard;
    private String text;

    public Note(String title, String tag, int key, long created, int linkCard, String text) {
        this.title = title;
        this.tag = tag;
        this.key = key;
        this.created = created;
        this.linkCard = linkCard;
        this.text = text;
    }

    protected Note(Parcel in) {
        title = in.readString();
        tag = in.readString();
        key = in.readInt();
        created = in.readLong();
        linkCard = in.readInt();
        text = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public Note setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getKey() {
        return key;
    }

    public Note setKey(int key) {
        this.key = key;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public Note setCreated(long created) {
        this.created = created;
        return this;
    }

    public int getLinkCard() {
        return linkCard;
    }

    public Note setLinkCard(int linkCard) {
        this.linkCard = linkCard;
        return this;
    }

    public String getText() {
        return text;
    }

    public Note setText(String text) {
        this.text = text;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFormatDate() {
        long date = getCreated();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String stringDate = dateFormat.format(date);
        return stringDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(tag);
        dest.writeInt(key);
        dest.writeLong(created);
        dest.writeInt(linkCard);
        dest.writeString(text);
    }
}
