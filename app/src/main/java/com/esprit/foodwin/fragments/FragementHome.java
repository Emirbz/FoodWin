package com.esprit.foodwin.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.github.siyamed.shapeimageview.CircularImageView;
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

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.Entity.Event;
import com.esprit.foodwin.MapSearch;
import com.esprit.foodwin.R;
import com.esprit.foodwin.RestaurantDetails;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.esprit.foodwin.utility.SearchAdapter;
import com.esprit.foodwin.utility.TodayEventsAdapter;
import com.esprit.foodwin.utility.TopCoffeesAdapter;
import com.esprit.foodwin.utility.TopDrinkAdapter;
import com.esprit.foodwin.utility.TopEstablishmentsAdapter;
import com.esprit.foodwin.utility.TopRestoAdapter;


public class FragementHome extends Fragment {
    private List<Etablissement> etabList;
    private List<Etablissement> topEtabList;
    private List<Etablissement> topCoffeesList;
    private List<Etablissement> topDrinkList;
    Toolbar toolbar;
    List<Event> eventList;
    RecyclerView eventsRecyclerView;
    RecyclerView estabRecyclerView;
    RecyclerView topEstabRecyclerView;
    RecyclerView topCoffeesRecyclerView;
    RecyclerView topDrinkRecyclerView;
    LinearLayout food;
    LinearLayout drinks;
    LinearLayout coffee;
    CircularImageView userImg;
    AVLoadingIndicatorView progressBar;
    AVLoadingIndicatorView progressBar2;
    Button allResto;
    Button allCoffee;
    Context mContext;
    Button allDrink;
    ImageView mapsearch;
    private AutoCompleteTextView userName;
    private ArrayList<Etablissement> userList;
    ImageView sadImg;
    TextView sadTxt;
    JSONArray jsonArrayx;


