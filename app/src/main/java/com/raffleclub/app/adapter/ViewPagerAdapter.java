package com.raffleclub.app.adapter;

import android.content.Context;

import com.google.android.material.tabs.TabLayout;
import com.raffleclub.app.ui.tickets.TicketsFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<TicketsFragment> mFragmentList ;
//    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
//    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();




    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        mFragmentList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
    }
    public void updateAdapter(ArrayList<TicketsFragment> fragment) {
        this.mFragmentList = fragment;
        notifyDataSetChanged();
//        mFragmentTitleList.add(title);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return mFragmentTitleList.get(position);
//    }

}
