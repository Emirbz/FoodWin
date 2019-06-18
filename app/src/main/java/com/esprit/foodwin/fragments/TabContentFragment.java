package com.esprit.foodwin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.HomeActivity;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.esprit.foodwin.utility.TabContentAdapter;
import io.supercharge.shimmerlayout.ShimmerLayout;


public class TabContentFragment extends Fragment {


    RecyclerView recyclerView;
    List<Etablissement> estabList;
    ProgressBar progressBar;
    TabContentAdapter searchAdapter;
    ShimmerLayout shimmerLayout;


    public TabContentFragment() {
        // Required empty public constructor
        estabList = new ArrayList<>();

    }

    public static TabContentFragment newInstance() {

        return new TabContentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_content, container, false);
        recyclerView = view.findViewById(R.id.allByCatRecyclerView);
        progressBar = view.findViewById(R.id.progressCategory);
        shimmerLayout = view.findViewById(R.id.shimmer_layout);

        loadList();


        return view;
    }


    private void loadList() {
        shimmerLayout.startShimmerAnimation();
        // progressBar.setVisibility(View.VISIBLE);
        String JSON_URL = IP.ip+"etabss/" + getArguments().getString("id");
        //String JSON_URL = "http://192.168.1.19:3000/etabss/" + getArguments().getString("id");
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
                            JSONArray jsonArray = response.getJSONArray("etabs");
                            //now looping through all the elements of the json array
                            estabList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                Etablissement item = new Etablissement();
                                // item.setImage(jsonObject.getString("image"));
                                item.setName(jsonObject.getString("name"));
                                item.setService(jsonObject.getDouble("service"));
                                item.setNbrreviews(jsonObject.getInt("nbrreviews"));
                                item.setName(jsonObject.getString("name"));
                                item.setRegion(jsonObject.getString("address"));
                                JSONObject jsonObjectx = jsonObject.getJSONObject("sub_category");
                                item.setSubCategory(jsonObjectx.getString("name"));
                                item.setId(jsonObject.getInt("id"));
                                item.setQuality(jsonObject.getDouble("quality"));
                                item.setImg1(jsonObject.getString("img1"));
                                item.setImg2(jsonObject.getString("img2"));
                                //adding the json data to list
                                estabList.add(item);

                            }
                            TabContentAdapter adapter = new TabContentAdapter(getContext(), estabList);
                            searchAdapter = adapter;
                            recyclerView.setAdapter(adapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapter.notifyDataSetChanged();
                            shimmerLayout.setAlpha(0);
                            //progressBar.setVisibility(View.GONE);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((HomeActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Etablissement> list = new ArrayList<>();
                for (Etablissement e : estabList) {
                    if (e.getName().toLowerCase().contains(newText.toLowerCase())) {
                        list.add(e);
                    }
                    searchAdapter.filter(list);
                }
                return false;
            }
        });
        MenuItem item2 = menu.findItem(R.id.action_filter);
        MenuItemCompat.setShowAsAction(item2, MenuItemCompat.SHOW_AS_ACTION_NEVER);
        item2.setVisible(false);


    }
}
