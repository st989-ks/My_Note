package com.pipe.my_note.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class NoteData implements Parcelable  {

    private String title;
    private String tag;
    private String id;
    private Date date;
    private String linkCard;
    private String text;
    private boolean like;

    public NoteData(String title, String tag, Date data,
                    String linkCard, String text, boolean like) {
        this.title = title;
        this.tag = tag;
        this.date = data;
        this.linkCard = linkCard;
        this.text = text;
        this.like = like;
    }

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

    protected NoteData(Parcel in) {
        title = in.readString();
        tag = in.readString();
        date = new Date(in.readLong());
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

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLinkCard(String linkCard) {
        this.linkCard = linkCard;
    }

    public void setText(String text) {
        this.text = text;
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
        dest.writeLong(date.getTime());
        dest.writeString(linkCard);
        dest.writeString(text);
        dest.writeByte((byte) (like ? 1 : 0));
    }
}
