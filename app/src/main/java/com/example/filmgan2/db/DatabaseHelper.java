package com.example.filmgan2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.filmgan2.db.DatabaseContract.DATABASE_NAME;
import static com.example.filmgan2.db.DatabaseContract.DATABASE_VERSION;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    private static final String SQL_CREATE_TABLE_FILM = String.format("CREATE TABLE %s"
            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL)",
            DatabaseContract.TABEL_FILM_FAVORITE,
            DatabaseContract.FilmColumns._ID,
            DatabaseContract.FilmColumns.ID_FILM,
            DatabaseContract.FilmColumns.ID_IMAGE_FILM
    );

    private static final String SQL_CREATE_TABLE_TV = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_TV_SHOW_FAVORITE,
            DatabaseContract.TvShowColumns._ID,
            DatabaseContract.TvShowColumns.ID_TV,
            DatabaseContract.TvShowColumns.ID_IMAGE_TV
    );

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FILM);
        db.execSQL(SQL_CREATE_TABLE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABEL_FILM_FAVORITE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_TV_SHOW_FAVORITE);
        onCreate(db);
    }
}
