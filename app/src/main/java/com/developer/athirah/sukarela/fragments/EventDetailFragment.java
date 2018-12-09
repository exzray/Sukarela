package com.developer.athirah.sukarela.fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.athirah.sukarela.LoginActivity;
import com.developer.athirah.sukarela.R;
import com.developer.athirah.sukarela.adapters.FragmentEventAdapter;
import com.developer.athirah.sukarela.adapters.RecyclerTaskAdapter;
import com.developer.athirah.sukarela.models.ModelEvent;
import com.developer.athirah.sukarela.models.ModelUser;
import com.developer.athirah.sukarela.utilities.NotificationReceiver;
import com.developer.athirah.sukarela.utilities.UserHelper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Transaction;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailFragment extends Fragment implements EventListener<DocumentSnapshot> {

    // declare view
    private ImageView image;
    private TextView title, location, date, time, description;
    private Button join, map;

    // firebase
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;


    public EventDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view
        image = view.findViewById(R.id.eventdetail_image);
        title = view.findViewById(R.id.eventdetail_title);
        location = view.findViewById(R.id.eventdetail_location);
        date = view.findViewById(R.id.eventdetail_date);
        time = view.findViewById(R.id.eventdetail_time);
        description = view.findViewById(R.id.eventdetail_description);
        join = view.findViewById(R.id.eventdetail_join);
        map = view.findViewById(R.id.eventdetail_map);

        // get pass arg contain event uid
        if (getArguments() != null) {
            final String uid = getArguments().getString(FragmentEventAdapter.EVENT_UID);

            listen(uid);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (registration != null) registration.remove();
    }

    @Override
    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

        if (documentSnapshot != null) {

            if (documentSnapshot.exists()) {
                final ModelEvent event = documentSnapshot.toObject(ModelEvent.class);

                if (event != null) {
                    event.setUid(documentSnapshot.getId());

                    updateFragment(event);
                    RecyclerTaskAdapter.ADAPTER.receiveEventUpdate(event);

                    if (event.isJoinEvent()) join.setVisibility(View.GONE);
                    else join.setVisibility(View.VISIBLE);


                    join.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ModelUser user = UserHelper.getInstance().get();

                            if (user == null){

                                if (getContext() != null){
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    getContext().startActivity(intent);
                                }
                            } else {
                                joinEvent(event.getUid());
                            }
                        }
                    });

                    // setup button
                    map.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String uri = "http://maps.google.com/maps?daddr=" + event.getPoint().getLatitude() + "," + event.getPoint().getLongitude();
                            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    public void updateFragment(ModelEvent event) {
        Glide
                .with(this)
                .load(event.getImage())
                .into(image);

        title.setText(event.getTitle());
        location.setText(event.getLocation());
        date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(event.getDate()));
        time.setText(DateFormat.getTimeInstance(DateFormat.MEDIUM).format(event.getDate()));
        description.setText(event.getDescription());
    }

    private void listen(String uid) {
        // start listen to event document
        registration = firestore.collection("events").document(uid).addSnapshotListener(this);
    }

    private void joinEvent(String uid){
        final ModelUser user = UserHelper.getInstance().get();
        final DocumentReference reference = firestore.collection("events").document(uid);

        firestore.runTransaction(new Transaction.Function<ModelEvent>() {

            @Nullable
            @Override
            public ModelEvent apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                // data snapshot
                DocumentSnapshot snapshot = transaction.get(reference);

                // convert to model object
                ModelEvent event = snapshot.toObject(ModelEvent.class);

                if (event != null){

                    List<String> list = event.getPeople();

                    if (user != null){
                        list.add(user.getUid());
                        transaction.set(reference, event);

                        if (getContext() != null ){
                            Calendar date1 = Calendar.getInstance();
                            date1.setTime(event.getDate());
                            date1.add(Calendar.DAY_OF_MONTH, -2);

                            Log.i("sakai", "apply: " + DateFormat.getDateInstance().format(date1.getTime()));

                            AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                            // a week
                            Intent intent = new Intent(getContext(), NotificationReceiver.class);
                            intent.putExtra(NotificationReceiver.CHANNEL, event.getUid() + "1"); // will be replace with event id
                            intent.putExtra(NotificationReceiver.TITLE, event.getTitle());
                            intent.putExtra(NotificationReceiver.CONTENT, "Aktiviti yang disertai pada minggu ini bermula!");
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
                            manager.setExact(AlarmManager.RTC_WAKEUP, date1.getTimeInMillis(), pendingIntent);
                        }
                    }
                }

                return event;
            }
        });
    }
}
