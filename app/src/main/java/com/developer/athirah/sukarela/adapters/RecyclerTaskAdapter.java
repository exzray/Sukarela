package com.developer.athirah.sukarela.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.athirah.sukarela.R;
import com.developer.athirah.sukarela.fragments.EventTaskFragment;
import com.developer.athirah.sukarela.models.ModelEvent;
import com.developer.athirah.sukarela.models.ModelTask;
import com.developer.athirah.sukarela.models.ModelUser;
import com.developer.athirah.sukarela.utilities.UserHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.VH> {

    // declare static component
    public static final List<ModelTask> LIST = new ArrayList<>();
    public static final RecyclerTaskAdapter ADAPTER = new RecyclerTaskAdapter();

    // declare component
    private ModelEvent event;
    private EventTaskFragment fragment;
    private int available_count = 0;

    // firebase
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        ModelTask task = LIST.get(i);

        // basic data bind
        vh.title.setText(task.getTitle());
        vh.description.setText(task.getDescription());

        renderLimit(vh.limit, task);
        renderButton(vh.button, task);

        if (fragment != null) {

            if (!event.isTaskFull(task)){
                available_count += 1;
            }

            String str_task = available_count + " task available";
            fragment.task.setText(str_task);

            String str_people = event.getTotalJoin() + " people joined";
            fragment.people.setText(str_people);
        }
    }

    @Override
    public int getItemCount() {
        return LIST.size();
    }

    private void renderLimit(TextView view, ModelTask task) {

        String str_limit;

        if (event != null) {

            if (event.getTotalJoinTask(task) == task.getLimit()) str_limit = "PENUH";
            else str_limit = "Limit " + event.getTotalJoinTask(task) + "/" + task.getLimit();

            view.setText(str_limit);
        }
    }

    private void renderButton(final Button button, final ModelTask task) {

        if (event != null) {

            if (event.isJoinEvent() && !event.isTaskFull(task) && !event.getStatus().equals(ModelEvent.Status.Complete)) {

                button.setVisibility(View.VISIBLE);

                if (event.isJoinTask(task)) button.setEnabled(false);
                else button.setEnabled(true);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        firestore.runTransaction(new Transaction.Function<ModelEvent>() {

                            @Nullable
                            @Override
                            public ModelEvent apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                // reference want to write
                                DocumentReference reference = firestore.collection("events").document(event.getUid());
                                ModelUser user = UserHelper.getInstance().get();

                                // read first
                                ModelEvent data = transaction.get(reference).toObject(ModelEvent.class);

                                if (data != null) {
                                    List<String> list_join = data.getPeople();
                                    List<String> list_task = data.getTask().get(task.getUid());

                                    // change on task list can be make if user join this event
                                    if (user != null && list_join.contains(user.getUid())) {

                                        if (list_task == null) list_task = new ArrayList<>();

                                        // // remove user from another task
                                        for (String key : data.getTask().keySet()) {
                                            List<String> list = data.getTask().get(key);

                                            if (list != null) list.remove(user.getUid());
                                        }

                                        // put user id into task list
                                        list_task.add(user.getUid());

                                        // update task list
                                        data.getTask().put(task.getUid(), list_task);

                                        // upload new data
                                        transaction.set(reference, data);
                                    }
                                }

                                return data;
                            }
                        }).addOnCompleteListener(new OnCompleteListener<ModelEvent>() {

                            @Override
                            public void onComplete(@NonNull Task<ModelEvent> task) {
                                Toast.makeText(button.getContext(), "Berjaya menyertai!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            } else button.setVisibility(View.GONE);
        }
    }

    public void receiveEventUpdate(ModelEvent event) {
        this.event = event;

        dataChange();
    }

    public void dataChange(){
        notifyDataSetChanged();
        available_count = 0;
    }

    class VH extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView limit;

        private Button button;

        private VH(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.itemtask_title);
            description = itemView.findViewById(R.id.itemtask_description);
            limit = itemView.findViewById(R.id.itemtask_limit);

            button = itemView.findViewById(R.id.itemtask_button);
        }
    }

    public void addFragment(EventTaskFragment fragment) {
        this.fragment = fragment;
    }

    public void removeFragrament() {
        fragment = null;
    }
}
