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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.HomeActivity;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.esprit.foodwin.utility.TabContentAdapter;
import io.supercharge.shimmerlayout.ShimmerLayout;

public class FirstTabFragment extends Fragment implements FilterDialog.filter {

    RecyclerView recyclerView;
    List<Etablissement> estabList;
    TabContentAdapter adapter;
    private TabContentAdapter searchAdapter;
    ShimmerLayout shimmerLayout ;

    public FirstTabFragment() {
        estabList = new ArrayList<>();
    }

    public static FirstTabFragment newInstance() {
        return new FirstTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_first_tab, container, false);

        recyclerView = view.findViewById(R.id.firstRecyclerView);
        shimmerLayout = view.findViewById(R.id.shimmer_layout);
        loadList();


        return view;
    }

    void openDialog() {
        FilterDialog dialog = new FilterDialog();
        dialog.etablissements = estabList;
        dialog.setTargetFragment(this, 1);
        dialog.show(getFragmentManager(), "Filter");
    }

    private void loadList() {
        shimmerLayout.startShimmerAnimation();
        String JSON_URL = IP.ip+"etabss/";
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor
                        Log.d("jsonon", response.toString());
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
                                item.setId(jsonObject.getInt("id"));
                                item.setName(jsonObject.getString("name"));
                                item.setRegion(jsonObject.getString("address"));
                                item.setAlchool(jsonObject.getString("alchool"));
                                item.setClimatisation(jsonObject.getString("climatisation"));
                                item.setParking(jsonObject.getString("parking"));
                                item.setWifi(jsonObject.getString("wifi"));
                                item.setTerrace(jsonObject.getString("terrace"));
                                item.setDebitcard(jsonObject.getString("debitcard"));
                                JSONObject jsonObjectx = jsonObject.getJSONObject("sub_category");
                                item.setSubCategory(jsonObjectx.getString("name"));
                                item.setQuality(jsonObject.getDouble("quality"));
                                item.setService(jsonObject.getDouble("service"));
                                item.setRating((item.getQuality() + item.getService()) / 2);
                                item.setImg1(jsonObject.getString("img1"));
                                item.setImg2(jsonObject.getString("img2"));
                                item.setNbrRev(jsonObject.getInt("nbrreviews"));
                                //adding the json data to list

                                if (String.valueOf(jsonObjectx.getInt("fk_categories_id")).equals(getArguments().getString("firstid"))) {
                                    estabList.add(item);
                                }
                                Log.d("size", String.valueOf(estabList.size()));
                            }
                            adapter = new TabContentAdapter(getContext(), estabList);
                            recyclerView.setAdapter(adapter);
                            searchAdapter = adapter;
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapter.notifyDataSetChanged();
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

    @Override
    public void filterr(String alc, String clim, String ter, String par, String wifi, String card, String rat, String rev) {
        List<Etablissement> list = estabList;
        List<Etablissement> filtred = new ArrayList<>();
        if (wifi.equals("yes")) {
            if (filtred.size() > 0) {
                filtred = filtred.stream().filter(e -> e.getWifi().equals(wifi)).collect(Collectors.toList());
            } else {
                filtred = list.stream().filter(e -> e.getWifi().equals(wifi)).collect(Collectors.toList());
            }

        }
        if (alc.equals("yes")) {
            if (filtred.size() > 0) {
                filtred = filtred.stream().filter(e -> e.getAlchool().equals(alc)).collect(Collectors.toList());

            } else {
                filtred = list.stream().filter(e -> e.getAlchool().equals(alc)).collect(Collectors.toList());

            }
        }
        if (clim.equals("yes")) {
            if (filtred.size() > 0) {
                filtred = filtred.stream().filter(e -> e.getClimatisation().equals(clim)).collect(Collectors.toList());

            } else {
                filtred = list.stream().filter(e -> e.getClimatisation().equals(clim)).collect(Collectors.toList());

            }
        }
        if (ter.equals("yes")) {
            if (filtred.size() > 0) {
                filtred = filtred.stream().filter(e -> e.getTerrace().equals(ter)).collect(Collectors.toList());

            } else {
                filtred = list.stream().filter(e -> e.getTerrace().equals(ter)).collect(Collectors.toList());

            }
        }
        if (card.equals("yes")) {
            if (filtred.size() > 0) {
                filtred = filtred.stream().filter(e -> e.getDebitcard().equals(card)).collect(Collectors.toList());

            } else {
                filtred = list.stream().filter(e -> e.getDebitcard().equals(card)).collect(Collectors.toList());

            }
        }
        if (par.equals("yes")) {
            if (filtred.size() > 0) {
                filtred = filtred.stream().filter(e -> e.getParking().equals(par)).collect(Collectors.toList());

            } else {
                filtred = list.stream().filter(e -> e.getParking().equals(par)).collect(Collectors.toList());

            }
        }

        if (par.equals("yes") && clim.equals("yes") && ter.equals("yes") && card.equals("yess") && alc.equals("yes") && wifi.equals("yes")) {
            filtred = list.stream().filter(e -> e.getParking().equals(par)).filter(e -> e.getClimatisation().equals(clim))
                    .filter(e -> e.getTerrace().equals(ter)).filter(e -> e.getDebitcard().equals(card)).filter(e -> e.getAlchool().equals(alc))
                    .filter(e -> e.getWifi().equals(wifi)).collect(Collectors.toList());
        }
        if (par.equals("no") && clim.equals("no") && ter.equals("no") && card.equals("no") && alc.equals("no") && wifi.equals("no")) {
            filtred = list;
        }

        if (rat.equals("yes")) {
            filtred.sort(Comparator.comparingDouble(Etablissement::getRating).reversed());
        }
        if (rev.equals("yes")) {
            filtred.sort(Comparator.comparingDouble(Etablissement::getNbrRev).reversed());
        }


        adapter.filter(filtred);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item1 = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((HomeActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item1, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item1, searchView);
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
        MenuItemCompat.setShowAsAction(item2, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getActivity().invalidateOptionsMenu();
                openDialog();
                return false;
            }
        });


    }
}
