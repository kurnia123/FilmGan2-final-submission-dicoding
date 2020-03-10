package com.example.filmgan2.application.tvshow.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvShowData implements Parcelable {

    @SerializedName("original_name")
    private String judulTv;
    @SerializedName("vote_average")
    private double rating;
    @SerializedName("genre_ids")
    private ArrayList<Integer> genreTV;
    private String creator;
    @SerializedName("overview")
    private String description;
    @SerializedName("poster_path")
    private String imageTv;
    @SerializedName("first_air_date")
    private String dateFirstLiris;
    @SerializedName("backdrop_path")
    private String imageBackdrop;
    @SerializedName("id")
    private int idTvShow;

    public TvShowData(String judulTv, double rating, ArrayList<Integer> genreTV, String creator, String description, String imageTv, String dateFirstLiris, String imageBackdrop, int idTvShow) {
        this.judulTv = judulTv;
        this.rating = rating;
        this.genreTV = genreTV;
        this.creator = creator;
        this.description = description;
        this.imageTv = imageTv;
        this.dateFirstLiris = dateFirstLiris;
        this.imageBackdrop = imageBackdrop;
        this.idTvShow = idTvShow;
    }

    public int getIdTvShow() {
        return idTvShow;
    }

    public void setIdTvShow(int idTvShow) {
        this.idTvShow = idTvShow;
    }

    public String getImageBackdrop() {
        return imageBackdrop;
    }

    public void setImageBackdrop(String imageBackdrop) {
        this.imageBackdrop = imageBackdrop;
    }

    public String getDateFirstLiris() {
        return dateFirstLiris;
    }

    public void setDateFirstLiris(String dateFirstLiris) {
        this.dateFirstLiris = dateFirstLiris;
    }

    protected TvShowData(Parcel in) {
        judulTv = in.readString();
        rating = in.readDouble();
        this.genreTV = new ArrayList<>();
        in.readList(genreTV,Integer.class.getClassLoader());
        creator = in.readString();
        description = in.readString();
        imageTv = in.readString();
        dateFirstLiris = in.readString();
        imageBackdrop = in.readString();
        idTvShow = in.readInt();
    }

    public static final Creator<TvShowData> CREATOR = new Creator<TvShowData>() {
        @Override
        public TvShowData createFromParcel(Parcel in) {
            return new TvShowData(in);
        }

        @Override
        public TvShowData[] newArray(int size) {
            return new TvShowData[size];
        }
    };

    public String getJudulTv() {
        return judulTv;
    }

    public void setJudulTv(String judulTv) {
        this.judulTv = judulTv;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<Integer> getGenreTV() {
        return genreTV;
    }

    public void setGenreTV(ArrayList<Integer> genreTV) {
        this.genreTV = genreTV;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageTv() {
        return imageTv;
    }

    public void setImageTv(String imageTv) {
        this.imageTv = imageTv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(judulTv);
        dest.writeDouble(rating);
        dest.writeList(genreTV);
        dest.writeString(creator);
        dest.writeString(description);
        dest.writeString(imageTv);
        dest.writeString(dateFirstLiris);
        dest.writeString(imageBackdrop);
        dest.writeInt(idTvShow);
    }
}
