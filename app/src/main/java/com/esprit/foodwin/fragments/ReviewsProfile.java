package com.esprit.foodwin.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.esprit.foodwin.Entity.Reviews;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.esprit.foodwin.utility.RestaurantReviewsAdapter;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class ReviewsProfile extends Fragment {
    RecyclerView estabRecyclerView;
    List<Reviews> reviewsList;
    PorterCircularImageView reviewEstabImage;
    TextView reviewsEstabName;
    TextView reviewsEstabNbrReviews;
    ShimmerLayout shimmerLayout ;
    ImageView filterSub;
    RestaurantReviewsAdapter mAdapter;
    TextView sadTxt;
    ImageView sadImg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_reviews_profile, container, false);
        estabRecyclerView = view.findViewById(R.id.ReviewsHolder);
        reviewEstabImage = view.findViewById(R.id.reviewEstabImage);
        reviewsEstabName = view.findViewById(R.id.reviewEstabName);
        reviewsEstabNbrReviews = view.findViewById(R.id.reviewEstabNbrReviews);
        shimmerLayout = view.findViewById(R.id.shimmer_layout2);
        reviewsList = new ArrayList<>();
        filterSub = view.findViewById(R.id.filterSub1);
        sadImg = view.findViewById(R.id.sadImg6);
        sadTxt = view.findViewById(R.id.sadText6);
        filterSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(getActivity())
                        .setSheet(R.menu.menu_filter)
                        .setTitle("Filter")
                        .setListener(new BottomSheetListener() {
                            @Override
                            public void onSheetShown(@NonNull BottomSheet bottomSheet, @Nullable Object o) {

                            }

                            @Override
                            public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem, @Nullable Object o) {
                                switch (menuItem.getItemId()) {
                                    case R.id.recentReviws:

                                        estabRecyclerView.setAlpha(0);
                                        reviewsList.clear();

                                        mAdapter.notifyDataSetChanged();
                                        loadList(IP.ip+"reviewssubdatex/");

                                        estabRecyclerView.setAlpha(1);
                                        break;
                                    case R.id.bestReviews:
                                        estabRecyclerView.setAlpha(0);
                                        reviewsList.clear();

                                        mAdapter.notifyDataSetChanged();
                                        loadList(IP.ip+"reviewssublikesx/");

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

        //adding the adapter to list view

        loadList(IP.ip+"reviews/");

        return view;
    }

    private void loadList(String url) {
        shimmerLayout.startShimmerAnimation();
        Bundle bundle = getArguments();

        String JSON_URL = url+String.valueOf(bundle.getInt("id"));
        // String JSON_URL = "http://192.168.1.19:3000/reviews/"+String.valueOf(bundle.getInt("id"));
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        reviewsList.clear();
                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor
                        Log.d("toutoureview", response.toString());
                        try {
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = response.getJSONArray("etabs");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                Reviews item = new Reviews();
                                // item.setImage(jsonObject.getString("image"));
                                item.setService(jsonObject.getDouble("service"));
                                item.setDate(jsonObject.getString("createdAt"));
                                item.setQuality(jsonObject.getDouble("quality"));
                                item.setCommmentaire(jsonObject.getString("commentaire"));
                                JSONObject jsonObjectx = jsonObject.getJSONObject("user");
                                JSONObject jsonObjectxx = jsonObject.getJSONObject("establishment");
                                byte[] imageByteArray1 = Base64.decode(jsonObjectxx.getString("img1"), Base64.DEFAULT);
                                Glide.with(getContext())
                                        .load(imageByteArray1)                     // Set image url
                                        //   .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                                        .into(reviewEstabImage);
                                reviewsEstabName.setText(jsonObjectxx.getString("name"));
                                reviewsEstabNbrReviews.setText(String.valueOf(jsonObjectxx.getInt("nbrreviews")));
                                item.setNameUser(jsonObjectx.getString("name"));
                                item.setImgUser(jsonObjectx.getString("image"));
                                item.setIdUser(jsonObjectx.getString("id"));


                                item.setId(jsonObject.getString("id"));
                                //adding the json data to list
                                reviewsList.add(item);
                            }


                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
                            //  estabRecyclerView.setLayoutManager(new VegaLayoutManager());
                            estabRecyclerView.setLayoutManager(gridLayoutManager);
                            //creating custom adapter object
                            mAdapter = new RestaurantReviewsAdapter(reviewsList, getContext());
                            //adding the adapter to list view
                            estabRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            shimmerLayout.setAlpha(0);


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
