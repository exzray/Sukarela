package com.developer.athirah.sukarela.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.developer.athirah.sukarela.fragments.EventDetailFragment;
import com.developer.athirah.sukarela.fragments.EventTaskFragment;
import com.developer.athirah.sukarela.models.ModelEvent;

import java.util.ArrayList;
import java.util.List;

public class FragmentEventAdapter extends FragmentStatePagerAdapter {

    // declare static keyword
    public static final String EVENT_UID = "event_uid";

    // declare component
    private List<Fragment> list;
    private List<String> titles;

    private EventDetailFragment detailFragment;
    private EventTaskFragment taskFragment;


    public FragmentEventAdapter(FragmentManager fm, String uid) {
        super(fm);

        // init component
        list = new ArrayList<>();
        titles = new ArrayList<>();

        // pass uid in args
        Bundle bundle = new Bundle();
        bundle.putString(EVENT_UID, uid);

        detailFragment = new EventDetailFragment();
        detailFragment.setArguments(bundle);

        taskFragment = new EventTaskFragment();
        taskFragment.setArguments(bundle);

        initAdapter();
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    private void initAdapter() {
        // setup list
        list.add(detailFragment);
        list.add(taskFragment);

        // setup titles
        titles.add("detail");
        titles.add("task");
    }
}
