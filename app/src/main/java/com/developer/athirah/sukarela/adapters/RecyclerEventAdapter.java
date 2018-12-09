package com.developer.athirah.sukarela.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.athirah.sukarela.EventActivity;
import com.developer.athirah.sukarela.R;
import com.developer.athirah.sukarela.models.ModelEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerEventAdapter extends RecyclerView.Adapter<RecyclerEventAdapter.VH> {

    // declare static component
    public static final RecyclerEventAdapter ADAPTER = new RecyclerEventAdapter();
    public static final List<ModelEvent> LIST = new ArrayList<>();

    // declare static keyword
    public static final String EXTRA_EVENT_UID = "event_uid";


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        // require component
        final Context context = vh.itemView.getContext();
        final ModelEvent event = LIST.get(i);

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
        return LIST.size();
    }


    class VH extends RecyclerView.ViewHolder {

        // declare view
        private View hidden;

        private TextView status;
        private TextView title;
        private TextView date;
        private TextView time;
        private TextView location;

        private ImageView image;


        private VH(@NonNull View itemView) {
            super(itemView);

            // init view
            hidden = itemView.findViewById(R.id.cardevent_buttons);
            hidden.setVisibility(View.GONE);

            status = itemView.findViewById(R.id.cardevent_status);
            title = itemView.findViewById(R.id.cardevent_title);
            date = itemView.findViewById(R.id.cardevent_date);
            time = itemView.findViewById(R.id.cardevent_time);
            location = itemView.findViewById(R.id.cardevent_location);

            image = itemView.findViewById(R.id.cardevent_image);
        }

        private void setEvent(final Context context, final String uid){

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventActivity.class);
                    intent.putExtra(EXTRA_EVENT_UID, uid);

                    context.startActivity(intent);
                }
            });
        }
    }
}
