package com.developer.athirah.sukarela.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.athirah.sukarela.R;
import com.developer.athirah.sukarela.adapters.RecyclerEventAdapter;
import com.developer.athirah.sukarela.models.ModelEvent;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainHomeFragment extends Fragment {

    // declare view
    private LinearLayoutManager layoutManager;
    private RecyclerView recycler;


    public MainHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view
        layoutManager = new LinearLayoutManager(view.getContext());
        recycler = view.findViewById(R.id.mainhome_recycler);

        initFragment();
    }

    private void initFragment() {
        // setup recycler
        recycler.setAdapter(RecyclerEventAdapter.ADAPTER);
        recycler.setLayoutManager(layoutManager);
    }
}
