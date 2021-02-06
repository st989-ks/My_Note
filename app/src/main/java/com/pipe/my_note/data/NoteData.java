package com.pipe.my_note.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;

public class NoteData implements Parcelable {
    public static final Creator<NoteData> CREATOR = new Creator<NoteData>() {
        @Override
        public NoteData createFromParcel(Parcel in) {
            return new NoteData(in);
        }

        @Override
        public NoteData[] newArray(int size) {
            return new NoteData[size];
        }
    };
    private String title;
    private String tag;
    private int key;
    private long created;
    private int linkCard;
    private String text;
    private boolean like;

    public NoteData(String title, String tag, int key, long created,
                    int linkCard, String text, boolean like) {
        this.title = title;
        this.tag = tag;
        this.key = key;
        this.created = created;
        this.linkCard = linkCard;
        this.text = text;
        this.like = like;
    }

    protected NoteData(Parcel in) {
        title = in.readString();
        tag = in.readString();
        key = in.readInt();
        created = in.readLong();
        linkCard = in.readInt();
        text = in.readString();
        like = in.readByte() != 0;
    }

    public String getTitle() {
        return title;
    }

    public NoteData setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getKey() {
        return key;
    }

    public NoteData setKey(int key) {
        this.key = key;
        return this;
    }

    public long getCreated() {
        return created;
    }

    public NoteData setCreated(long created) {
        this.created = created;
        return this;
    }

    public int getLinkCard() {
        return linkCard;
    }

    public NoteData setLinkCard(int linkCard) {
        this.linkCard = linkCard;
        return this;
    }

    public String getText() {
        return text;
    }

    public NoteData setText(String text) {
        this.text = text;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean like() {
        return like;
    }

    public void like(boolean important) {
        like = important;
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
        dest.writeByte((byte) (like ? 1 : 0));
    }
}
