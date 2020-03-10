package com.example.filmgan2.application.favorite;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.filmgan2.R;
import com.example.filmgan2.application.favorite.film.ViewPagerFilmFragment;
import com.example.filmgan2.application.favorite.tvshow.ViewPagerTvShowFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    Context context;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, Context context) {
        super(fm);
        this.context = context;
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ViewPagerFilmFragment();
            case 1:
                return new ViewPagerTvShowFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                return context.getString(R.string.movie);
            case 1:
                return context.getString(R.string.tv_show);
//                title = getItem(position).getClass().getName();
//                data = title.subSequence(title.lastIndexOf(".") + 1, title.length());
        }
        return title;
    }


}
