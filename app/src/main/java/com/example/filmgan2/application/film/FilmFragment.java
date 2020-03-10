package com.example.filmgan2.application.film;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmgan2.R;
import com.example.filmgan2.application.film.model.FilmAdapter;
import com.example.filmgan2.application.film.model.FilmModel;
import com.example.filmgan2.application.film.model.filmData;
import com.example.filmgan2.application.film.search.SearchFilmActivity;
import com.example.filmgan2.settings.localization.LocaleHelper;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilmFragment extends Fragment {

    private ProgressBar progressBar;
    private static final String FRAGMENT_NAME = FilmFragment.class.getSimpleName();
    private static final String SEARCH_KEY = "search_key";
    private static final String TAG = FRAGMENT_NAME;
    private RecyclerView rvFilm;
    private SearchView searchView;
    private String searchViewSaveQueryFilm;

    public FilmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_film, container, false);
        //Log.d(TAG, LocaleHelper.getLanguage(getActivity()) + " onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateLanguage();
        rvFilm = view.findViewById(R.id.rv_film);
        rvFilm.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.fragment_film_progressbarr);

        getActivity().setTitle(R.string.movie);
        //Log.d(TAG, "onViewCreated");

        FilmModel filmModel = new ViewModelProvider(requireActivity()).get(FilmModel.class);
        filmModel.setFilm(Locale.getDefault().getLanguage());
        showLoading(true);

        filmModel.getFilm().observe(requireActivity(), new Observer<ArrayList<filmData>>() {
            @Override
            public void onChanged(ArrayList<filmData> filmData) {
                if (filmData != null) {
                    showRecyclerView(filmData);
                    showLoading(false);
                }
            }
        });

        if (savedInstanceState != null) {
            searchViewSaveQueryFilm = savedInstanceState.getString(SEARCH_KEY);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        setHasOptionsMenu(true);
        menu.clear();
        inflater.inflate(R.menu.searchview_appbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);

        if (searchViewSaveQueryFilm != null) {
            item.expandActionView();
            searchView.setQuery(searchViewSaveQueryFilm, true);
            searchView.clearFocus();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getActivity(), SearchFilmActivity.class);
                intent.putExtra(SearchFilmActivity.KEY_NAME, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        searchViewSaveQueryFilm = searchView.getQuery().toString();
        outState.putString(SEARCH_KEY, searchViewSaveQueryFilm);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.d(TAG, "TAG onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.d(TAG, "TAG onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.d(TAG, "TAG onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.d(TAG, "TAG onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.d(TAG, "TAG onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.d(TAG, "TAG onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d(TAG, "TAG onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Log.d(TAG, "TAG onDetach");
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
        rvFilm.setLayoutManager(layoutManager);
        FilmAdapter filmAdapter = new FilmAdapter(getActivity(), list);
        Log.d("TAGJJ","MainActivity RV AWAL");
        rvFilm.setAdapter(filmAdapter);
        filmAdapter.notifyDataSetChanged();
        filmAdapter.setOnClickCallback(new FilmAdapter.OnitemClickCallback() {
            @Override
            public void onItemClicked(filmData filmdata) {
                Log.d("TAGJJ","MainActivity RV");
                moveActicity(filmdata);
            }
        });
        Log.d("TAGJJ","MainActivity RV AKHIR");
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
