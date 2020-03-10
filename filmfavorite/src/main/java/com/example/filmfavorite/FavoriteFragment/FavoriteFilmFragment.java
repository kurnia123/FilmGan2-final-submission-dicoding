package com.example.filmfavorite.FavoriteFragment;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmfavorite.ApiClient;
import com.example.filmfavorite.BuildConfig;
import com.example.filmfavorite.FavoriteFragment.film.FilmAdapter;
import com.example.filmfavorite.R;
import com.example.filmfavorite.db.DatabaseContract;
import com.example.filmfavorite.db.GetServiceFavorite;
import com.example.filmfavorite.db.entity.FavoriteModuleFilm;
import com.example.filmfavorite.db.filmData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.filmfavorite.db.DatabaseContract.CONTENT_URI_FILM;
import static com.example.filmfavorite.db.DatabaseContract.CONTENT_URI_TV_SHOW;

public class FavoriteFilmFragment extends Fragment implements LoadCallbackFilm {

    private static final String TAG = FavoriteFilmFragment.class.getSimpleName();
    private RecyclerView rvFavoriteFilm;
    private ProgressBar progressBar;
    private TextView textNotif;

    private ArrayList<FavoriteModuleFilm> listSQL = new ArrayList<>();
    private ArrayList<filmData> mFilmData = new ArrayList<>();
    private static final String EXTRA_STATE = "EXTRA_STATE";

     public FavoriteFilmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,CONTENT_URI_FILM.toString());
        Log.d(TAG,CONTENT_URI_TV_SHOW.toString());
        return inflater.inflate(R.layout.fragment_favorite_film, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textNotif = view.findViewById(R.id.text_notif_data_film);
        rvFavoriteFilm = view.findViewById(R.id.rv_film_favorite);
        progressBar = view.findViewById(R.id.fragment_viewpager_film_progressbarr);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, getContext());
        getContext().getContentResolver().registerContentObserver(CONTENT_URI_FILM, true, myObserver);

        if (savedInstanceState == null){
            new LoadAsync(getContext(),this).execute();
        } else {
            listSQL = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            processGetDataListRecyclerAdapter();
        }
        Log.d(TAG, "TAG ViewPagerFilmFragment onViewCreate");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE,listSQL);
    }

    private void getDataSQL(Cursor cursor) {
//        Cursor cursor = getActivity().getContentResolver().query(CONTENT_URI_FILM,null,null,null,null);
        Log.d(TAG,CONTENT_URI_FILM.toString());
        if (cursor != null){
            while (cursor.moveToNext()) {
                FavoriteModuleFilm film = new FavoriteModuleFilm();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns._ID));
                int idFilm = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns.ID_FILM));
                String itemImageId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns.ID_IMAGE_FILM));

                film.setIdFavorite(idFilm);
                film.setIdImageFavorite(itemImageId);
                film.setId(id);
                listSQL.add(film);
                Log.d(TAG, "DATA CURSOR : " + itemImageId);
            }
            cursor.close();
        }
    }

    private void showRecyclerView(ArrayList<filmData> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvFavoriteFilm.setLayoutManager(layoutManager);
        FilmAdapter filmAdapter = new FilmAdapter(getActivity(), list);
        rvFavoriteFilm.setAdapter(filmAdapter);
        filmAdapter.notifyDataSetChanged();
        filmAdapter.setOnClickCallback(new FilmAdapter.OnitemClickCallback() {
            @Override
            public void onItemClicked(filmData filmdata) {
                moveActicity(filmdata);
            }
        });
    }

    private void moveActicity(filmData filmData) {
//        Intent intent = new Intent(getActivity(), DetailFilm.class);
//        intent.putExtra(DetailFilm.EXTRA_NAME, filmData);
//        startActivity(intent);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void processGetDataListRecyclerAdapter(){
        GetServiceFavorite apiClient = ApiClient.getFavoriteInstance().create(GetServiceFavorite.class);
        for (FavoriteModuleFilm data : listSQL) {
            Log.d(TAG, "TAG Observarble data : " + data.getIdImageFavorite());
            apiClient.getMovie(data.getIdFavorite(), BuildConfig.API_KEY, Locale.getDefault().getLanguage().equals("in") ? "id" : Locale.getDefault().getLanguage())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<filmData>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(filmData filmData) {
                            Log.d(TAG, "TAG onNext : " + filmData.getJudulfilm());
                            mFilmData.add(filmData);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            Log.d(TAG, "TAG onComplete : ");
                            showRecyclerView(mFilmData);
                            showLoading(false);
                        }
                    });
        }
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor cursor) {
        getDataSQL(cursor);
        processGetDataListRecyclerAdapter();
    }

    private static class LoadAsync extends AsyncTask<Void,Void,Cursor>{
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallbackFilm> weakCallback;

        private LoadAsync(Context context, LoadCallbackFilm callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Cursor cursor = weakContext.get().getContentResolver().query(CONTENT_URI_FILM,null,null,null,null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadAsync(context, (LoadCallbackFilm) context).execute();
        }
    }
}

interface LoadCallbackFilm{
    void preExecute();
    void postExecute(Cursor cursor);
}
