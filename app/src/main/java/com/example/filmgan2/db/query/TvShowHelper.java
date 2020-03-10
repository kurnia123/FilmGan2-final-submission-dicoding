package com.example.filmgan2.db.query;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.filmgan2.db.DatabaseHelper;

import static android.provider.BaseColumns._ID;
import static com.example.filmgan2.db.DatabaseContract.TABLE_TV_SHOW_FAVORITE;
import static com.example.filmgan2.db.DatabaseContract.TvShowColumns.ID_IMAGE_TV;
import static com.example.filmgan2.db.DatabaseContract.TvShowColumns.ID_TV;

public class TvShowHelper {
    private static final String DATABASE_TABLE = TABLE_TV_SHOW_FAVORITE;
    private static DatabaseHelper databaseHelper;
    private static TvShowHelper INSTANCE;

    private static SQLiteDatabase database;

    private TvShowHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public static TvShowHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new TvShowHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open(){
        database = databaseHelper.getWritableDatabase();
    }

    public void read(){
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
        return database.delete(DATABASE_TABLE, ID_TV + " = ?",new String[]{id});
    }

}
