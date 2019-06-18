package com.esprit.foodwin.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esprit.foodwin.AddEventActivity;
import com.esprit.foodwin.Entity.Checkin;
import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.Entity.Event;
import com.esprit.foodwin.Entity.Reviews;
import com.esprit.foodwin.Entity.Subscribe;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.AddNotification;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;


public class DetailsRestaurant extends Fragment {
    ViewFlipper v_flipper;

    int nbrReviews;
    TextView name;
    TextView address;
    TextView openat;
    TextView closeat;
    CheckBox wifi;
    CheckBox parking;
    String nameEtab;
    CheckBox terrace;
    CheckBox debitcard;
    String idEtab;
    CheckBox alchool;
    CheckBox climatisation;
    SharedPreferences loginsharedPref;
    private MapView mapView;
    RatingBar reviewQuality;
    List<Event> eventList;
    RatingBar reviewService;
    TextView ratingCount;

    Double totalQualityEtab;
    Double totalServiceEtab;
    Toolbar toolbar;
    TextView subCat;
    int subcatId;
    AVLoadingIndicatorView progressBar;
    NestedScrollView nestedScrollView;
    Integer nbrRevX;
    Integer nbrSubX;
    Integer nbrCheckinX;
    AppBarLayout appBarLayout;
    ImageView imgCover;
    ImageView call;
    String prop;
    private List<String> mycheckinsList;
    private List<String> myReviewsDay;
    private List<String> mycheckinsListx;
    private List<String> myReviewsDayx;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(getContext(), getString(R.string.access_token));
        View view = inflater.inflate(R.layout.fragment_details_restaurant2, container, false);
        loginsharedPref = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        final Bundle bundle = getArguments();
        GetNumbers();
        mycheckinsList = new ArrayList<>();
        myReviewsDay = new ArrayList<>();
        myReviewsDayx = new ArrayList<>();
        mycheckinsListx = new ArrayList<>();
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        progressBar = view.findViewById(R.id.progressDetails);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        mapView = view.findViewById(R.id.mapView);
        imgCover = view.findViewById(R.id.imgCover);
        name = view.findViewById(R.id.textView12);
        address = view.findViewById(R.id.textView25);
        openat = view.findViewById(R.id.textView20);
        closeat = view.findViewById(R.id.textView23);
        wifi = view.findViewById(R.id.checkboxWifi);

        parking = view.findViewById(R.id.checkboxParking);
        terrace = view.findViewById(R.id.checkboxTerrace);
        debitcard = view.findViewById(R.id.checkboxDebitcard);
        alchool = view.findViewById(R.id.checkboxAlchool);
        climatisation = view.findViewById(R.id.checkboxClimatisation);
        reviewQuality = view.findViewById(R.id.ratingBar4);
        reviewService = view.findViewById(R.id.ratingBar5);
        ratingCount = view.findViewById(R.id.textView17);
        subCat = view.findViewById(R.id.subCatNameResto);
        call = view.findViewById(R.id.call);
        SpeedDialView speedDialView = view.findViewById(R.id.speedDial);
        loadList(bundle.getInt("id"), speedDialView);
        eventList = new ArrayList<>();
        loadevent(String.valueOf(bundle.getInt("id")));
        mycheckins();
        myreviews();

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.addEvent:
                        Intent intent = new Intent(getActivity(), AddEventActivity.class);

                        intent.putExtra("idetab", idEtab);
                        intent.putExtra("nameetab", nameEtab);
                        startActivity(intent);

                        return false; // true
                    case R.id.addBook:
                        ReservationDialog dialog2 = new ReservationDialog();

                        dialog2.populate(loginsharedPref.getString("id", ""), String.valueOf(bundle.getInt("id")),
                                openat.getText().toString(), closeat.getText().toString(), nameEtab, prop);
                        dialog2.show(getFragmentManager(), "res");
                        return false; // true
                    case R.id.addCalendar:
                        openDialog();
                        return false; // true
                    case R.id.addCheckin:
                        Checkin subscribe = new Checkin();
                        subscribe.setIdEtab(String.valueOf(bundle.getInt("id")));


                        subscribe.setIdUser(loginsharedPref.getString("id", ""));
                        String url = IP.ip+"addcheckin";
                        // String url = "http://192.168.1.19:3000/addsubscribe";
                        PostDataCheckin(subscribe, url, "Cheked in ");
                        speedDialView.removeActionItemById(R.id.addCheckin);


