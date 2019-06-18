package com.esprit.foodwin.utility;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.esprit.foodwin.Entity.Event;
import com.esprit.foodwin.EventDetailsActivity;

public class AllEventsAdapter extends RecyclerView.Adapter<AllEventsAdapter.eventsViewHolder> {

    List<Event> eventList;
    Context context;

    public AllEventsAdapter() {

    }

    public AllEventsAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;

    }

    @NonNull
    @Override
    public eventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(com.esprit.foodwin.R.layout.all_event_cell, viewGroup, false);


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
        String dateString = eventList.get(i).getDate();
        Date date = null;
        try {
            date = new SimpleDateFormat("MM/dd/yyyy", Locale.UK).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayOfTheWeek = eventList.get(i).getDate();


        Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date current = c.getTime();
        String currentday = (String) DateFormat.format("dd", current);
        String currentmonth = (String) DateFormat.format("MM", current);
        String currentyear = (String) DateFormat.format("yyyy", current);
        Date date2 = null;
        try {
            date2 = new SimpleDateFormat("MM/dd/yyyy", Locale.UK).parse(eventList.get(i).getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String day = (String) DateFormat.format("dd", date2);
        String month = (String) DateFormat.format("MM", date2);
        String year = (String) DateFormat.format("yyyy", date2);

        if (Integer.valueOf(year).equals(Integer.valueOf(currentyear)) && Integer.valueOf(month).equals(Integer.valueOf(currentmonth))) {
            if (Integer.valueOf(day) - Integer.valueOf(currentday) <= 3) {
                switch ((String) DateFormat.format("EEEE", date2)) {
                    case "lundi": {
                        dayOfTheWeek = "Monday";
                        break;
                    }

                    case "mardi": {
                        dayOfTheWeek = "Tuesday";
                        break;
                    }


                    case "mercredi": {
                        dayOfTheWeek = "Wednesday";
                        break;
                    }

                    case "jeudi": {
                        dayOfTheWeek = "Thursday";
                        break;
                    }

                    case "vendredi": {
                        dayOfTheWeek = "Friday";
                        break;
                    }

                    case "samedi": {
                        dayOfTheWeek = "Saturday";
                        break;
                    }

                    case "dimanche": {
                        dayOfTheWeek = "Sunday";
                        break;
                    }

                    default:
                        dayOfTheWeek = eventList.get(i).getDate();
                }
            }
        }
        if (!Integer.valueOf(year).equals(Integer.valueOf(currentyear))) {
            dayOfTheWeek = eventList.get(i).getDate();
        }
        if (Integer.valueOf(year).equals(Integer.valueOf(currentyear))) {
            if (!Integer.valueOf(month).equals(Integer.valueOf(currentmonth))) {
                dayOfTheWeek = eventList.get(i).getDate();
            }

        }


        if (Integer.valueOf(year).equals(Integer.valueOf(currentyear)) && Integer.valueOf(month).equals(Integer.valueOf(currentmonth)) && Integer.valueOf(day).equals(Integer.valueOf(currentday))) {
            dayOfTheWeek = "Today";
        }


        eventsViewHolder.date.setText(dayOfTheWeek);
        eventsViewHolder.desc.setText("Description: " + eventList.get(i).getDescription());
        eventsViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", eventList.get(i).getId());
                context.startActivity(intent);


            }
        });
    }

    public void filter(List<Event> list) {
        eventList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class eventsViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView desc;
        TextView date;
        CardView parentLayout;

        public eventsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(com.esprit.foodwin.R.id.image);
            name = itemView.findViewById(com.esprit.foodwin.R.id.name);
            date = itemView.findViewById(com.esprit.foodwin.R.id.date);
            desc = itemView.findViewById(com.esprit.foodwin.R.id.desc);
            parentLayout = itemView.findViewById(com.esprit.foodwin.R.id.parentLayout);
        }
    }
}
