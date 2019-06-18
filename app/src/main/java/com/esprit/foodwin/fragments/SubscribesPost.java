package com.esprit.foodwin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.mask.PorterCircularImageView;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.Entity.Reviews;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.esprit.foodwin.utility.SubscribePostAdapter;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


public class SubscribesPost extends Fragment {
    RecyclerView estabRecyclerView;
    ImageView filterSub;
    List<Etablissement> estabList;
    List<Reviews> reviewsList;
    List<Integer> idEtabs;
    AVLoadingIndicatorView avLoadingIndicatorView;
    AVLoadingIndicatorView avLoadingIndicatorView1;
    ImageView sadImg;
    TextView sadText;
    TextView nbrSubs;
    TextView User_Name;
    PorterCircularImageView img;
    SharedPreferences loginsharedPref;
    WaveSwipeRefreshLayout swipeRefreshLayout;
    SubscribePostAdapter adapter;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribes_post, container, false);
        estabRecyclerView = view.findViewById(R.id.subRecyclerView);
        avLoadingIndicatorView = view.findViewById(R.id.progressSub);
        avLoadingIndicatorView1 = view.findViewById(R.id.progressSub1);
        filterSub = view.findViewById(R.id.filterSub);
        estabList = new ArrayList<>();
        reviewsList = new ArrayList<>();
        idEtabs = new ArrayList<>();
        sadImg = view.findViewById(R.id.sadImg);
        sadText = view.findViewById(R.id.sadText);
        nbrSubs = view.findViewById(R.id.nbrSub);
        img = view.findViewById(R.id.subImgSub);
        User_Name = view.findViewById(R.id.textView36);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshSub);
         loginsharedPref = mContext.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        User_Name.setText(loginsharedPref.getString("name" ,""));
        Glide.with(mContext)
                .load(loginsharedPref.getString("image", ""))                     // Set image url
                //      .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(img);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        swipeRefreshLayout.setWaveRGBColor(255, 33, 79);

        swipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reviewsList.clear();
                estabList.clear();
                idEtabs.clear();
                loadList(IP.ip+"reviewssub/");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        loadList(IP.ip+"reviewssub/");

        filterSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new BottomSheet.Builder(mContext)
                        .setSheet(R.menu.menu_filter)
                        .setTitle("Filter")
                        .setListener(new BottomSheetListener() {
                            @Override
                            public void onSheetShown(@NonNull BottomSheet bottomSheet, @Nullable Object o) {

                            }

                            @Override
                            public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem, @Nullable Object o) {
                                switch (menuItem.getItemId()) {
                                    case R.id.recentReviws: {
                                        avLoadingIndicatorView1.setAlpha(1);
                                        estabRecyclerView.setAlpha(0);
                                        reviewsList.clear();
                                        estabList.clear();
                                        idEtabs.clear();
                                        adapter.notifyDataSetChanged();
                                        loadList(IP.ip+"reviewssubdate/");
                                        avLoadingIndicatorView1.setAlpha(0);
                                        avLoadingIndicatorView1.hide();
                                        estabRecyclerView.setAlpha(1);


                                        break;
                                    }
                                    case R.id.bestReviews:
                                        avLoadingIndicatorView1.setAlpha(1);
                                        estabRecyclerView.setAlpha(0);
                                        reviewsList.clear();
                                        estabList.clear();
                                        idEtabs.clear();
                                        adapter.notifyDataSetChanged();
                                        loadList(IP.ip+"reviewssublikes/");
                                        avLoadingIndicatorView1.setAlpha(0);
                                        avLoadingIndicatorView1.hide();
                                        estabRecyclerView.setAlpha(1);


                                        break;


                                }
                            }

                            @Override
                            public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @Nullable Object o, int i) {

                            }
                        })
                        .show();
            }
        });
        return view;


    }

    private void getSubs() {
        idEtabs.clear();
        avLoadingIndicatorView.show();
        loginsharedPref = mContext.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String JSON_URL = IP.ip+"mysubs/" + loginsharedPref.getString("id", "");
        // String JSON_URL = "http://192.168.1.19:3000/reviews/"+String.valueOf(bundle.getInt("id"));
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor

                        try {
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = response.getJSONArray("subscribe");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object

                                // item.setImage(jsonObject.getString("image"));
                                idEtabs.add(jsonObject.getInt("fk_establishment_id"));

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    private void loadList(String url) {
        getSubs();
        reviewsList.clear();
        estabList.clear();


//
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor
                        try {


                            Date currentTime = Calendar.getInstance().getTime();

                            JSONArray jsonArray = response.getJSONArray("etabs");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Reviews item = new Reviews();
                                Etablissement etablissement = new Etablissement();

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject jsonObjectx = jsonObject.getJSONObject("establishment");
                                JSONObject jsonObjectxx = jsonObject.getJSONObject("user");
                                Integer idEtab = jsonObject.getInt("fk_establishment_id");
                                for (int y = 0; y < idEtabs.size(); y++) {
                                    if (idEtabs.contains(idEtab)) {
                                        item.setId(jsonObject.getString("id"));
                                        etablissement.setName(jsonObjectx.getString("name"));
                                        etablissement.setAddress(jsonObjectx.getString("address"));
                                        etablissement.setImg1(jsonObjectx.getString("img1"));
                                        etablissement.setId(Integer.valueOf(jsonObjectx.getString("id")));
                                        item.setService(jsonObject.getDouble("quality"));
                                        item.setQuality(jsonObject.getDouble("service"));
                                        item.setCommmentaire(jsonObject.getString("commentaire"));
                                        String date = jsonObject.getString("createdAt");
                                        item.setDate(covertTimeToText(date));
                                        item.setIdUser(jsonObjectxx.getString("id"));
                                        item.setImgUser(jsonObjectxx.getString("image"));
                                        item.setNameUser(jsonObjectxx.getString("name"));
                                        estabList.add(etablissement);
                                        reviewsList.add(item);

                                    }
                                }


                            }
                            reviewsList = reviewsList.stream().distinct().collect(Collectors.toList());
                            adapter = new SubscribePostAdapter(mContext, estabList, reviewsList);
                            estabRecyclerView.setAdapter(adapter);
                            estabRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
                            //recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            estabRecyclerView.setLayoutManager(gridLayoutManager);
                            adapter.notifyDataSetChanged();
                            avLoadingIndicatorView.hide();
                            NbrSun();

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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    public String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);


            if (second < 60) {
                convTime = second+" Seconds "+suffix;
            } else if (minute < 60) {
                convTime = minute+" Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour+" Hours "+suffix;
            } else if (day >= 7) {
                if (day > 30) {
                    convTime = (day / 30)+" Months "+suffix;
                } else if (day > 360) {
                    convTime = (day / 360)+" Years "+suffix;
                } else {
                    convTime = (day / 7) + " Week "+suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();

        }

        return convTime;

    }

    private void NbrSun() {
          loginsharedPref = mContext.getSharedPreferences("FoodWin", Context.MODE_PRIVATE);
         String JSON_URL = IP.ip+"subcount/" + loginsharedPref.getString("id", "");


        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Integer nbrSub = Integer.valueOf(String.valueOf(response.get("count")));

                            nbrSubs.setText(String.valueOf(nbrSub) + " subs");

                            if (reviewsList.size() == 0) {
                                if (nbrSub == 0) {
                                    sadImg.setImageResource(R.drawable.ic_sad);
                                    sadText.setText("You have no subscribes");
                                    sadText.setAlpha(1);
                                    estabRecyclerView.setAlpha(0);

                                } else {
                                    sadImg.setImageResource(R.drawable.ic_sad);
                                    estabRecyclerView.setAlpha(0);

                                    sadText.setText("No feeds for today , get more subs");
                                    sadText.setAlpha(1);
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

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }


}
