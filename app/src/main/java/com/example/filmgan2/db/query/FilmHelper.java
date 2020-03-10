package com.example.filmgan2.db.query;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.filmgan2.db.DatabaseHelper;

import static android.provider.BaseColumns._ID;
import static com.example.filmgan2.db.DatabaseContract.FilmColumns.ID_FILM;
import static com.example.filmgan2.db.DatabaseContract.TABEL_FILM_FAVORITE;
import static com.example.filmgan2.db.DatabaseContract.FilmColumns.ID_IMAGE_FILM;

public class FilmHelper {
    private static final String DATABASE_TABLE = TABEL_FILM_FAVORITE;
    private static DatabaseHelper databaseHelper;

    private static FilmHelper INSTANCE;

    private static SQLiteDatabase database;

    private FilmHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public static FilmHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new FilmHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void read() throws SQLException{
        database = databaseHelper.getReadableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll(){
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    public Cursor queryById(String id) {
        return database.query(
                DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public long insert(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }

    public int update(String id, ContentValues values){
        return database.update(DATABASE_TABLE,values,_ID + " = ?",new String[]{id});
    }

    public int delteById(String id){
        return database.delete(DATABASE_TABLE, ID_FILM + " = ?",new String[]{id});
    }
}
