package com.capstone.click2drink;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.capstone.click2drink.utility.Common;

public class CategoryAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;


    public CategoryAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                DispenserGallonFragment dispenserGallonFragment = new DispenserGallonFragment();
                return dispenserGallonFragment;
            case 1:
                SquareGallonFragment squareGallonFragment = new SquareGallonFragment();
                return squareGallonFragment;
            case 2:
                BottledWaterFragment bottledWaterFragment = new BottledWaterFragment();
                return bottledWaterFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
