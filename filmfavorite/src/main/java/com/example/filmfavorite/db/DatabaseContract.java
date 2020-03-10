package com.example.filmfavorite.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.filmgan2";
    private static final String SCHEME = "content";

    public static final Integer DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favoriteDatabase";

    // TABLE NAME FILM
    public final static String TABEL_FILM_FAVORITE = "film";
    public static final class FilmColumns implements BaseColumns{
        public static String ID_FILM = "id_film";
        public static String ID_IMAGE_FILM = "id_image_film";
    }

    // TABLE NAME TV SHOW
    public final static String TABLE_TV_SHOW_FAVORITE = "tvshow";
    public static final class TvShowColumns implements BaseColumns{
        public static String ID_TV = "id_tv";
        public static String ID_IMAGE_TV = "id_image_tv";
    }

    // URI FILM
    public static final Uri CONTENT_URI_FILM = new Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath(TABEL_FILM_FAVORITE)
        .build();

    // URI TV SHOW
    public static final Uri CONTENT_URI_TV_SHOW = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_TV_SHOW_FAVORITE)
            .build();
}
