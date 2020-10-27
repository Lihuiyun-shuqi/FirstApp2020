package com.swufe.firstapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class myViewPagerAdapter extends FragmentPagerAdapter {

    public myViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new viewpagerAFragment();
        }else if(position == 1){
            return  new viewpagerBFragment();
        }else{
            return new viewpagerCFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;//返回的是该适配器管理的fragment页面个数
    }

    /*
    @Override
    public CharSequence getPageTitle(int position){
        return "Title" + position;
    }*/
}
