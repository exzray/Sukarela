package com.developer.athirah.sukarela.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.developer.athirah.sukarela.fragments.LoginSigninFragment;
import com.developer.athirah.sukarela.fragments.LoginSignupFragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentLoginAdapter extends FragmentStatePagerAdapter {

    // declare component
    private List<Fragment> fragments;
    private List<String> titles;

    private LoginSigninFragment signinFragment;
    private LoginSignupFragment signupFragment;


    public FragmentLoginAdapter(FragmentManager fm) {
        super(fm);

        // init component
        fragments = new ArrayList<>();
        titles = new ArrayList<>();

        signinFragment = new LoginSigninFragment();
        signupFragment = new LoginSignupFragment();

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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    private void initAdapter(){
        // setup fragments
        fragments.add(signinFragment);
        fragments.add(signupFragment);

        // setup titles
        titles.add("Log Masuk");
        titles.add("Daftar");
    }
}
