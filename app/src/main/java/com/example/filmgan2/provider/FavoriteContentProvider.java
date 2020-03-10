package com.example.filmgan2.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.filmgan2.db.query.FilmHelper;
import com.example.filmgan2.db.query.TvShowHelper;

import static com.example.filmgan2.db.DatabaseContract.AUTHORITY;
import static com.example.filmgan2.db.DatabaseContract.CONTENT_URI_FILM;
import static com.example.filmgan2.db.DatabaseContract.CONTENT_URI_TV_SHOW;
import static com.example.filmgan2.db.DatabaseContract.TABEL_FILM_FAVORITE;
import static com.example.filmgan2.db.DatabaseContract.TABLE_TV_SHOW_FAVORITE;


public class FavoriteContentProvider extends ContentProvider {

    private FilmHelper filmHelper;
    private TvShowHelper tvShowHelper;

    private static final int FILM = 1;
    private static final int FILM_ID = 2;
    private static final int TVSHOW = 3;
    private static final int TVSHOW_ID = 4;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // FILM URI MATCHER\
        sUriMatcher.addURI(AUTHORITY, TABEL_FILM_FAVORITE, FILM);
        sUriMatcher.addURI(AUTHORITY, TABEL_FILM_FAVORITE + "/#", FILM_ID);

        // TV SHOW MATCHER
        sUriMatcher.addURI(AUTHORITY, TABLE_TV_SHOW_FAVORITE, TVSHOW);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV_SHOW_FAVORITE + "/#", TVSHOW_ID);
    }

    public FavoriteContentProvider() {
    }

    @Override
    public boolean onCreate() {
        filmHelper = FilmHelper.getInstance(getContext());
        tvShowHelper = TvShowHelper.getInstance(getContext());

        filmHelper.open();
        tvShowHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case FILM:
                //Log.d("FavoriteContentProvider","QUERY FAVORITE");
                cursor = filmHelper.queryAll();
                break;
            case FILM_ID:
                cursor = filmHelper.queryById(uri.getLastPathSegment());
                break;
            case TVSHOW:
                cursor = tvShowHelper.queryAll();
                break;
            case TVSHOW_ID:
                cursor = tvShowHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)) {
            case FILM:
                //Log.d("FavoriteContentProvider","INSERT FAVORITE");
                added = filmHelper.insert(values);
                getContext().getContentResolver().notifyChange(CONTENT_URI_FILM, null);
//                return Uri.parse(CONTENT_URI_FILM + "/" + added);
                break;
            case TVSHOW:
                added = tvShowHelper.insert(values);
                getContext().getContentResolver().notifyChange(CONTENT_URI_TV_SHOW, null);
//                return Uri.parse(CONTENT_URI_TV_SHOW + "/" + added);
                break;
            default:
                added = 0;
                break;
        }
        return Uri.parse(CONTENT_URI_FILM + "/" + added);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delete;
        switch (sUriMatcher.match(uri)) {
            case FILM_ID:
                delete = filmHelper.delteById(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_URI_FILM, null);
                break;
            case TVSHOW_ID:
                delete = tvShowHelper.delteById(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_URI_TV_SHOW, null);
                break;
            default:
                delete = 0;
                break;
        }
        return delete;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
