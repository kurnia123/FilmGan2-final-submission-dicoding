package com.example.filmfavorite.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteModuleFilm implements Parcelable {
    private int id;
    private int idFavorite;
    private String idImageFavorite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFavorite() {
        return idFavorite;
    }

    public FavoriteModuleFilm(){}

    public void setIdFavorite(int idFavorite) {
        this.idFavorite = idFavorite;
    }

    public String getIdImageFavorite() {
        return idImageFavorite;
    }

    public void setIdImageFavorite(String idImageFavorite) {
        this.idImageFavorite = idImageFavorite;
    }

    protected FavoriteModuleFilm(Parcel in) {
        id = in.readInt();
        idFavorite = in.readInt();
        idImageFavorite = in.readString();
    }

    public static final Creator<FavoriteModuleFilm> CREATOR = new Creator<FavoriteModuleFilm>() {
        @Override
        public FavoriteModuleFilm createFromParcel(Parcel in) {
            return new FavoriteModuleFilm(in);
        }

        @Override
        public FavoriteModuleFilm[] newArray(int size) {
            return new FavoriteModuleFilm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idFavorite);
        dest.writeString(idImageFavorite);
    }
}
