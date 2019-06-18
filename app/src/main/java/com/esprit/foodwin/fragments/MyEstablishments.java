package com.esprit.foodwin.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.esprit.foodwin.utility.ProfileEtabsAdapter;


public class MyEstablishments extends Fragment {
    RecyclerView recyclerView;
    AVLoadingIndicatorView progressBar;
    List<Etablissement> reservations;
    TextView sadTxt;
    ImageView sadImg;



    public MyEstablishments() {
        reservations = new ArrayList<>();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_establishments, container, false);
        recyclerView = view.findViewById(R.id.profileEtabRecyclerView);
        progressBar = view.findViewById(R.id.progressProfileEtab);
        sadImg = view.findViewById(R.id.sadImg7);
        sadTxt = view.findViewById(R.id.sadText7);
        loadList();
        return view;
    }
    public static MyEstablishments newInstance() {

        return new MyEstablishments();
    }

    private void loadList() {
        progressBar.show();
        SharedPreferences loginsharedPref = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String JSON_URL = IP.ip+"myetabs/" + loginsharedPref.getString("id", "");
//        String JSON_URL = "http://192.168.1.19:3000/myreviews/" + loginsharedPref.getString("id", "");
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor
                        Log.d("jsonon", response.toString());
                        try {
                            reservations.clear();
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = response.getJSONArray("etabs");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                Etablissement item = new Etablissement();
                                item.setQuality(jsonObject.getDouble("quality"));
                                item.setService(jsonObject.getDouble("service"));
                                item.setNbrreviews(jsonObject.getInt("nbrreviews"));
                                item.setName(jsonObject.getString("name"));
                                item.setAddress(jsonObject.getString("address"));
                                item.setId(jsonObject.getInt("id"));
                                item.setImg1(jsonObject.getString("img1"));
                                item.setDate(jsonObject.getString("createdAt"));


                                //adding the json data to list

                                reservations.add(item);




                            }
                            ProfileEtabsAdapter adapter = new ProfileEtabsAdapter(getContext(), reservations);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                            //recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            recyclerView.setLayoutManager(gridLayoutManager);
                            adapter.notifyDataSetChanged();
                            if (reservations.size() == 0) {

                                sadImg.setImageResource(R.drawable.ic_sad);
                                sadTxt.setText("You have no Establishments");
                                sadTxt.setAlpha(1);





                            }
                            progressBar.hide();

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
