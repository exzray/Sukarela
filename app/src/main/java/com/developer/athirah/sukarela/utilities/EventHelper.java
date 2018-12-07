package com.developer.athirah.sukarela.utilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.developer.athirah.sukarela.models.ModelEvent;
import com.developer.athirah.sukarela.models.ModelUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventHelper {

    // declare static component
    private static EventHelper helper;

    // firebase
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    private EventHelper() {
        helper = this;
    }

    public static EventHelper getInstance() {
        if (helper == null) new EventHelper();

        return helper;
    }

    public void join(final String uid, final OnEventCompleted completed) {
        // get user
        final ModelUser user = UserHelper.getInstance().get();

        if (user != null) {
            firestore.runTransaction(new Transaction.Function<Void>() {

                @android.support.annotation.Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference reference = firestore.collection("events").document(uid);

                    // read
                    DocumentSnapshot snapshot = transaction.get(reference);

                    // convert snapshot into ModelEvent object
                    ModelEvent event = snapshot.toObject(ModelEvent.class);

                    if (event != null) {

                        // null mean no people join this event yet
                        if (event.getPeople() == null) event.setPeople(new ArrayList<String>());

                        // add user uid to this event
                        event.getPeople().add(user.getUid());

                        // upload latest data
                        transaction.set(reference, event);

                        completed.completed(event, "Berjaya menyertai " + event.getTitle().toLowerCase());
                    }

                    return null;
                }
            });
        }
    }

    public void unjoin(final String uid, final OnEventCompleted completed) {
        // get user
        final ModelUser user = UserHelper.getInstance().get();

        if (user != null) {
            firestore.runTransaction(new Transaction.Function<Void>() {

                @android.support.annotation.Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference reference = firestore.collection("events").document(uid);

                    // read
                    DocumentSnapshot snapshot = transaction.get(reference);

                    // convert snapshot into ModelEvent object
                    ModelEvent event = snapshot.toObject(ModelEvent.class);

                    if (event != null) {

                        // null mean no people join this event yet
                        if (event.getPeople() == null) event.setPeople(new ArrayList<String>());

                        // add user uid to this event
                        event.getPeople().remove(user.getUid());

                        // upload latest data
                        transaction.set(reference, event);

                        completed.completed(event, "Berjaya keluar " + event.getTitle().toLowerCase());
                    }

                    return null;
                }
            });
        }
    }

    public boolean isJoin(ModelEvent event) {
        List<String> list = event.getPeople();
        ModelUser user = UserHelper.getInstance().get();

        Log.i("mydebug", "isJoin: " + user.getUid());

        if (list == null) return false;

        return list.contains(user.getUid());
    }

    public interface OnEventCompleted {

        void completed(ModelEvent event, String message);
    }
}
