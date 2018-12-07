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
import android.widget.TextView;

import com.developer.athirah.sukarela.R;
import com.developer.athirah.sukarela.adapters.FragmentEventAdapter;
import com.developer.athirah.sukarela.adapters.RecyclerTaskAdapter;
import com.developer.athirah.sukarela.models.ModelEvent;
import com.developer.athirah.sukarela.models.ModelTask;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;


public class EventTaskFragment extends Fragment implements EventListener<QuerySnapshot> {

    // declare component
    LinearLayoutManager manager;
    DividerItemDecoration decoration;

    // declare view
    private RecyclerView recycler;
    public TextView people, task;

    // firebase
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;


    public EventTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init component
        manager = new LinearLayoutManager(view.getContext());
        decoration = new DividerItemDecoration(view.getContext(), manager.getOrientation());

        // init view
        recycler = view.findViewById(R.id.eventtask_recycler);
        people = view.findViewById(R.id.eventtask_people);
        task = view.findViewById(R.id.eventtask_task);

        // get pass arg contain event uid
        if (getArguments() != null) {
            String uid = getArguments().getString(FragmentEventAdapter.EVENT_UID);

            listen(uid);
        }

        initFragment();
        RecyclerTaskAdapter.ADAPTER.addFragment(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (registration != null) registration.remove();

        RecyclerTaskAdapter.ADAPTER.removeFragrament();
    }

    private void initFragment() {
        // setup recycler
        recycler.setAdapter(RecyclerTaskAdapter.ADAPTER);
        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(decoration);
    }

    private void listen(String uid) {
        // start listen to task collection
        registration = firestore.collection("events").document(uid).collection("tasks").addSnapshotListener(this);
    }

    @Override
    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
        if (queryDocumentSnapshots != null) {

            // this is important, clear previous data after new update
            RecyclerTaskAdapter.LIST.clear();

            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                // convert snapshot into ModelEvent object
                ModelTask data = snapshot.toObject(ModelTask.class);

                if (data != null) {
                    data.setUid(snapshot.getId());

                    RecyclerTaskAdapter.LIST.add(data);
                }
            }

            RecyclerTaskAdapter.ADAPTER.dataChange();
        }
    }
}
