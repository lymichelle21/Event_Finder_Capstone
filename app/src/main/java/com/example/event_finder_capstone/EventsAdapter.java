package com.example.event_finder_capstone;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.event_finder_capstone.models.Event;

import java.util.Date;
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
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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

        public void bind(Event event) {
            tvEventTitle.setText(event.getName());
            tvEventDescription.setText(event.getDescription());
            tvStartDate.setText(convertEventDateFormat());
            tvEndDate.setText(convertEventDateFormat());
            Glide.with(context).load(event.getImageUrl()).centerCrop().into(ivEventPhoto);
        }

        private String convertEventDateFormat() {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
            return formatter.format(date);
        }

        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                context.startActivity(intent);
            }
        }
    }

}