                        return false; // true to keep the Speed Dial open
                    case R.id.addReview:
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                        View mView = getLayoutInflater().inflate(R.layout.dialog_add_review, null);


                        Button mLogin = (Button) mView.findViewById(R.id.btnLogin);
                        ImageView mCancel = mView.findViewById(R.id.btnCancel);
                        final EditText reviewCommentaire = mView.findViewById(R.id.reviewCommentaire);
                        final RatingBar reviewQuality = mView.findViewById(R.id.reviewQuality);
                        final RatingBar reviewService = mView.findViewById(R.id.reviewService);


                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        mLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Reviews reviews = new Reviews();
                                reviews.setIdEtab(String.valueOf(bundle.getInt("id")));


                                reviews.setIdUser(loginsharedPref.getString("id", ""));
                                reviews.setCommmentaire(reviewCommentaire.getText().toString());
                                reviews.setQuality(reviewQuality.getRating());
                                reviews.setService(reviewService.getRating());
                                PostDataReviews(reviews, dialog);
                                Etablissement etab = new Etablissement();
                                etab.setId((bundle.getInt("id")));
                                etab.setTotalquality(totalQualityEtab);
                                etab.setTotalservice(totalServiceEtab);
                                NbrReviews(IP.ip+"reviewscount/" + String.valueOf(bundle.getInt("id")));

