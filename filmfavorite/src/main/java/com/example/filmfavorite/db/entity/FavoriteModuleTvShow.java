package com.example.filmfavorite.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteModuleTvShow implements Parcelable {
    private int did;
    private int idTv;
    private String idImageTv;

    public FavoriteModuleTvShow(){}

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public int getIdTv() {
        return idTv;
    }

    public void setIdTv(int idTv) {
        this.idTv = idTv;
    }

    public String getIdImageTv() {
        return idImageTv;
    }

    public void setIdImageTv(String idImageTv) {
        this.idImageTv = idImageTv;
    }

    protected FavoriteModuleTvShow(Parcel in) {
        did = in.readInt();
        idTv = in.readInt();
        idImageTv = in.readString();
    }

    public static final Creator<FavoriteModuleTvShow> CREATOR = new Creator<FavoriteModuleTvShow>() {
        @Override
        public FavoriteModuleTvShow createFromParcel(Parcel in) {
            return new FavoriteModuleTvShow(in);
        }

        @Override
        public FavoriteModuleTvShow[] newArray(int size) {
            return new FavoriteModuleTvShow[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(did);
        dest.writeInt(idTv);
        dest.writeString(idImageTv);
    }
}
