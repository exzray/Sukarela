package com.developer.athirah.sukarela.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developer.athirah.sukarela.R;
import com.developer.athirah.sukarela.adapters.RecyclerJoinAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainJoinFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    // declare component
    private LinearLayoutManager manager;

    // declare view
    private TabLayout.Tab finish;
    private TabLayout.Tab ongoing;
    private TabLayout tab;
    private RecyclerView recycler;


    public MainJoinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_join, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init component
        manager = new LinearLayoutManager(view.getContext());

        // init view
        tab = view.findViewById(R.id.mainjoin_tab);
        ongoing = tab.newTab();
        finish = tab.newTab();
        recycler = view.findViewById(R.id.mainjoin_recycler);

        initFragment();
    }

    private void initFragment(){
        // setup tab
        ongoing.setText("akan datang");
        finish.setText("selesai");

        tab.addTab(ongoing);
        tab.addTab(finish);
        tab.addOnTabSelectedListener(this);

        // setup recycler
        recycler.setAdapter(RecyclerJoinAdapter.ADAPTER);
        recycler.setLayoutManager(manager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        if (tab.equals(ongoing)) RecyclerJoinAdapter.ADAPTER.filterOngoing();
        else RecyclerJoinAdapter.ADAPTER.filterFinish();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
