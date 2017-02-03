package com.epiagregator.impls.webapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by etien on 25/01/2017.
 */

public class Entry implements Parcelable {

    private String id;
    private String feedId;
    private String author;
    private String link;
    private String title;
    private long pubDate;
    private String description;
    private Boolean read;
    private Boolean favorite;

    public Entry() {
    }

    public Entry(String id, String feedId, String author, String link, String title, Date pubDate, String description) {
        this.id = id;
        this.feedId = feedId;
        this.author = author;
        this.link = link;
        this.title = title;
        this.pubDate = pubDate.getTime();
        this.description = description;
    }

    public Entry(String id, String feedId, String author, String link, String title, Date pubDate, String description, Boolean read, Boolean favorite) {
        this.id = id;
        this.feedId = feedId;
        this.author = author;
        this.link = link;
        this.title = title;
        this.pubDate = pubDate.getTime();
        this.description = description;
        this.read = read;
        this.favorite = favorite;
    }

    protected Entry(Parcel in) {
        id = in.readString();
        feedId = in.readString();
        author = in.readString();
        link = in.readString();
        title = in.readString();
        pubDate = in.readLong();
        description = in.readString();
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    public boolean getRead() {
        return read != null && read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean getFavorite() {
        return favorite != null && favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPubDate() {
        return new Date(pubDate);
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate.getTime();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(feedId);
        parcel.writeString(author);
        parcel.writeString(link);
        parcel.writeString(title);
        parcel.writeLong(pubDate);
        parcel.writeString(description);
    }
}
