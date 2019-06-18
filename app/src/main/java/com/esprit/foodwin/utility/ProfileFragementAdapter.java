package com.esprit.foodwin.utility;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragementAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragementNameList = new ArrayList<>();


    public ProfileFragementAdapter(FragmentManager fm) {
        super(fm);



    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragementNameList.size();

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragementNameList.get(position);
    }
    public void AddFragment (Fragment fragment,String name)
    {
        fragmentList.add(fragment);
        fragementNameList.add(name);
    }


}
