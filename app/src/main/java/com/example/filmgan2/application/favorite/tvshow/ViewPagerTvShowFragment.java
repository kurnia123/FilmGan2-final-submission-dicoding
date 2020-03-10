package com.example.filmgan2.application.favorite.tvshow;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmgan2.ApiClient;
import com.example.filmgan2.BuildConfig;
import com.example.filmgan2.R;
import com.example.filmgan2.application.tvshow.DetailTVShow;
import com.example.filmgan2.application.tvshow.model.TvShowAdapter;
import com.example.filmgan2.application.tvshow.model.TvShowData;
import com.example.filmgan2.db.DatabaseContract;
import com.example.filmgan2.db.GetServiceFavorite;
import com.example.filmgan2.db.entity.FavoriteModuleTvShow;
import com.example.filmgan2.settings.localization.LocaleHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.filmgan2.db.DatabaseContract.CONTENT_URI_TV_SHOW;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerTvShowFragment extends Fragment {

    private final static String TAG = ViewPagerTvShowFragment.class.getSimpleName();
    private RecyclerView rvFavoriteTvShow;
    private ProgressBar progressBar;

    private List<FavoriteModuleTvShow> listSQL = new ArrayList<>();
    private ArrayList<TvShowData> mTvShowData = new ArrayList<>();

    public ViewPagerTvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        updateLanguage();
        super.onViewCreated(view, savedInstanceState);

        rvFavoriteTvShow = view.findViewById(R.id.rv_tvshow_favorite);
        progressBar = view.findViewById(R.id.fragment_viewpager_tvshow_progressbarr);

        getDataSQL();

        showLoading(true);
        if (listSQL == null) {
            showLoading(false);
        } else {
            showLoading(true);
            GetServiceFavorite apiclient = ApiClient.getFavoriteInstance().create(GetServiceFavorite.class);
            for (FavoriteModuleTvShow data : listSQL) {
                apiclient.getTvShow(data.getIdTv(), BuildConfig.API_KEY, Locale.getDefault().getLanguage().equals("in") ? "id" : Locale.getDefault().getLanguage())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TvShowData>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(TvShowData tvShowData) {
                                mTvShowData.add(tvShowData);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                showRecyclerView(mTvShowData);
                                showLoading(false);
                            }
                        });
            }
        }
    }

    private void getDataSQL() {
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(CONTENT_URI_TV_SHOW,null,null,null,null);
            while (cursor.moveToNext()) {
                FavoriteModuleTvShow tvShow = new FavoriteModuleTvShow();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns._ID));
                int idTv = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.ID_TV));
                String idImageTv = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.ID_IMAGE_TV));

                tvShow.setDid(id);
                tvShow.setIdTv(idTv);
                tvShow.setIdImageTv(idImageTv);
                listSQL.add(tvShow);
                //Log.d(TAG, "DATA CURSOR : " + idImageTv);
            }
        } catch (NullPointerException e){
            cursor = null;
        } finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    private void showRecyclerView(ArrayList<TvShowData> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvFavoriteTvShow.setLayoutManager(layoutManager);
        TvShowAdapter tvAdapter = new TvShowAdapter(getActivity(), list);
        rvFavoriteTvShow.setAdapter(tvAdapter);
        tvAdapter.notifyDataSetChanged();
        tvAdapter.setOnClickCallback(new TvShowAdapter.OnitemClickCallback() {
            @Override
            public void onItemClicked(TvShowData tvdata) {
                moveActicity(tvdata);
            }
        });
    }

    private void moveActicity(TvShowData tvShowData) {
        Intent intent = new Intent(getActivity(), DetailTVShow.class);
        intent.putExtra(DetailTVShow.EXTRA_NAME, tvShowData);
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
