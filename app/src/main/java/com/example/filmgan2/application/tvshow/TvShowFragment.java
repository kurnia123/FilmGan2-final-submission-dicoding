package com.example.filmgan2.application.tvshow;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.example.filmgan2.application.tvshow.model.TvShowAdapter;
import com.example.filmgan2.application.tvshow.model.TvShowData;
import com.example.filmgan2.application.tvshow.model.TvShowModel;
import com.example.filmgan2.application.tvshow.search.SearchTvShowActivity;
import com.example.filmgan2.settings.localization.LocaleHelper;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {

    private SearchView searchView;
    private ProgressBar progressBar;
    private RecyclerView tvRecyclerview;
    private static final String SEARCH_KEY = "search_key_tv";
    private String searchViewSaveQueryTV;

    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvRecyclerview = view.findViewById(R.id.rv_tvshow);
        tvRecyclerview.setHasFixedSize(true);
        updateLanguage();

        progressBar = view.findViewById(R.id.fragment_tv_progressbarr);
        getActivity().setTitle(R.string.tv_show);

        TvShowModel tvShowModel = new ViewModelProvider(requireActivity()).get(TvShowModel.class);
        tvShowModel.setTvShow(Locale.getDefault().getLanguage());
        showLoading(true);

        tvShowModel.getTvShow().observe(getActivity(), new Observer<ArrayList<TvShowData>>() {
            @Override
            public void onChanged(ArrayList<TvShowData> tvShowData) {
                if (tvShowData != null) {
                    showRecycler(tvShowData);
                    showLoading(false);
                }
            }
        });

        if (savedInstanceState != null) {
            searchViewSaveQueryTV = savedInstanceState.getString(SEARCH_KEY);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.searchview_appbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);

        if (searchViewSaveQueryTV != null) {
            item.expandActionView();
            searchView.setQuery(searchViewSaveQueryTV, true);
            searchView.clearFocus();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getActivity(), SearchTvShowActivity.class);
                intent.putExtra(SearchTvShowActivity.KEY_NAME, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        searchViewSaveQueryTV = searchView.getQuery().toString();
        outState.putString(SEARCH_KEY, searchViewSaveQueryTV);
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

    private void showRecycler(ArrayList<TvShowData> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        tvRecyclerview.setLayoutManager(layoutManager);
        TvShowAdapter tvShowAdapter = new TvShowAdapter(getActivity(), list);
        tvRecyclerview.setAdapter(tvShowAdapter);

        tvShowAdapter.setOnClickCallback(new TvShowAdapter.OnitemClickCallback() {
            @Override
            public void onItemClicked(TvShowData tvdata) {
                moveActicity(tvdata);
            }
        });
    }

    private void moveActicity(TvShowData tvData) {
        Intent intent = new Intent(getActivity(), DetailTVShow.class);
        intent.putExtra(DetailTVShow.EXTRA_NAME, tvData);
        startActivity(intent);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
