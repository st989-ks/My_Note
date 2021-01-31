package com.pipe.my_note;

import android.os.Parcel;
import android.os.Parcelable;

public class Content implements Parcelable {
    private String nameCard;
    private String created;
    private String tags;
    private int key;
    private String linkCard;
    private String textNote;

    public Content(String name, String created, String tags, int key, String linkCard, String textNote) {
        this.nameCard = name;
        this.created = created;
        this.tags = tags;
        this.key = key;
        this.linkCard = linkCard;
        this.textNote = textNote;
    }

    protected Content(Parcel in) {
        nameCard = in.readString();
        created = in.readString();
        tags = in.readString();
        key = in.readInt();
        linkCard = in.readString();
        textNote = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameCard);
        dest.writeString(created);
        dest.writeString(tags);
        dest.writeInt(key);
        dest.writeString(linkCard);
        dest.writeString(textNote);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    public String getNameCard() {
        return nameCard;
    }

    public Content setNameCard(String nameCard) {
        this.nameCard = nameCard;
        return this;
    }

    public String getCreated() {
        return created;
    }

    public Content setCreated(String created) {
        this.created = created;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public Content setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public int getKey() {
        return key;
    }

    public Content setKey(int key) {
        this.key = key;
        return this;
    }

    public String getLinkCard() {
        return linkCard;
    }

    public Content setLinkCard(String linkCard) {
        this.linkCard = linkCard;
        return this;
    }

    public String getTextNote() {
        return textNote;
    }

    public Content setTextNote(String textNote) {
        this.textNote = textNote;
        return this;
    }
}
