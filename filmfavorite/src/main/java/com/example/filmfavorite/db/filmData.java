package com.example.filmfavorite.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class filmData implements Parcelable {

    @SerializedName("original_title")
    private String judulfilm;
    @SerializedName("vote_average")
    private double rating;
    @SerializedName("release_date")
    private String durationFilm;
    @SerializedName("genre_ids")
    private ArrayList<Integer> genreFilm;
    @SerializedName("overview")
    private String description;
    @SerializedName("poster_path")
    private String imageFilm;
    @SerializedName("backdrop_path")
    private String imageBackdop;
    @SerializedName("id")
    private int idFilm;

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public String getImageBackdop() {
        return imageBackdop;
    }

    public void setImageBackdop(String imageBackdop) {
        this.imageBackdop = imageBackdop;
    }

    public String getJudulfilm() {
        return judulfilm;
    }

    public void setJudulfilm(String judulfilm) {
        this.judulfilm = judulfilm;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDurationFilm() {
        return durationFilm;
    }

    public void setDurationFilm(String durationFilm) {
        this.durationFilm = durationFilm;
    }

    public ArrayList<Integer> getGenreFilm() {
        return genreFilm;
    }

    public void setGenreFilm(ArrayList<Integer> genreFilm) {
        this.genreFilm = genreFilm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFilm() {
        return imageFilm;
    }

    public void setImageFilm(String imageFilm) {
        this.imageFilm = imageFilm;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.judulfilm);
        dest.writeDouble(this.rating);
        dest.writeString(this.durationFilm);
        dest.writeList(this.genreFilm);
        dest.writeString(this.description);
        dest.writeString(this.imageFilm);
        dest.writeString(this.imageBackdop);
        dest.writeInt(this.idFilm);
    }

    public filmData(){}

    public filmData(String judulfilm, Double rating, String durationFilm, ArrayList<Integer> genreFilm, String description, String imageFilm, String imageBackdrop){
        this.judulfilm = judulfilm;
        this.rating = rating;
        this.durationFilm = durationFilm;
        this.genreFilm = genreFilm;
        this.description = description;
        this.imageFilm = imageFilm;
        this.imageBackdop = imageBackdrop;
    }

    protected filmData(Parcel in){
        this.judulfilm = in.readString();
        this.rating = in.readDouble();
        this.durationFilm = in.readString();
        this.genreFilm = new ArrayList<>();
        in.readList(genreFilm,Integer.class.getClassLoader());
        this.description = in.readString();
        this.imageFilm = in.readString();
        this.imageBackdop = in.readString();
        this.idFilm = in.readInt();
    }

    public static final Creator<filmData> CREATOR = new Creator<filmData>() {
        @Override
        public filmData createFromParcel(Parcel source) {
            return new filmData(source);
        }

        @Override
        public filmData[] newArray(int size) {
            return new filmData[0];
        }
    };
}
