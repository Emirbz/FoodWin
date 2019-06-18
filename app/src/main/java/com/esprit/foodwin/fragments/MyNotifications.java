package com.esprit.foodwin.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.mask.PorterCircularImageView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.esprit.foodwin.Entity.Notifications;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.esprit.foodwin.utility.NotificationsAdapter;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class MyNotifications extends Fragment {
    RecyclerView notifRecyclerView;
    ImageView seen;
    List<Notifications> notifList;

    AVLoadingIndicatorView avLoadingIndicatorView;

    ImageView sadImg;
    TextView sadText;
    TextView nbrSubs;
    TextView User_Name;
    PorterCircularImageView img;
    WaveSwipeRefreshLayout swipeRefreshLayout;
    NotificationsAdapter adapter;

    bottom listener;
    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_notifications, container, false);

        notifRecyclerView = view.findViewById(R.id.NotificationsRecyclerView);
        avLoadingIndicatorView = view.findViewById(R.id.progressSub3);

        seen = view.findViewById(R.id.seen);
        notifList = new ArrayList<>();
        sadImg = view.findViewById(R.id.sadImg1);
        sadText = view.findViewById(R.id.sadText1);
        nbrSubs = view.findViewById(R.id.nbrSub1);
        img = view.findViewById(R.id.subImgSub1);
        User_Name = view.findViewById(R.id.textView361);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshSub1);
        SharedPreferences loginsharedPref = mContext.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        User_Name.setText(loginsharedPref.getString("name", ""));
        Glide.with(mContext)
                .load(loginsharedPref.getString("image", ""))                     // Set image url
                //      .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(img);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        swipeRefreshLayout.setWaveRGBColor(255, 33, 79);

        swipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notifList.clear();
                loadList();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        loadList();


        seen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteReveiew();

                int count = adapter.getItemCount();
                notifList.clear();
                adapter.notifyItemRangeRemoved(0, count);

                listener.getbottom();


            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {

        try {
            listener = (bottom) getActivity();
        } catch (ClassCastException e) {
            System.out.println("must implement interface");
        }
        super.onAttach(context);
        mContext = context;
    }

    public String covertTimeToText(String dataDate) {
        if ((dataDate.charAt(12) == '2') && (dataDate.charAt(13) == '3')) {
            String string1 = dataDate.substring(0, 11);
            string1 += "00";
            String string2 = dataDate.substring(13, 24);

            string1 += string2;
            dataDate = string1;

        } else {
            String string1 = dataDate.substring(0, 11);
            Integer integer1 = Integer.valueOf(dataDate.substring(11, 13));
            integer1 += 1;
            string1 += String.valueOf(integer1);
            String string2 = dataDate.substring(13, 24);

            string1 += string2;
            dataDate = string1;
        }

        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);


            if (second < 60) {
                convTime = second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Minutes " + suffix;
            } else if (hour < 24) {
                convTime = hour + " Hours " + suffix;
            } else if (day >= 7) {
                if (day > 30) {
                    convTime = (day / 30) + " Months " + suffix;
                } else if (day > 360) {
                    convTime = (day / 360) + " Years " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day + " Days " + suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();

        }

        return convTime;

    }

    private void loadList() {
        SharedPreferences loginsharedPref = mContext.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        notifList.clear();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, IP.ip + "notifications/" + loginsharedPref.getString("id", ""), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor

                        try {


                            JSONArray jsonArray = response.getJSONArray("notifications");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Notifications item = new Notifications();


                                JSONObject jsonObject = jsonArray.getJSONObject(i);


                                item.setId(jsonObject.getString("id"));


                                item.setIdUser(jsonObject.getString("fk_user_id"));
                                item.setImage(jsonObject.getString("image"));
                                item.setText(jsonObject.getString("text"));
                                item.setTitle(jsonObject.getString("title"));
                                String date = covertTimeToText(jsonObject.getString("createdAt"));
                                item.setDate(date);

                                notifList.add(item);


                            }

                            adapter = new NotificationsAdapter(mContext, notifList);
                            notifRecyclerView.setAdapter(adapter);
                            notifRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
                            //recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            notifRecyclerView.setLayoutManager(gridLayoutManager);
                            adapter.notifyItemRangeInserted(0, notifList.size());
                            avLoadingIndicatorView.hide();
                            avLoadingIndicatorView.setAlpha(0);
                            NbrSun();

                            sadText.setAlpha(0);
                            sadImg.setImageDrawable(null);
                            notifRecyclerView.setAlpha(1);

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void NbrSun() {
        SharedPreferences loginsharedPref = mContext.getSharedPreferences("FoodWin", Context.MODE_PRIVATE);
        String JSON_URL = IP.ip + "notifcount/" + loginsharedPref.getString("id", "");
        Log.d("sassy3",JSON_URL);


        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Integer nbrSub = Integer.valueOf(String.valueOf(response.get("count")));

                            nbrSubs.setText(String.valueOf(nbrSub) + " notifications");

                            if (notifList.size() == 0) {
                                if (nbrSub == 0) {

                                    sadImg.setImageResource(R.drawable.ic_sad);
                                    sadText.setText("You have no notifications for today");
                                    sadText.setAlpha(1);
                                    notifRecyclerView.setAlpha(0);

                                }

                            }
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void DeleteReveiew() {

        SharedPreferences loginsharedPref = mContext.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP.ip + "deletenotifications", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {

                    NbrSun();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                }  else if (volleyError instanceof AuthFailureError) {
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fk_user_id", loginsharedPref.getString("id", ""));


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(stringRequest);

    }

    public interface bottom {
        void getbottom();
    }
}
