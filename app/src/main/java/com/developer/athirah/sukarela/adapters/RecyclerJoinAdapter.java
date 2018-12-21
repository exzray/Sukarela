package com.developer.athirah.sukarela.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.athirah.sukarela.EventActivity;
import com.developer.athirah.sukarela.R;
import com.developer.athirah.sukarela.models.ModelEvent;
import com.developer.athirah.sukarela.models.ModelUser;
import com.developer.athirah.sukarela.utilities.UserHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerJoinAdapter extends RecyclerView.Adapter<RecyclerJoinAdapter.VH> {

    // declare static component
    public static final RecyclerJoinAdapter ADAPTER = new RecyclerJoinAdapter();
    public static final List<ModelEvent> ALL = new ArrayList<>();

    // declare component
    private List<ModelEvent> list = new ArrayList<>();
    private boolean filter = false;

    // firebase
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event, viewGroup, false);

        return new VH(root);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        // require component
        final Context context = vh.itemView.getContext();
        final ModelEvent event = list.get(i);

        // bind data into view
        Glide.with(context).load(event.getImage()).into(vh.image);

        vh.status.setText(event.maskStatus());
        vh.title.setText(event.getTitle());
        vh.location.setText(event.getLocation());
        vh.date.setText(DateFormat.getDateInstance(DateFormat.LONG).format(event.getDate()));
        vh.time.setText(DateFormat.getTimeInstance(DateFormat.MEDIUM).format(event.getDate()));

        vh.setEvent(context, event.getUid());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterOngoing() {
        filter = false;

        update();
    }

    public void filterFinish() {
        filter = true;

        update();
    }

    public void update() {
        list.clear();

        if (!filter) {
            for (ModelEvent event : ALL) {

                if (event.getStatus().equals(ModelEvent.Status.Ongoing) && event.isJoinEvent())
                    list.add(event);
            }
        } else {
            for (ModelEvent event : ALL) {

                if (event.getStatus().equals(ModelEvent.Status.Complete) && event.isJoinEvent())
                    list.add(event);
            }
        }

        ADAPTER.notifyDataSetChanged();
    }


    class VH extends RecyclerView.ViewHolder {

        // declare view
        private View hidden;
        private TextView status, title, date, time, location;
        private ImageView image;
        private Button detail, cancel;

        private VH(@NonNull View itemView) {
            super(itemView);

            // init view
            hidden = itemView.findViewById(R.id.cardevent_info);
            hidden.setVisibility(View.GONE);

            status = itemView.findViewById(R.id.cardevent_status);
            title = itemView.findViewById(R.id.cardevent_title);
            date = itemView.findViewById(R.id.cardevent_date);
            time = itemView.findViewById(R.id.cardevent_time);
            location = itemView.findViewById(R.id.cardevent_location);

            image = itemView.findViewById(R.id.cardevent_image);

            detail = itemView.findViewById(R.id.cardevent_detail);
            cancel = itemView.findViewById(R.id.cardevent_cancel);
        }

        private void setEvent(final Context context, final String uid) {

            detail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventActivity.class);
                    intent.putExtra(RecyclerEventAdapter.EXTRA_EVENT_UID, uid);

                    context.startActivity(intent);
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
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

                            if (event != null) {

                                List<String> list = event.getPeople();
                                Map<String, List<String>> map = event.getTask();

                                if (user != null) {
                                    list.remove(user.getUid());

                                    if (map != null) {

                                        for (String key : map.keySet()) {
                                            List<String> task_list = map.get(key);

                                            if (task_list != null) task_list.remove(user.getUid());
                                        }
                                    }

                                    transaction.set(reference, event);
                                }
                            }

                            return event;
                        }
                    }).addOnCompleteListener(new OnCompleteListener<ModelEvent>() {

                        @Override
                        public void onComplete(@NonNull Task<ModelEvent> task) {
                            filterOngoing();
                        }
                    });
                }
            });
        }
    }
}