                                UpdateEtabReviews(etab, reviews, nbrReviews);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadList(bundle.getInt("id"), speedDialView);
                                        NbrReviews(IP.ip+"reviewscount/" + String.valueOf(bundle.getInt("id")));

                                    }
                                }, 500);


                            }
                        });
                        mCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        speedDialView.removeActionItemById(R.id.addReview);
                        return false; // true to keep the Speed Dial open
                    default:
                        return false;
                }
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    speedDialView.hide();
                } else {
                    speedDialView.show();
                }
            }
        });

        //  NbrReviews("http://192.168.1.19:3000/reviewscount/" + String.valueOf(bundle.getInt("id")));

        NbrReviews(IP.ip+"reviewscount/" + String.valueOf(bundle.getInt("id")));
        Log.d("diana", String.valueOf(subcatId));


        v_flipper = view.findViewById(R.id.v_flipper);


        LikeButton likeButton = view.findViewById(R.id.star_button);

        CheckUserSub(loginsharedPref.getString("id", ""), bundle.getInt("id"), likeButton);

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
                Subscribe subscribe = new Subscribe();
                subscribe.setIdEtab(String.valueOf(bundle.getInt("id")));


                subscribe.setIdUser(loginsharedPref.getString("id", ""));
                String url = IP.ip+"addsubscribe";
                // String url = "http://192.168.1.19:3000/addsubscribe";
                PostDataSubscribe(subscribe, url, "Subscribed");
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                String url = IP.ip+"removesubscribe";
                //  String url = "http://192.168.1.19:3000/removesubscribe";
                Subscribe subscribe = new Subscribe();
                subscribe.setIdEtab(String.valueOf(bundle.getInt("id")));


                subscribe.setIdUser(loginsharedPref.getString("id", ""));
                PostDataSubscribe(subscribe, url, "Unsubscribed");
            }
        });
        toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                getActivity().onBackPressed();

            }
        });

        int images[] = {R.drawable.resto1, R.drawable.resto2, R.drawable.resto3};

        for (int image : images) {
            flipperImages(image);
        }


        return view;
    }


    public static DetailsRestaurant newInstance() {

        return new DetailsRestaurant();

    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);
        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(3000);
        v_flipper.setAutoStart(true);

        /// animation
        v_flipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);


    }



    public void checkCheckbox(CheckBox checkBox, String value) {
        if (value.equals("yes")) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

    }

    private void PostDataReviews(final Reviews reviews, final AlertDialog dialog) {

        String s = loginsharedPref.getString("id", "");  //cr


        if (nbrSubX > 10 && nbrCheckinX > 10) {
            if (nbrRevX == 9) {
                UpdateUser("Gourmand");

            }
        }
        if (nbrSubX > 20 && nbrCheckinX > 20) {
            if (nbrRevX == 19) {
                UpdateUser("Foodie");

            }
        }

        final String URL = IP.ip+"addreview";
        //  final String URL = "http://192.168.1.19:3000/addreview";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    Toast.makeText(getContext(), "Review added", Toast.LENGTH_SHORT).show();


                    dialog.dismiss();
                    AddNotification addNotification = new AddNotification();
                    addNotification.postDataNotifications(loginsharedPref.getString("name", ""), "Added a new review to " + nameEtab, loginsharedPref.getString("image", ""), prop);
                    Log.d("marwen", prop);
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


                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fk_user_id", reviews.getIdUser());
                params.put("fk_establishment_id", reviews.getIdEtab());
                params.put("commentaire", reviews.getCommmentaire());
                params.put("quality", String.valueOf(reviews.getQuality()));
                params.put("service", String.valueOf(reviews.getService()));
                params.put("prop", prop);

                params.put("current", s);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);

    }

    public void PostDataSubscribe(final Subscribe subscribe, String URL, final String msg) {
        if (nbrRevX > 10 && nbrCheckinX > 10) {
            if (nbrSubX == 9) {
                UpdateUser("Gourmand");

            }
        }
        if (nbrRevX > 20 && nbrCheckinX > 20) {
            if (nbrSubX == 19) {
                UpdateUser("Foodie");

            }
        }
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if (URL.equals(IP.ip+"addsubscribe")) {
                        AddNotification addNotification = new AddNotification();
                        addNotification.postDataNotifications(loginsharedPref.getString("name", ""), "Subscribed to " + nameEtab, loginsharedPref.getString("image", ""), prop);
                    }

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
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fk_user_id", subscribe.getIdUser());
                params.put("fk_establishment_id", subscribe.getIdEtab());


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);

    }

    public void PostDataCheckin(final Checkin subscribe, String URL, final String msg) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        if (nbrRevX > 10 && nbrSubX > 10) {
            if (nbrCheckinX == 9) {
                UpdateUser("Gourmand");

            }
        }
        if (nbrRevX > 20 && nbrSubX > 20) {
            if (nbrCheckinX == 19) {
                UpdateUser("Foodie");

            }

        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


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
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fk_user_id", subscribe.getIdUser());
                params.put("fk_establishment_id", subscribe.getIdEtab());


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


    }

    private void UpdateEtabReviews(final Etablissement etablissement, final Reviews reviews, final int nbr) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        final String URL = IP.ip+"updateetab";
        //  final String URL = "http://192.168.1.19:3000/updateetab";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    reviewQuality.setRating((float) (etablissement.getTotalquality() + reviews.getQuality()) / (nbrReviews + 1));
                    reviewService.setRating((float) (etablissement.getTotalservice() + reviews.getQuality()) / (nbrReviews + 1));


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
                params.put("id", String.valueOf(etablissement.getId()));

                params.put("totalquality", String.valueOf(etablissement.getTotalquality() + reviews.getQuality()));
                params.put("nbrreviews", String.valueOf(nbr + 1));
                params.put("totalservice", String.valueOf(etablissement.getTotalservice() + reviews.getService()));
                params.put("quality", String.valueOf((etablissement.getTotalquality() + reviews.getQuality()) / (nbrReviews + 1)));
                params.put("service", String.valueOf((etablissement.getTotalservice() + reviews.getService()) / (nbrReviews + 1)));


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


    }


    private void NbrReviews(String JSON_URL) {

        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            nbrReviews = Integer.valueOf(String.valueOf(response.get("count")));
                            ratingCount.setText(String.valueOf(nbrReviews));
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


    private void loadList(int id, SpeedDialView speedDialView) {
        mycheckins();
        myreviews();
        progressBar.show();
        String JSON_URL = IP.ip+"etabs/" + String.valueOf(id);
        //   String JSON_URL = "http://192.168.1.19:3000/etabs/" + String.valueOf(id);
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {


                        try {
                            //we have the array named hero inside the object
                            //so here we are getting that json array

                            //now looping through all the elements of the json array

                            //getting the json object of the particular index inside the array

                            String title = response.getString("name");
                            String addressMap = response.getString("address");
                            //creating a hero object and giving them the values from json object
                            mapView.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(MapboxMap mapboxMap) {
                                    // One way to add a marker view
                                    try {
                                        mapboxMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.parseDouble(response.getString("lat")), Double.parseDouble(response.getString("long"))))
                                                .title(title)
                                                .snippet(addressMap)

                                        );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    CameraPosition cameraPosition = null;
                                    try {
                                        cameraPosition = new CameraPosition.Builder()
                                                .target(new LatLng(Double.parseDouble(response.getString("lat")), Double.parseDouble(response.getString("long"))))
                                                .zoom(15)
                                                .build();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);

                                }
                            });
                            // item.setImage(jsonObject.getString("image"));
                            nameEtab = response.getString("name");
                            name.setText(response.getString("name"));
                            address.setText(response.getString("address"));
                            // ratingCount.setText(String.valueOf(response.getInt("nbrreviews"))+ " reviews");
                            reviewQuality.setRating((float) response.getDouble("quality"));
                            reviewService.setRating((float) response.getDouble("service"));
                            idEtab = response.getString("id");

                            openat.setText(response.getString("opentime"));
                            closeat.setText(response.getString("closetime"));
                            checkCheckbox(wifi, response.getString("wifi"));
                            checkCheckbox(wifi, response.getString("wifi"));
                            checkCheckbox(terrace, response.getString("terrace"));
                            checkCheckbox(climatisation, response.getString("climatisation"));
                            checkCheckbox(parking, response.getString("parking"));
                            checkCheckbox(alchool, response.getString("alchool"));
                            checkCheckbox(debitcard, response.getString("debitcard"));
                            totalQualityEtab = response.getDouble("totalquality");
                            totalServiceEtab = response.getDouble("totalservice");
                            toolbar.setTitle(response.getString("name"));
                            loadSubCatName(response.getInt("fk_sub_categories_id"));
                            prop = response.getString("fk_user_id");
                            Calendar c = new GregorianCalendar();
                            c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
                            c.set(Calendar.MINUTE, 0);
                            c.set(Calendar.SECOND, 0);
                            Date current = c.getTime();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String today = df.format(current);
                            Log.d("todayyyy", today);
                            Log.d("todayyyy2", String.valueOf(mycheckinsList.size()));

                            for (int j = 0; j < mycheckinsList.size(); j++) {


                                if ((mycheckinsList.get(j).substring(0, 10).equals(today))) {
                                    mycheckinsListx.add(mycheckinsList.get(j));
                                }
                            }


                            if (mycheckinsListx.size() == 0) {
                                speedDialView.addActionItem(
                                        new SpeedDialActionItem.Builder(R.id.addCheckin, R.drawable.ic_checkin)
                                                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, getActivity().getTheme()))

                                                .setLabel("Check In")
                                                .setLabelColor(getResources().getColor(R.color.black, getActivity().getTheme()))
                                                .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getActivity().getTheme()))


                                                .create()
                                );
                            }


                            if (prop.equals(loginsharedPref.getString("id", ""))) {
                                speedDialView.addActionItem(
                                        new SpeedDialActionItem.Builder(R.id.addEvent, R.drawable.addevent)
                                                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.color_accent, getActivity().getTheme()))

                                                .setLabel("Add Event")
                                                .setLabelColor(getResources().getColor(R.color.black, getActivity().getTheme()))
                                                .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getActivity().getTheme()))


                                                .create()
                                );

                            }
                            for (int j = 0; j < myReviewsDay.size(); j++) {


                                if ((myReviewsDay.get(j).substring(0, 10).equals(today))) {
                                    myReviewsDayx.add(myReviewsDay.get(j));
                                }
                            }


                            if (myReviewsDayx.size() == 0) {
                                speedDialView.addActionItem(
                                        new SpeedDialActionItem.Builder(R.id.addReview, R.drawable.addreview)
                                                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.mapbox_plugins_bright_blue, getActivity().getTheme()))

                                                .setLabel("Add Review")
                                                .setLabelColor(getResources().getColor(R.color.black, getActivity().getTheme()))
                                                .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getActivity().getTheme()))

                                                .create()
                                );
                            }


                            speedDialView.addActionItem(
                                    new SpeedDialActionItem.Builder(R.id.addBook, R.drawable.ic_book)
                                            .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.main_blue_stroke_color, getActivity().getTheme()))

                                            .setLabel("Book a table")
                                            .setLabelColor(getResources().getColor(R.color.black, getActivity().getTheme()))
                                            .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getActivity().getTheme()))


                                            .create()
                            );
                            speedDialView.addActionItem(
                                    new SpeedDialActionItem.Builder(R.id.addCalendar, R.drawable.ic_events5)
                                            .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.google_color, getActivity().getTheme()))

                                            .setLabel("Check events")
                                            .setLabelColor(getResources().getColor(R.color.black, getActivity().getTheme()))
                                            .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.white, getActivity().getTheme()))


                                            .create()
                            );
                            byte[] imageByteArray1 = Base64.decode(response.getString("img1"), Base64.DEFAULT);
                            byte[] imageByteArray2 = Base64.decode(response.getString("img2"), Base64.DEFAULT);
                            byte[] imageByteArray3 = Base64.decode(response.getString("img3"), Base64.DEFAULT);
                            byte[] imageByteArray4 = Base64.decode(response.getString("img4"), Base64.DEFAULT);
                            byte[] imageByteArray5 = Base64.decode(response.getString("img5"), Base64.DEFAULT);
                            Glide.with(getContext())
                                    .load(imageByteArray1)                     // Set image url
                                    // .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                                    .into(imgCover);

                            call.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent call = new Intent(Intent.ACTION_DIAL);
                                    try {
                                        call.setData(Uri.parse("tel:" + response.getString("phone")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(call);
                                }
                            });


                            nestedScrollView.setAlpha(1);
                            appBarLayout.setAlpha(1);
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

    private void loadSubCatName(int id) {

        String JSON_URL = IP.ip+"subcatid/" + String.valueOf(id);
        //String JSON_URL = "http://192.168.1.19:3000/subcatid/" + String.valueOf(id);
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            //we have the array named hero inside the object
                            //so here we are getting that json array

                            //now looping through all the elements of the json array

                            //getting the json object of the particular index inside the array


                            //creating a hero object and giving them the values from json object

                            // item.setImage(jsonObject.getString("image"));
                            Log.d("diana1", response.getString("name"));
                            subCat.setText(response.getString("name"));


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

    private void CheckUserSub(String iduser, int idetab, final LikeButton likeButton) {
        String JSON_URL = IP.ip+"subcheck/" + String.valueOf(idetab) + "/" + iduser;
        //    String JSON_URL = "http://192.168.1.19:3000/subcheck/" + String.valueOf(idetab) + "/" + iduser;
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int subCheck = Integer.valueOf(String.valueOf(response.get("count")));
                            Log.d("subcheck", String.valueOf(subCheck));
                            if (subCheck > 0)
                                likeButton.setLiked(true);

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

    private void GetNumbers() {

        String JSON_URL = IP.ip+"subcount/" + loginsharedPref.getString("id", "");  //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            nbrSubX = Integer.valueOf(String.valueOf(response.get("count")));

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


        String JSON_URL2 = IP.ip+"checkincount/" + loginsharedPref.getString("id", "");  //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, JSON_URL2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            nbrCheckinX = Integer.valueOf(String.valueOf(response.get("count")));

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

        String JSON_URL3 = IP.ip+"revcount/" + loginsharedPref.getString("id", "");  //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.GET, JSON_URL3, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            nbrRevX = Integer.valueOf(String.valueOf(response.get("count")));

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
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest2);
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest3);
    }

    private void UpdateUser(String grade) {


        SharedPreferences.Editor editor = loginsharedPref.edit();

        editor.putString("grade", grade);
        editor.apply();
        final String URL = IP.ip+"updateuser";
        //  final String URL = "http://192.168.1.19:3000/updateetab";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {


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
                params.put("id", loginsharedPref.getString("id", ""));

                params.put("grade", grade);


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    void openDialog() {
        EventEstabCalendar dialog = new EventEstabCalendar();
        dialog.populate(eventList, name.getText().toString());
        dialog.show(getFragmentManager(), "events");
    }

    private void loadevent(String id) {

        String JSON_URL = IP.ip+"eventsetab/" + id;
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
                            JSONArray jsonArray = response.getJSONArray("events");


                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                final Event item = new Event();
                                item.setId(jsonObject.getString("id"));
                                item.setDate(jsonObject.getString("date"));
                                item.setName(jsonObject.getString("name"));
                                eventList.add(item);
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


    void mycheckins() {
        String JSON_URL = IP.ip+"mycheckins/" + loginsharedPref.getString("id", "") + "/" + getArguments().getInt("id");
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
                            JSONArray jsonArray = response.getJSONArray("check");


                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                Log.d("todayyyy3", jsonObject.getString("createdAt"));
                                mycheckinsList.add(jsonObject.getString("createdAt"));
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

    void myreviews() {
        String JSON_URL = IP.ip+"myreviewsx/" + loginsharedPref.getString("id", "") + "/" + getArguments().getInt("id");
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
                            JSONArray jsonArray = response.getJSONArray("rev");


                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object

                                myReviewsDay.add(jsonObject.getString("createdAt"));
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
