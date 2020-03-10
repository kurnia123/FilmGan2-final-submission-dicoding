package com.example.filmfavorite;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.filmfavorite.FavoriteFragment.FavoriteFilmFragment;
import com.example.filmfavorite.FavoriteFragment.FavoriteTvShowFragment;

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
                return new FavoriteFilmFragment();
            case 1:
                return new FavoriteTvShowFragment();
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