    String subCatName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragement_home, container, false);
        userName = view.findViewById(R.id.user_name);
        mapsearch = view.findViewById(R.id.imageView10);
        mapsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapSearch.class);
                getActivity().startActivity(intent);
            }
        });
        etabList = new ArrayList<>();
        topEtabList = new ArrayList<>();
        topCoffeesList = new ArrayList<>();
        topDrinkList = new ArrayList<>();
        eventList = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressHome);
        progressBar2 = view.findViewById(R.id.progressHome2);
        sadImg = view.findViewById(R.id.sadImg1x);
        sadTxt = view.findViewById(R.id.sadTextx);
        //  userImg = view.findViewById(R.id.userImagex);
        SharedPreferences loginsharedPref = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

    /*    Glide.with(getContext())
                .load(loginsharedPref.getString("image", ""))                     // Set image url
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(userImg);*/

        allResto = view.findViewById(R.id.sellAllRestaurants);
        allCoffee = view.findViewById(R.id.sellAllCoffees);
        allDrink = view.findViewById(R.id.sellAllDrinks);
        food = view.findViewById(R.id.foodCat);
        drinks = view.findViewById(R.id.drinkCat);
        coffee = view.findViewById(R.id.coffeeCat);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllByCategoryFragment fragment = new ViewAllByCategoryFragment();
                Bundle args = new Bundle();
                args.putString("id", "1");
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, fragment).commit();
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllByCategoryFragment fragment = new ViewAllByCategoryFragment();
                Bundle args = new Bundle();
                args.putString("id", "2");
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, fragment).commit();
            }
        });
        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllByCategoryFragment fragment = new ViewAllByCategoryFragment();
                Bundle args = new Bundle();
                args.putString("id", "3");
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, fragment).commit();
            }
        });


        allResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllByCategoryFragment fragment = new ViewAllByCategoryFragment();
                Bundle args = new Bundle();
                args.putString("id", "1");
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, fragment).commit();
            }
        });
        allDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllByCategoryFragment fragment = new ViewAllByCategoryFragment();
                Bundle args = new Bundle();
                args.putString("id", "2");
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, fragment).commit();
            }
        });
        allCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAllByCategoryFragment fragment = new ViewAllByCategoryFragment();
                Bundle args = new Bundle();
                args.putString("id", "3");
                fragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, fragment).commit();
            }
        });


        eventsRecyclerView = view.findViewById(R.id.eventsHolder);


        estabRecyclerView = view.findViewById(R.id.estabHolder);
        topEstabRecyclerView = view.findViewById(R.id.topEstabRecycleView);
        topCoffeesRecyclerView = view.findViewById(R.id.topCoffeesRecycleView);
        topDrinkRecyclerView = view.findViewById(R.id.topDrinkRecycleView);
        loadeventsList();
        loadList();
        loadListTopResto();
        loadListTopCoffees();
        loadListTopDrink();
        loadListSearch();


        return view;
    }

    private void loadList() {
        progressBar.setAlpha(1);
        progressBar.show();

        String JSON_URL = IP.ip+"etabss6";
        //   String JSON_URL = "http://192.168.1.19:3000/etabss6";
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
                            JSONArray jsonArray = response.getJSONArray("etabs");
                          /*  userList = extractUser(jsonArray);

                            SearchAdapter userAdapter = new SearchAdapter(Objects.requireNonNull(getContext()), R.layout.user_raw_layout, userList);

                            userName.setAdapter(userAdapter);
                            userName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Etablissement user = (Etablissement) parent.getItemAtPosition(position);
                                    Intent intent = new Intent(getActivity(), RestaurantDetails.class);
                                    Log.d("idtest", String.valueOf(user.getId()) + user.getName());
                                    intent.putExtra("id", user.getId());
                                    startActivity(intent);

                                }
                            });*/

                            //now looping through all the elements of the json array
                            for (int i = 0; i < 6; i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                final Etablissement item = new Etablissement();
                                // item.setImage(jsonObject.getString("image"));
                                item.setId(jsonObject.getInt("id"));

                                item.setQuality(jsonObject.getDouble("quality"));
                                item.setService(jsonObject.getDouble("service"));
                                item.setName(jsonObject.getString("name"));

                                /////
                                JSONObject jsonObjectx = jsonObject.getJSONObject("sub_category");
                                item.setSubCategory(jsonObjectx.getString("name"));


                                /////////
                                item.setAddress(jsonObject.getString("address"));

                                item.setImg1(jsonObject.getString("img1"));

                                //adding the json data to list
                                etabList.add(item);
                            }

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                            estabRecyclerView.setLayoutManager(gridLayoutManager);
                            //creating custom adapter object
                            TopEstablishmentsAdapter mAdapter = new TopEstablishmentsAdapter(etabList, getContext());
                            //adding the adapter to list view
                            estabRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
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
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void loadListSearch() {


        String JSON_URL = IP.ip+"etabssearch";
        //   String JSON_URL = "http://192.168.1.19:3000/etabss6";
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
                            JSONArray jsonArray = response.getJSONArray("etabs");
                            userList = extractUser(jsonArray);

                            SearchAdapter userAdapter = new SearchAdapter(mContext, R.layout.user_raw_layout, userList);

                            userName.setAdapter(userAdapter);
                            userName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Etablissement user = (Etablissement) parent.getItemAtPosition(position);
                                    Intent intent = new Intent(getActivity(), RestaurantDetails.class);

                                    intent.putExtra("id", user.getId());
                                    startActivity(intent);

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
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
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
                        Log.d("wadiii", response.toString());
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
                                item.setDate(jsonObject.getString("date"));
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

                                if (Integer.valueOf(year).equals(Integer.valueOf(currentyear)) && Integer.valueOf(month).equals(Integer.valueOf(currentmonth)) && Integer.valueOf(day).equals(Integer.valueOf(currentday))) {
                                    eventList.add(item);
                                }
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            eventsRecyclerView.setLayoutManager(linearLayoutManager);
                            //creating custom adapter object
                            TodayEventsAdapter mAdapter = new TodayEventsAdapter(eventList, getContext());
                            //adding the adapter to list view
                            eventsRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            if (eventList.size()==0)
                            {
                                sadImg.setImageResource(R.drawable.ic_sad);
                                sadTxt.setText("There is no events for today");
                                sadTxt.setAlpha(1);
                            }
                            else
                            {
                                sadImg.setImageDrawable(null);
                                sadTxt.setText("");
                                sadTxt.setAlpha(0);
                            }
                            progressBar2.hide();

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
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
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

    private void loadListTopResto() {


        String JSON_URL = IP.ip+"etabssresto6";
        // String JSON_URL = "http://192.168.1.19:3000/etabssresto6";
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor
                        Log.d("amirx", response.toString());

                        try {

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = response.getJSONArray("etabs");


                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                final Etablissement item = new Etablissement();
                                // item.setImage(jsonObject.getString("image"));
                                item.setId(jsonObject.getInt("id"));

                                item.setQuality(jsonObject.getDouble("quality"));
                                item.setService(jsonObject.getDouble("service"));
                                item.setName(jsonObject.getString("name"));

                                /////
                                JSONObject jsonObjectx = jsonObject.getJSONObject("sub_category");
                                item.setSubCategory(jsonObjectx.getString("name"));


                                /////////


                                item.setAddress(jsonObject.getString("address"));

                                item.setImg1(jsonObject.getString("img1"));

                                //adding the json data to list
                                topEtabList.add(item);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            topEstabRecyclerView.setLayoutManager(linearLayoutManager);
                            //creating custom adapter object
                            TopRestoAdapter mAdapter = new TopRestoAdapter(topEtabList, getContext());
                            //adding the adapter to list view
                            topEstabRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();


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
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
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

    private void loadListTopDrink() {


        String JSON_URL = IP.ip+"etabssdrinkto6";
        //    String JSON_URL = "http://192.168.1.19:3000/etabssdrinkto6";
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor
                        Log.d("amirx", response.toString());

                        try {

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = response.getJSONArray("etabs");


                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                final Etablissement item = new Etablissement();
                                // item.setImage(jsonObject.getString("image"));
                                item.setId(jsonObject.getInt("id"));

                                item.setQuality(jsonObject.getDouble("quality"));
                                item.setService(jsonObject.getDouble("service"));
                                item.setName(jsonObject.getString("name"));

                                /////
                                JSONObject jsonObjectx = jsonObject.getJSONObject("sub_category");
                                item.setSubCategory(jsonObjectx.getString("name"));


                                /////////


                                item.setAddress(jsonObject.getString("address"));

                                item.setImg1(jsonObject.getString("img1"));

                                //adding the json data to list
                                topDrinkList.add(item);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            topDrinkRecyclerView.setLayoutManager(linearLayoutManager);
                            //creating custom adapter object
                            TopDrinkAdapter mAdapter = new TopDrinkAdapter(topDrinkList, getContext());
                            //adding the adapter to list view
                            topDrinkRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();


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
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
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

    private void loadListTopCoffees() {


        String JSON_URL = IP.ip+"etabsscoffees6";
        //   String JSON_URL = "http://192.168.1.19:3000/etabsscoffees6";
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor
                        Log.d("amirx", response.toString());

                        try {

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = response.getJSONArray("etabs");


                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                final Etablissement item = new Etablissement();
                                // item.setImage(jsonObject.getString("image"));
                                item.setId(jsonObject.getInt("id"));

                                item.setQuality(jsonObject.getDouble("quality"));
                                item.setService(jsonObject.getDouble("service"));
                                item.setName(jsonObject.getString("name"));

                                /////
                                JSONObject jsonObjectx = jsonObject.getJSONObject("sub_category");
                                item.setSubCategory(jsonObjectx.getString("name"));


                                /////////


                                item.setAddress(jsonObject.getString("address"));

                                item.setImg1(jsonObject.getString("img1"));

                                //adding the json data to list
                                topCoffeesList.add(item);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            topCoffeesRecyclerView.setLayoutManager(linearLayoutManager);
                            //creating custom adapter object
                            TopCoffeesAdapter mAdapter = new TopCoffeesAdapter(topCoffeesList, getContext());
                            //adding the adapter to list view
                            topCoffeesRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();


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
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
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

    private ArrayList<Etablissement> extractUser(JSONArray jsonArray) {
        ArrayList<Etablissement> list = new ArrayList<>();

        try {


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jo = jsonArray.getJSONObject(i);

                int id = jo.getInt("id");
                String name = jo.getString("name");

                String address = jo.getString("address");
                String img = jo.getString("img1");

                Etablissement user = new Etablissement();
                user.setName(name);
                user.setAddress(address);
                user.setImg1(img);
                user.setId(id);


                list.add(user);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }


}
