package com.esprit.foodwin.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.esprit.foodwin.Entity.Event;
import com.esprit.foodwin.EventDetailsActivity;
import com.esprit.foodwin.R;

public class EventEstabCalendar extends AppCompatDialogFragment {

    CompactCalendarView calendarView;
    List<Event> events;
    String name;
    TextView month;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_event_estab_calendar, null);
        calendarView = view.findViewById(R.id.compactcalendar_view);
        calendarView.setLocale(TimeZone.getDefault(), Locale.ENGLISH);
        calendarView.setUseThreeLetterAbbreviation(true);
        month = view.findViewById(R.id.month);
        Calendar cal = Calendar.getInstance();
        Date datee = cal.getTime();
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM-yyyy", Locale.ENGLISH);
        month.setText(dateFormatMonth.format(datee));
        SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        for (Event e : events) {
            try {
                //   Date date = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse(e.getDate());


                Date date1 = df2.parse(e.getDate());
                String date2 = sdf.format(date1);
                //System.out.println();
                Date mDate = sdf.parse(date2);
                long timeInMilliseconds = mDate.getTime();
                com.github.sundeepk.compactcalendarview.domain.Event event = new com.github.sundeepk.compactcalendarview.domain.Event(Color.RED, timeInMilliseconds, e.getName());
                calendarView.addEvent(event);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                for (int i = 0; i < events.size(); i++) {


                    try {
                        Date date1 = df2.parse(events.get(i).getDate());

                        if (dateClicked.getTime() == date1.getTime()) {
                            Snackbar snackbar = Snackbar.make(view, events.get(i).getName(), Snackbar.LENGTH_INDEFINITE);
                            int finalI = i;
                            snackbar.setAction("Check it out", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                                    intent.putExtra("id", events.get(finalI).getId());
                                    startActivity(intent);
                                }
                            });
                            snackbar.show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
        builder.setView(view)
                .setTitle(name + "'s events")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public void populate(List<Event> list, String name) {
        this.events = list;
        this.name = name;
    }
}
