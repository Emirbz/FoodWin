package com.esprit.foodwin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.esprit.foodwin.Entity.Event;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.AllEventsAdapter;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;

public class AllEventsFragment extends Fragment {

    RecyclerView recyclerView;
    List<Event> eventList;
    AVLoadingIndicatorView progressBar2;
    AppCompatAutoCompleteTextView search;
    AppCompatSeekBar seekBar;
    TextView mon;
    TextView tue;
    TextView wed;
    TextView thu;
    TextView fri;
    TextView sat;
    TextView sun;
    private Context mContext;
    ImageView sadImg;
    TextView sadText;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public AllEventsFragment() {
        // Required empty public constructor
    }

    public static AllEventsFragment newInstance() {

        return new AllEventsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);
        eventList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.allEventsRecyclerView);
        progressBar2 = view.findViewById(R.id.allEventsProgress2);
        search = view.findViewById(R.id.searchView);
        seekBar = view.findViewById(R.id.seekBar);
        sadImg = view.findViewById(R.id.sadImg);
        sadText = view.findViewById(R.id.sadText);
        mon = view.findViewById(R.id.monday);
        tue = view.findViewById(R.id.tuesday);
        wed = view.findViewById(R.id.wednesday);
        thu = view.findViewById(R.id.thursday);
        fri = view.findViewById(R.id.friday);
        sat = view.findViewById(R.id.saturday);
        sun = view.findViewById(R.id.sunday);
        seekBar.setProgress(0);
        loadeventsList();
        return view;
    }


    private void loadeventsList() {
        progressBar2.show();
        String JSON_URL = IP.ip+"events";
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after completion and showing list view
                        // Showing json data in log monitor
                        try {
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = response.getJSONArray("events");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                Event item = new Event();
                                item.setId(jsonObject.getString("id"));
                                item.setImage(jsonObject.getString("image"));
                                item.setName(jsonObject.getString("name"));
                                item.setStarttime(jsonObject.getString("starttime"));
                                item.setEndtime(jsonObject.getString("endtime"));
                                item.setFee(jsonObject.getString("fee"));
                                item.setAlchool(jsonObject.getString("alchool"));
                                item.setDate(jsonObject.getString("date"));
                                item.setDescription(jsonObject.getString("description"));
                                //adding the json data to list
                                Calendar c = new GregorianCalendar();
                                c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
                                c.set(Calendar.MINUTE, 0);
                                c.set(Calendar.SECOND, 0);
                                Date current = c.getTime();
                                String currentday = (String) DateFormat.format("dd", current);
                                String currentmonth = (String) DateFormat.format("MM", current);
                                String currentyear = (String) DateFormat.format("yyyy", current);
                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("MM/dd/yyyy", Locale.UK).parse(jsonObject.getString("date"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String day = (String) DateFormat.format("dd", date);
                                String month = (String) DateFormat.format("MM", date);
                                String year = (String) DateFormat.format("yyyy", date);

                                if (Integer.valueOf(year) > Integer.valueOf(currentyear)) {
                                    eventList.add(item);
                                } else if (Integer.valueOf(year).equals(Integer.valueOf(currentyear))) {
                                    if (Integer.valueOf(month) > Integer.valueOf(currentmonth)) {
                                        eventList.add(item);
                                    } else if (Integer.valueOf(month).equals(Integer.valueOf(currentmonth))) {
                                        if (Integer.valueOf(day) >= Integer.valueOf(currentday)) {
                                            eventList.add(item);
                                        }
                                    }
                                }
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            //creating custom adapter object
                            final AllEventsAdapter mAdapter = new AllEventsAdapter(eventList, mContext);
                            //adding the adapter to list view
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            progressBar2.hide();
                            if(eventList.size()==0){
                                progressBar2.hide();
                                sadImg.setImageResource(R.drawable.ic_sad);

                                sadText.setText("No Events found");
                                sadText.setAlpha(1);
                            }


                            search.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    List<Event> list = new ArrayList<>();

                                    for (Event e : eventList) {
                                        if (e.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                                            list.add(e);
                                        }
                                    }
                                    mAdapter.filter(list);
                                    if(list.size()==0){
                                        progressBar2.hide();
                                        sadImg.setImageResource(R.drawable.ic_sad);

                                        sadText.setText("No Events found");
                                        sadText.setAlpha(1);
                                    }
                                }
                            });

                            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                    Calendar c = new GregorianCalendar();
                                    c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
                                    c.set(Calendar.MINUTE, 0);
                                    c.set(Calendar.SECOND, 0);
                                    Date current = c.getTime();
                                    String day1 = (String) DateFormat.format("dd", current);
                                    if (progress == 0) {
                                        System.out.println("0a");
                                        mAdapter.filter(eventList);
                                        System.out.println("0b");
                                        if(eventList.size()==0){
                                            progressBar2.hide();
                                            sadImg.setImageResource(R.drawable.ic_sad);

                                            sadText.setText("No Events found");
                                            sadText.setAlpha(1);
                                        }
                                    }
                                    if (progress == 1) {
                                        List<Event> list = new ArrayList<>();
                                        for (Event e : eventList) {
                                            Date date;
                                            try {
                                                date = new SimpleDateFormat("MM/dd/yyyy", Locale.UK).parse(e.getDate());
                                                String day2 = (String) DateFormat.format("dd", date);
                                                int diff = Integer.valueOf(day1) - Integer.valueOf(day2);


                                                if (diff == 0) {
                                                    list.add(e);

                                                }
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                        }
                                        mAdapter.filter(list);
                                        if(list.size()==0){
                                            progressBar2.hide();
                                            sadImg.setImageResource(R.drawable.ic_sad);

                                            sadText.setText("No Events found");
                                            sadText.setAlpha(1);
                                        }

                                    }
                                    if (progress == 2) {
                                        List<Event> list = new ArrayList<>();
                                        for (Event e : eventList) {
                                            Date date;
                                            try {
                                                date = new SimpleDateFormat("MM/dd/yyyy", Locale.UK).parse(e.getDate());
                                                String day2 = (String) DateFormat.format("dd", date);
                                                int diff = Math.abs(Integer.valueOf(day1) - Integer.valueOf(day2));
                                                if (diff < 3) {
                                                    list.add(e);

                                                }
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }

                                        }
                                        mAdapter.filter(list);
                                        if(list.size()==0){
                                            progressBar2.hide();
                                            sadImg.setImageResource(R.drawable.ic_sad);

                                            sadText.setText("No Events found");
                                            sadText.setAlpha(1);
                                        }
                                    }


                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });

                            mon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mon.setBackgroundResource(R.drawable.circle_textview);
                                    sun.setBackgroundResource(R.drawable.circle_grey_textview);
                                    tue.setBackgroundResource(R.drawable.circle_grey_textview);
                                    wed.setBackgroundResource(R.drawable.circle_grey_textview);
                                    thu.setBackgroundResource(R.drawable.circle_grey_textview);
                                    fri.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sat.setBackgroundResource(R.drawable.circle_grey_textview);
                                    List<Event> list = null;
                                    try {
                                        list = filterByDate("lundi");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mAdapter.filter(list);
                                    if(list.size()==0){
                                        progressBar2.hide();
                                        sadImg.setImageResource(R.drawable.ic_sad);

                                        sadText.setText("No Events found");
                                        sadText.setAlpha(1);
                                    }
                                    mon.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mAdapter.filter(eventList);
                                            if(eventList.size()==0){
                                                progressBar2.hide();
                                                sadImg.setImageResource(R.drawable.ic_sad);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(1);
                                            }else {
                                                progressBar2.hide();
                                                sadImg.setImageDrawable(null);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(0);

                                            }
                                            mon.setBackgroundResource(R.drawable.circle_grey_textview);
                                        }
                                    });
                                }
                            });


                            tue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    tue.setBackgroundResource(R.drawable.circle_textview);
                                    mon.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sun.setBackgroundResource(R.drawable.circle_grey_textview);
                                    wed.setBackgroundResource(R.drawable.circle_grey_textview);
                                    thu.setBackgroundResource(R.drawable.circle_grey_textview);
                                    fri.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sat.setBackgroundResource(R.drawable.circle_grey_textview);
                                    List<Event> list = null;
                                    try {
                                        list = filterByDate("mardi");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mAdapter.filter(list);
                                    if(list.size()==0){
                                        progressBar2.hide();
                                        sadImg.setImageResource(R.drawable.ic_sad);

                                        sadText.setText("No Events found");
                                        sadText.setAlpha(1);
                                    }
                                    tue.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mAdapter.filter(eventList);
                                            if(eventList.size()==0){
                                                progressBar2.hide();
                                                sadImg.setImageResource(R.drawable.ic_sad);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(1);
                                            }else {
                                                progressBar2.hide();
                                                sadImg.setImageDrawable(null);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(0);

                                            }
                                            tue.setBackgroundResource(R.drawable.circle_grey_textview);
                                        }
                                    });
                                }
                            });

                            wed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    wed.setBackgroundResource(R.drawable.circle_textview);
                                    mon.setBackgroundResource(R.drawable.circle_grey_textview);
                                    tue.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sun.setBackgroundResource(R.drawable.circle_grey_textview);
                                    thu.setBackgroundResource(R.drawable.circle_grey_textview);
                                    fri.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sat.setBackgroundResource(R.drawable.circle_grey_textview);
                                    List<Event> list = null;
                                    try {
                                        list = filterByDate("mercredi");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mAdapter.filter(list);
                                    if(list.size()==0){
                                        progressBar2.hide();
                                        sadImg.setImageResource(R.drawable.ic_sad);

                                        sadText.setText("No Events found");
                                        sadText.setAlpha(1);
                                    }
                                    wed.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mAdapter.filter(eventList);
                                            if(eventList.size()==0){
                                                progressBar2.hide();
                                                sadImg.setImageResource(R.drawable.ic_sad);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(1);
                                            }else {
                                                progressBar2.hide();
                                                sadImg.setImageDrawable(null);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(0);

                                            }
                                            wed.setBackgroundResource(R.drawable.circle_grey_textview);
                                        }
                                    });
                                }
                            });
                            thu.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    thu.setBackgroundResource(R.drawable.circle_textview);
                                    mon.setBackgroundResource(R.drawable.circle_grey_textview);
                                    tue.setBackgroundResource(R.drawable.circle_grey_textview);
                                    wed.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sun.setBackgroundResource(R.drawable.circle_grey_textview);
                                    fri.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sat.setBackgroundResource(R.drawable.circle_grey_textview);
                                    List<Event> list = null;
                                    try {
                                        list = filterByDate("jeudi");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mAdapter.filter(list);
                                    if(list.size()==0){
                                        progressBar2.hide();
                                        sadImg.setImageResource(R.drawable.ic_sad);

                                        sadText.setText("No Events found");
                                        sadText.setAlpha(1);
                                    }
                                    thu.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mAdapter.filter(eventList);
                                            if(eventList.size()==0){
                                                progressBar2.hide();
                                                sadImg.setImageResource(R.drawable.ic_sad);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(1);
                                            }else {
                                                progressBar2.hide();
                                                sadImg.setImageDrawable(null);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(0);

                                            }
                                            thu.setBackgroundResource(R.drawable.circle_grey_textview);
                                        }
                                    });
                                }
                            });
                            fri.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fri.setBackgroundResource(R.drawable.circle_textview);
                                    mon.setBackgroundResource(R.drawable.circle_grey_textview);
                                    tue.setBackgroundResource(R.drawable.circle_grey_textview);
                                    wed.setBackgroundResource(R.drawable.circle_grey_textview);
                                    thu.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sun.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sat.setBackgroundResource(R.drawable.circle_grey_textview);
                                    List<Event> list = null;
                                    try {
                                        list = filterByDate("vendredi");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mAdapter.filter(list);
                                    if(list.size()==0){
                                        progressBar2.hide();
                                        sadImg.setImageResource(R.drawable.ic_sad);

                                        sadText.setText("No Events found");
                                        sadText.setAlpha(1);
                                    }
                                    fri.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mAdapter.filter(eventList);
                                            if(eventList.size()==0){
                                                progressBar2.hide();
                                                sadImg.setImageResource(R.drawable.ic_sad);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(1);
                                            }else {
                                                progressBar2.hide();
                                                sadImg.setImageDrawable(null);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(0);

                                            }
                                            fri.setBackgroundResource(R.drawable.circle_grey_textview);
                                        }
                                    });
                                }
                            });
                            sat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sat.setBackgroundResource(R.drawable.circle_textview);
                                    mon.setBackgroundResource(R.drawable.circle_grey_textview);
                                    tue.setBackgroundResource(R.drawable.circle_grey_textview);
                                    wed.setBackgroundResource(R.drawable.circle_grey_textview);
                                    thu.setBackgroundResource(R.drawable.circle_grey_textview);
                                    fri.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sun.setBackgroundResource(R.drawable.circle_grey_textview);
                                    List<Event> list = null;
                                    try {
                                        list = filterByDate("samedi");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mAdapter.filter(list);
                                    if(list.size()==0){
                                        progressBar2.hide();
                                        sadImg.setImageResource(R.drawable.ic_sad);

                                        sadText.setText("No Events found");
                                        sadText.setAlpha(1);
                                    }
                                    sat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mAdapter.filter(eventList);
                                            if(eventList.size()==0){
                                                progressBar2.hide();
                                                sadImg.setImageResource(R.drawable.ic_sad);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(1);
                                            }else {
                                                progressBar2.hide();
                                                sadImg.setImageDrawable(null);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(0);

                                            }
                                            sat.setBackgroundResource(R.drawable.circle_grey_textview);
                                        }
                                    });
                                }
                            });
                            sun.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sun.setBackgroundResource(R.drawable.circle_textview);
                                    mon.setBackgroundResource(R.drawable.circle_grey_textview);
                                    tue.setBackgroundResource(R.drawable.circle_grey_textview);
                                    wed.setBackgroundResource(R.drawable.circle_grey_textview);
                                    thu.setBackgroundResource(R.drawable.circle_grey_textview);
                                    fri.setBackgroundResource(R.drawable.circle_grey_textview);
                                    sat.setBackgroundResource(R.drawable.circle_grey_textview);
                                    List<Event> list = null;
                                    try {
                                        list = filterByDate("dimanche");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    mAdapter.filter(list);
                                    if(list.size()==0){
                                        progressBar2.hide();
                                        sadImg.setImageResource(R.drawable.ic_sad);

                                        sadText.setText("No Events found");
                                        sadText.setAlpha(1);
                                    }
                                    sun.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mAdapter.filter(eventList);
                                            if(eventList.size()==0){
                                                progressBar2.hide();
                                                sadImg.setImageResource(R.drawable.ic_sad);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(1);
                                            }else {
                                                progressBar2.hide();
                                                sadImg.setImageDrawable(null);

                                                sadText.setText("No Events found");
                                                sadText.setAlpha(0);

                                            }
                                            sun.setBackgroundResource(R.drawable.circle_grey_textview);
                                        }
                                    });
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000,3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    List<Event> filterByDate(String day) throws ParseException {
        List<Event> list = new ArrayList<>();
        for (Event e : eventList) {
            Date date = new SimpleDateFormat("MM/dd/yyyy", Locale.UK).parse(e.getDate());
            String s = (String) DateFormat.format("EEEE", date);
            if (s.equals(day)) {
                list.add(e);
            }

        }
        return list;
    }
}