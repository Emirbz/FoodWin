package com.esprit.foodwin.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.esprit.foodwin.LoginActivity;
import com.esprit.foodwin.Map.Constants;
import com.esprit.foodwin.Map.GeocodeAddressIntentService;
import com.esprit.foodwin.MapAddEtab;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.esprit.foodwin.utility.ProfileFragementAdapter;


public class Profile extends Fragment {
    GoogleSignInClient mGoogleSignInClient;
    TabLayout tabLayout;
    ViewPager viewPager;
    AppCompatTextView gradeProfil;
    AppCompatTextView nbrRev;
    AppCompatTextView nbrSub;
    AppCompatTextView nbrCheckin;
    ImageView openSettings;
    Integer nbrRevX;
    Integer nbrSubX;
    Integer nbrCheckinX;
    ImageView imgGrade;
    boolean fetchAddress;
    int fetchType = Constants.USE_ADDRESS_LOCATION;
    AddressResultReceiver mResultReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tabLayout = view.findViewById(R.id.profiletabLayout);
        viewPager = view.findViewById(R.id.profileViewPager);
        openSettings = view.findViewById(R.id.oepnSettings);
        gradeProfil = view.findViewById(R.id.gradeProfil);
        imgGrade = view.findViewById(R.id.imgGrade);
        nbrRev = view.findViewById(R.id.nbrReviewsProfil);
        nbrSub = view.findViewById(R.id.nbrSubProfil);
        nbrCheckin = view.findViewById(R.id.nbrCheckinProfil);
        GetNumbers();
        mResultReceiver = new AddressResultReceiver(null);

        AppCompatTextView appCompatTextView = view.findViewById(R.id.nameProfil);
        CircularImageView circularImageView = view.findViewById(R.id.imgProfil);

        SharedPreferences loginsharedPref = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        appCompatTextView.setText(loginsharedPref.getString("name", ""));

        Glide.with(getContext())
                .load(loginsharedPref.getString("image", ""))                     // Set image url
                //      .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(circularImageView);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        switch (loginsharedPref.getString("grade","")){
            case "Taster": {
                imgGrade.setImageResource(R.drawable.grade1);
                gradeProfil.setText(loginsharedPref.getString("grade", ""));

                break;
            }
            case "Gourmand": {
                imgGrade.setImageResource(R.drawable.grade2);
                gradeProfil.setText(loginsharedPref.getString("grade", ""));
                break;
            }
            case "Foodie": {
                imgGrade.setImageResource(R.drawable.grade3);
                gradeProfil.setText(loginsharedPref.getString("grade", ""));
                break;
            }
        }

        ProfileFragementAdapter profileFragementAdapter = new ProfileFragementAdapter(getActivity().getSupportFragmentManager());


        profileFragementAdapter.AddFragment(new ReservationsProfile(), "Reservations");
        profileFragementAdapter.AddFragment(new MyEstablishments(), "Establishments");
        profileFragementAdapter.AddFragment(new MyReviews(), "Reviews");
        profileFragementAdapter.notifyDataSetChanged();
        viewPager.setAdapter(profileFragementAdapter);
        tabLayout.setupWithViewPager(viewPager);




        ////////////



        ////////////////



        openSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(getActivity())
                        .setSheet(R.menu.bottomsheet)
                        .setTitle("Options")
                        .setListener(new BottomSheetListener() {
                            @Override
                            public void onSheetShown(@NonNull BottomSheet bottomSheet, @Nullable Object o) {

                            }

                            @SuppressLint("ApplySharedPref")
                            @Override
                            public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem, @Nullable Object o) {
                                switch (menuItem.getItemId()) {


                                    case R.id.logOutProfile:

                                        signOut();


                                        break;
                                    case R.id.addEtab:
                                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
                                        View mView = getLayoutInflater().inflate(R.layout.dialog_location, null);


                                        Button serachMap = (Button) mView.findViewById(R.id.mapSearch);

                                        final EditText locationMap = mView.findViewById(R.id.mapLocation);


                                        mBuilder.setView(mView);
                                        final AlertDialog dialog = mBuilder.create();
                                        dialog.show();
                                        serachMap.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ProgressDialog dialogx = ProgressDialog.show(getActivity(), "",
                                                        "Loading. Please wait...", true);
                                                dialogx.show();
                                                String location = locationMap.getText().toString();
                                                fetchAddress = false;
                                                fetchType = Constants.USE_ADDRESS_NAME;

                                                dialog.dismiss();
                                                Intent intent = new Intent(getContext(), GeocodeAddressIntentService.class);
                                                intent.putExtra(Constants.RECEIVER, mResultReceiver);
                                                intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);
                                                if (fetchType == Constants.USE_ADDRESS_NAME) {

                                                    intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, location);
                                                    Log.d("cafeine", location);
                                                }
                                                getActivity().startService(intent);
                                                dialogx.dismiss();
                                            }
                                        });




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

    private void GetNumbers() {
        SharedPreferences loginsharedPref = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);



        String JSON_URL = IP.ip+"subcount/" + loginsharedPref.getString("id", "");  //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            nbrSubX = Integer.valueOf(String.valueOf(response.get("count")));
                            nbrSub.setText(String.valueOf(nbrSubX));
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
                            nbrCheckin.setText(String.valueOf(nbrCheckinX));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //displaying the error in toast if occurred
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
                            nbrRev.setText(String.valueOf(nbrRevX));
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

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Intent i = new Intent(getActivity(), MapAddEtab.class);
                        i.putExtra("lat", address.getLatitude());
                        i.putExtra("lng", address.getLongitude());
                        startActivity(i);
                        getActivity().finish();

                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // progressBar.setVisibility(View.GONE);
                        //infoText.setVisibility(View.VISIBLE);
                        //infoText.setText(resultData.getString(Constants.RESULT_DATA_KEY));

                        Toast.makeText(getContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    public static Profile newInstance() {

        return new Profile();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences loginsharedPref = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                        loginsharedPref.edit().clear().commit();
                        LoginActivity.account = null;
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);

                    }
                });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}