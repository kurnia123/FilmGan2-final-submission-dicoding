package com.example.filmgan2.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.filmgan2.R;
import com.example.filmgan2.db.DatabaseContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.filmgan2.db.DatabaseContract.CONTENT_URI_FILM;

public class StackRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = StackRemoteViewFactory.class.getSimpleName();
    private List<String> urlImage;

    private String urlPoster = "https://image.tmdb.org/t/p/w780/";
    private final Context mContext;

    StackRemoteViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        urlImage  = new ArrayList<>();
        final long identityToken = Binder.clearCallingIdentity();
        Cursor cursor = mContext.getContentResolver().query(CONTENT_URI_FILM,null,null,null,null);
        while (cursor.moveToNext()) {
            urlImage.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns.ID_IMAGE_FILM)));
        }
        cursor.close();
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        //Log.d("StackWidgetFavoriteFilm", "onDestroy");
    }

    @Override
    public int getCount() {
        return urlImage.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        //Log.d("StackWidgetFavoriteFilm", "getViewAt");
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        try {
            Bitmap b = Picasso.with(mContext).load(urlPoster + urlImage.get(position)).get();
            rv.setImageViewBitmap(R.id.imageView, b);

            Bundle extras = new Bundle();
            extras.putInt(StackWidgetFavoriteFilm.EXTRA_ITEM, position);
            Intent fillIntent = new Intent();
            fillIntent.putExtras(extras);

            rv.setOnClickFillInIntent(R.id.imageView, fillIntent);
            return rv;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
