package com.esprit.foodwin.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.esprit.foodwin.HomeActivity;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.esprit.foodwin.utility.TabPageAdapter;

public class ViewAllByCategoryFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;


    public ViewAllByCategoryFragment() {

        // Required empty public constructor
    }

    public static ViewAllByCategoryFragment newInstance() {

        return new ViewAllByCategoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_all_by_category, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar2);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tabLayout = view.findViewById(R.id.catTabLayout);
        viewPager = view.findViewById(R.id.catViewPager);

        loadList();


        return view;
    }

    private void loadList() {
        String JSON_URL = IP.ip+"subcatx/" + getArguments().getString("id");
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor
                        Log.d("json", response.toString());
                        try {
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = response.getJSONArray("subcats");
                            //now looping through all the elements of the json array
                            TabPageAdapter adapter = new TabPageAdapter(getActivity().getSupportFragmentManager());
                            FirstTabFragment f = new FirstTabFragment();
                            Bundle bundlee = new Bundle();
                            bundlee.putString("firstid",getArguments().getString("id"));
                            f.setArguments(bundlee);
                            adapter.addFirst(f);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object


                                TabContentFragment fragment = new TabContentFragment();
                                Bundle bundle = new Bundle();

                                bundle.putString("id", jsonObject.getString("id"));
                                fragment.setArguments(bundle);
                                adapter.addFragments(fragment, jsonObject.getString("name"));
                            }

                            viewPager.setAdapter(adapter);
                            tabLayout.setupWithViewPager(viewPager);
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


