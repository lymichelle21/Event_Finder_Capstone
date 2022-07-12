package com.capstone.event_finder.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.capstone.event_finder.R;
import com.capstone.event_finder.activities.EventDetailsActivity;
import com.capstone.event_finder.activities.PhotoDetailsActivity;
import com.capstone.event_finder.models.Event;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    Context context;
    List<Event> events;

    public EventsAdapter(Context context, List<Event> eventsList) {
        this.context = context;
        this.events = eventsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        try {
            holder.bind(event);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivEventPhoto;
        TextView tvEventTitle;
        TextView tvEventDescription;
        TextView tvStartDate;
        TextView tvEndDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEventPhoto = itemView.findViewById(R.id.ivEventPhoto);
            tvEventTitle = itemView.findViewById(R.id.tvEventTitle);
            tvEventDescription = itemView.findViewById(R.id.tvEventDescription);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            itemView.setOnClickListener(this);
        }

        public void bind(Event event) throws ParseException {
            tvEventTitle.setText(event.getName());
            tvEventDescription.setText(event.getDescription());
            tvStartDate.setText(event.getTimeStart());
            tvEndDate.setText(event.getTimeEnd());
            Glide.with(context).load(event.getImageUrl()).centerCrop().transform(new CenterCrop(), new RoundedCorners(30)).into(ivEventPhoto);
        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Event event = events.get(position);
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra(Event.class.getSimpleName(), Parcels.wrap(event));
                context.startActivity(intent);
            }
        }
    }

}
