package com.example.filmgan2.application.favorite;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.filmgan2.R;
import com.example.filmgan2.settings.localization.LocaleHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    public static final String TAG = FavoriteFragment.class.getSimpleName();

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateLanguage();
        getActivity().setTitle(R.string.favorite);
        final TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Film"));
        tabLayout.addTab(tabLayout.newTab().setText("TVShow"));

        final ViewPager viewPager = view.findViewById(R.id.viewpager);

        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), getActivity()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager);
        //Log.d(TAG, "TAG onViewCreate");

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
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
}
