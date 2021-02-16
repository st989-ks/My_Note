package com.pipe.my_note.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;

public class NoteData implements Parcelable  {
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
    private String key;
    private String created;
    private String linkCard;
    private String text;
    private boolean like;

    public NoteData(String title, String tag, String key, String created,
                    String linkCard, String text, boolean like) {
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
        key = in.readString();
        created = in.readString();
        linkCard = in.readString();
        text = in.readString();
        like = in.readByte() != 0;
    }

    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }

    public String getKey() {
        return key;
    }

    public String getCreated() {
        return created;
    }

    public String getLinkCard() {
        return linkCard;
    }

    public String getText() {
        return text;
    }

    public boolean getLike() {
        return like;
    }

    public String getFormatDate() {
        long date = Long.valueOf(getCreated());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String stringDate = dateFormat.format(date);
        return stringDate;
    }

    public NoteData setTitle(String title) {
        this.title = title;
        return this;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public NoteData setKey(String key) {
        this.key = key;
        return this;
    }

    public NoteData setCreated(String created) {
        this.created = created;
        return this;
    }

    public NoteData setLinkCard(String linkCard) {
        this.linkCard = linkCard;
        return this;
    }

    public NoteData setText(String text) {
        this.text = text;
        return this;
    }

    public void setLike(boolean important) {
        like = important;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(tag);
        dest.writeString(key);
        dest.writeString(created);
        dest.writeString(linkCard);
        dest.writeString(text);
        dest.writeByte((byte) (like ? 1 : 0));
    }
}
