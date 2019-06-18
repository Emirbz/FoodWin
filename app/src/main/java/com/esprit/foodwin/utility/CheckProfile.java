package com.esprit.foodwin.utility;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import com.esprit.foodwin.R;

public class CheckProfile extends AppCompatDialogFragment {

    ViewPager viewPager;
    AppCompatTextView gradeProfil;
    AppCompatTextView nbrRev;
    AppCompatTextView nbrSub;
    AppCompatTextView nbrCheckin;
    AppCompatTextView appCompatTextView;
    Integer nbrRevX;
    CircularImageView circularImageView;
    Integer nbrSubX;
    Integer nbrCheckinX;

    ImageView imgGrade;
    String user;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_check_profile, null);


        gradeProfil = view.findViewById(R.id.gradeProfilx);
        imgGrade = view.findViewById(R.id.imgGradex);
        nbrRev = view.findViewById(R.id.nbrReviewsProfilx);
        nbrSub = view.findViewById(R.id.nbrSubProfilx);
        nbrCheckin = view.findViewById(R.id.nbrCheckinProfilx);
        appCompatTextView = view.findViewById(R.id.nameProfilx);
        circularImageView = view.findViewById(R.id.imgProfilx);
        loadeventsList(user);
        GetNumbers(user);


        builder.setView(view)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        return builder.create();
    }


    public void populate(String s) {
        user = s;
    }

    private void GetNumbers(String user) {


        String JSON_URL = IP.ip+"subcount/" + user;  //creating a string request to send request to the url
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
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurred
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        String JSON_URL2 = IP.ip+"checkincount/" + user;  //creating a string request to send request to the url
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
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurred
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        String JSON_URL3 = IP.ip+"revcount/" + user;  //creating a string request to send request to the url
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
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurred
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest2);
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest3);
    }

    private void loadeventsList(String user) {

        String JSON_URL = IP.ip+"userx/" + user;
        //   String JSON_URL = "http://192.168.1.19:3000/events";
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after completion and showing list view
                        // Showing json data in log monitor
                        Log.d("json", response.toString());
                        try {
                            //we have the array named hero inside the object
                            //so here we are getting that json array

                            //now looping through all the elements of the json array

                            //getting the json object of the particular index inside the array

                            JSONObject jsonObject = response.getJSONObject("user");

                            //creating a hero object and giving them the values from json object

                            appCompatTextView.setText(jsonObject.getString("name"));
                            Glide.with(getContext())
                                    .load(jsonObject.getString("image"))                     // Set image url
                                    //      .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                                    .into(circularImageView);
                            String grade = (jsonObject.getString("grade"));
                            gradeProfil.setText(grade);
                            switch (grade) {
                                case "Taster":
                                    imgGrade.setImageResource(R.drawable.grade1);
                                    break;
                                case "Gourmand":
                                    imgGrade.setImageResource(R.drawable.grade2);
                                    break;
                                case "Foodie":
                                    imgGrade.setImageResource(R.drawable.grade3);
                                    break;
                                default:
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
