package com.example.filmgan2.application.favorite.film;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
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

import com.example.filmgan2.ApiClient;
import com.example.filmgan2.BuildConfig;
import com.example.filmgan2.R;
import com.example.filmgan2.application.film.DetailFilm;
import com.example.filmgan2.application.film.model.FilmAdapter;
import com.example.filmgan2.application.film.model.filmData;
import com.example.filmgan2.db.DatabaseContract;
import com.example.filmgan2.db.GetServiceFavorite;
import com.example.filmgan2.db.entity.FavoriteModuleFilm;
import com.example.filmgan2.settings.localization.LocaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.filmgan2.db.DatabaseContract.CONTENT_URI_FILM;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFilmFragment extends Fragment {

    private static final String TAG = ViewPagerFilmFragment.class.getSimpleName();
    private RecyclerView rvFavoriteFilm;
    private ProgressBar progressBar;
    private TextView textNotif;

    private List<FavoriteModuleFilm> listSQL = new ArrayList<>();
    private ArrayList<filmData> mFilmData = new ArrayList<>();

    public ViewPagerFilmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, "TAG onCreatedView");

        return inflater.inflate(R.layout.fragment_view_pager_film, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        updateLanguage();
        super.onViewCreated(view, savedInstanceState);

        textNotif = view.findViewById(R.id.text_notif_data_film);
        rvFavoriteFilm = view.findViewById(R.id.rv_film_favorite);
        progressBar = view.findViewById(R.id.fragment_viewpager_film_progressbarr);

        getDataSQL();

        showLoading(true);
        if (listSQL == null) {
            textNotif.setVisibility(View.VISIBLE);
        } else {
            textNotif.setVisibility(View.GONE);
            GetServiceFavorite apiClient = ApiClient.getFavoriteInstance().create(GetServiceFavorite.class);
            for (FavoriteModuleFilm data : listSQL) {
                //Log.d(TAG, "TAG Observarble data : " + data.getIdImageFavorite());
                apiClient.getMovie(data.getIdFavorite(), BuildConfig.API_KEY, Locale.getDefault().getLanguage().equals("in") ? "id" : Locale.getDefault().getLanguage())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<filmData>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(filmData filmData) {
                                //Log.d(TAG, "TAG onNext : " + filmData.getJudulfilm());
                                mFilmData.add(filmData);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                //Log.d(TAG, "TAG onComplete : ");
                                showRecyclerView(mFilmData);
                                showLoading(false);
                            }
                        });
            }
        }
        //Log.d(TAG, "TAG ViewPagerFilmFragment onViewCreate");
    }

    private void getDataSQL() {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(CONTENT_URI_FILM, null, null, null, null);
            while (cursor.moveToNext()) {
                FavoriteModuleFilm film = new FavoriteModuleFilm();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns._ID));
                int idFilm = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns.ID_FILM));
                String itemImageId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FilmColumns.ID_IMAGE_FILM));

                film.setIdFavorite(idFilm);
                film.setIdImageFavorite(itemImageId);
                film.setId(id);
                listSQL.add(film);
                //Log.d(TAG, "DATA CURSOR : " + itemImageId);
            }
        } catch (NullPointerException e){
            cursor = null;
        } finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
    }

    private void updateLanguage() {
        Locale locale = new Locale(LocaleHelper.getLanguage(getActivity()));
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );
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
        Intent intent = new Intent(getActivity(), DetailFilm.class);
        intent.putExtra(DetailFilm.EXTRA_NAME, filmData);
        startActivity(intent);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}