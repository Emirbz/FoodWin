package com.esprit.foodwin.utility;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.esprit.foodwin.Entity.Event;
import com.esprit.foodwin.EventDetailsActivity;
import com.esprit.foodwin.R;

public class TodayEventsAdapter extends RecyclerView.Adapter<TodayEventsAdapter.eventsViewHolder> {

      List<Event> eventList;
      Context context;

      public TodayEventsAdapter() {

      }

      public TodayEventsAdapter(List<Event> eventList, Context context) {
            this.eventList = eventList;
            this.context = context;

      }

      @NonNull
      @Override
      public eventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.event_cell, viewGroup, false);


            return new eventsViewHolder(view);
      }

      @Override
      public void onBindViewHolder(@NonNull eventsViewHolder eventsViewHolder, final int i) {
            byte[] imageByteArray2 = Base64.decode(eventList.get(i).getImage(), Base64.DEFAULT);
            Glide.with(context)
                    .load(imageByteArray2)                     // Set image url
                    //     .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                    .into(eventsViewHolder.image);

            eventsViewHolder.name.setText(eventList.get(i).getName());
            eventsViewHolder.date.setText(eventList.get(i).getDate());
            eventsViewHolder.time.setText(eventList.get(i).getStarttime());
            eventsViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                        Intent intent= new Intent(context,EventDetailsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id",eventList.get(i).getId());
                        context.startActivity(intent);


                  }
            });
      }

      @Override
      public int getItemCount() {
            return eventList.size();
      }

      public static class eventsViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView name;
            TextView time;
            TextView date;
            RelativeLayout parentLayout;

            public eventsViewHolder(@NonNull View itemView) {
                  super(itemView);
                  image = itemView.findViewById(R.id.eventImage);
                  name = itemView.findViewById(R.id.eventName);
                  date = itemView.findViewById(R.id.eventDate);
                  time = itemView.findViewById(R.id.eventTime);
                  parentLayout = itemView.findViewById(R.id.eventParentLayout);
            }
      }
}
