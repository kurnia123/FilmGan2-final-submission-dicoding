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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmfavorite.ApiClient;
import com.example.filmfavorite.BuildConfig;
import com.example.filmfavorite.FavoriteFragment.tv.TvShowAdapter;
import com.example.filmfavorite.R;
import com.example.filmfavorite.db.DatabaseContract;
import com.example.filmfavorite.db.GetServiceFavorite;
import com.example.filmfavorite.db.TvShowData;
import com.example.filmfavorite.db.entity.FavoriteModuleTvShow;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.filmfavorite.db.DatabaseContract.CONTENT_URI_TV_SHOW;

public class FavoriteTvShowFragment extends Fragment implements LoadCallbackTvShow {

    private final static String TAG = FavoriteTvShowFragment.class.getSimpleName();
    private RecyclerView rvFavoriteTvShow;
    private ProgressBar progressBar;

    private ArrayList<FavoriteModuleTvShow> listSQL = new ArrayList<>();
    private ArrayList<TvShowData> mTvShowData = new ArrayList<>();

    private static final String EXTRA_STATE = "EXTRA_STATE";

    public FavoriteTvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, getContext());
        getContext().getContentResolver().registerContentObserver(CONTENT_URI_TV_SHOW, true, myObserver);
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvFavoriteTvShow = view.findViewById(R.id.rv_tvshow_favorite);
        progressBar = view.findViewById(R.id.fragment_viewpager_tvshow_progressbarr);

        if (savedInstanceState == null) {
            new FavoriteTvShowFragment.LoadAsync(getContext(), this).execute();
        } else {
            listSQL = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            processGetDataListRecyclerAdapter();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, listSQL);
    }

    private void getDataSQL(Cursor cursor) {
        if (cursor != null) {
            while (cursor.moveToNext()) {
                FavoriteModuleTvShow tvShow = new FavoriteModuleTvShow();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns._ID));
                int idTv = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.ID_TV));
                String idImageTv = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowColumns.ID_IMAGE_TV));

                tvShow.setDid(id);
                tvShow.setIdTv(idTv);
                tvShow.setIdImageTv(idImageTv);
                listSQL.add(tvShow);
                Log.d(TAG, "DATA CURSOR : " + idImageTv);
            }
            cursor.close();
        }
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

    private void processGetDataListRecyclerAdapter() {
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

    private void moveActicity(TvShowData tvShowData) {
//        Intent intent = new Intent(getActivity(), DetailTVShow.class);
//        intent.putExtra(DetailTVShow.EXTRA_NAME, tvShowData);
//        startActivity(intent);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
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

    private static class LoadAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallbackTvShow> weakCallback;

        private LoadAsync(Context context, LoadCallbackTvShow callback) {
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
            Cursor cursor = weakContext.get().getContentResolver().query(CONTENT_URI_TV_SHOW, null, null, null, null);
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
            new LoadAsync(context, (LoadCallbackTvShow) context).execute();
        }
    }

}

interface LoadCallbackTvShow {
    void preExecute();

    void postExecute(Cursor cursor);
}
