package com.developer.athirah.sukarela.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.developer.athirah.sukarela.fragments.MainHomeFragment;
import com.developer.athirah.sukarela.fragments.MainJoinFragment;
import com.developer.athirah.sukarela.fragments.MainProfileFragment;
import com.developer.athirah.sukarela.models.ModelUser;
import com.developer.athirah.sukarela.utilities.UserHelper;

import java.util.ArrayList;
import java.util.List;

public class FragmentMainAdapter extends FragmentStatePagerAdapter {

    // declare component
    private List<Fragment> fragments;
    private MainHomeFragment homeFragment;
    private MainJoinFragment joinFragment;
    private MainProfileFragment profileFragment;


    public FragmentMainAdapter(FragmentManager fm) {
        super(fm);

        // init component
        fragments = new ArrayList<>();
        homeFragment = new MainHomeFragment();
        joinFragment = new MainJoinFragment();
        profileFragment = new MainProfileFragment();

        initAdapter();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    private void initAdapter(){
        // setup fragments
        fragments.add(homeFragment);

        ModelUser user = UserHelper.getInstance().get();

        if (user != null){
            fragments.add(joinFragment);
            fragments.add(profileFragment);
        }
    }
}
