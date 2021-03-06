package com.alium.quran_app_data.model;

import android.arch.persistence.room.Entity;
import android.os.Parcel;


/**
 * Created by abdulmujibaliu on 3/16/17.
 */

@Entity
public class Bookmark extends Chapter {

    int pageNumber;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.pageNumber);
    }

    public Bookmark() {
    }

    protected Bookmark(Parcel in) {
        super(in);
        this.pageNumber = in.readInt();
    }

    public static final Creator<Bookmark> CREATOR = new Creator<Bookmark>() {
        @Override
        public Bookmark createFromParcel(Parcel source) {
            return new Bookmark(source);
        }

        @Override
        public Bookmark[] newArray(int size) {
            return new Bookmark[size];
        }
    };
}
