package com.esprit.foodwin.utility;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.HomeActivity;
import com.esprit.foodwin.R;
import com.esprit.foodwin.RestaurantDetails;
import com.esprit.foodwin.fragments.ConfirmReservation;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class ProfileEtabsAdapter extends RecyclerView.Adapter<ProfileEtabsAdapter.myViewHolder> {

    Context context;
    List<Etablissement> reservations;
    List<String> names;
    Integer countReservation = 0;


    public ProfileEtabsAdapter(Context context, List<Etablissement> reservations) {
        this.context = context;
        this.reservations = reservations;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_estabs_cell, viewGroup, false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, final int i) {

        myViewHolder.name.setText(reservations.get(i).getName());
        myViewHolder.date.setText("Created at : " + reservations.get(i).getDate().substring(0, 10));
        myViewHolder.address.setText(reservations.get(i).getAddress());
        myViewHolder.nbrReviews.setText(String.valueOf(reservations.get(i).getNbrreviews()));

        myViewHolder.checkReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmReservation dialog = new ConfirmReservation();
                dialog.populate(String.valueOf(reservations.get(i).getId()),reservations.get(i).getName(),reservations.get(i).getImg1());
                dialog.show(((HomeActivity) context).getSupportFragmentManager(), "resconfirm");
            }
        });


        //
        float rate = (float) ((reservations.get(i).getQuality() + reservations.get(i).getService()) / 2);
        if (rate >= 4.5) {
            myViewHolder.rating.setBackgroundColor(Color.parseColor("#006929"));
        } else if (rate < 4.5 && rate >= 3.5) {
            myViewHolder.rating.setBackgroundColor(Color.parseColor("#75a11d"));
        } else if (rate < 3.5 && rate >= 2.5) {
            myViewHolder.rating.setBackgroundColor(Color.parseColor("#afba0e"));
        } else if (rate < 2.5 && rate >= 0.1) {
            myViewHolder.rating.setBackgroundColor(Color.parseColor("#EED10A"));
        } else if (rate == 0) {
            myViewHolder.rating.setBackgroundColor(Color.parseColor("#878787"));
        }
        myViewHolder.rating.setText(String.valueOf(rate).substring(0, 3));
        //
        byte[] imageByteArray5 = Base64.decode(reservations.get(i).getImg1(), Base64.DEFAULT);
        Glide.with(context)
                .load(imageByteArray5)
                .apply(bitmapTransform(new BlurTransformation(10, 1))).into(myViewHolder.image);

        myViewHolder.rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", reservations.get(i).getId());

                context.startActivity(intent);
            }
        });

        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this establishment !")
                        .setCancelText("No,cancel!")
                        .setConfirmText("Yes,delete it!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                // reuse previous dialog instance, keep widget user state, reset them if you need
                                sDialog.setTitleText("Cancelled!")
                                        .setContentText("Your establishment is safe :)")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                // or you can new a SweetAlertDialog to show
                               /* sDialog.dismiss();
                                new SweetAlertDialog(SampleActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Cancelled!")
                                        .setContentText("Your imaginary file is safe :)")
                                        .setConfirmText("OK")
                                        .show();*/
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                sDialog.setTitleText("Deleted!")
                                        .setContentText("Your Establishment  has been deleted!")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                DeleteEstab(String.valueOf(reservations.get(i).getId()));
                                reservations.remove(i);
                                notifyDataSetChanged();


                            }
                        })
                        .show();
            }
        });


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, IP.ip+"bookcount/" + reservations.get(i).getId(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            if (Integer.valueOf(String.valueOf(response.get("count"))) != 0)
                                myViewHolder.checkReservation.setImageResource(R.drawable.reservation);
                            else
                                myViewHolder.checkReservation.setImageDrawable(null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurred

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        Log.d("countrev", String.valueOf(countReservation));


    }

    private void DeleteEstab(final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP.ip+"removeetab", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);

    }


    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView checkReservation;
        TextView name;
        TextView address;
        TextView rating;
        TextView nbrReviews;
        TextView date;
        ConstraintLayout parentLayout;
        com.makeramen.roundedimageview.RoundedImageView image;
        ImageView delete;


        myViewHolder(@NonNull View itemView) {
            super(itemView);
            checkReservation = itemView.findViewById(R.id.checkReservation);
            parentLayout = itemView.findViewById(R.id.myEstabParent);
            image = itemView.findViewById(R.id.myEstabImage);
            name = itemView.findViewById(R.id.myEstabName);
            address = itemView.findViewById(R.id.myEstabAddress);
            rating = itemView.findViewById(R.id.myEstabRating);
            date = itemView.findViewById(R.id.myEstabDate);
            nbrReviews = itemView.findViewById(R.id.myEtabNbrReviews);
            delete = itemView.findViewById(R.id.deleteEstab);
        }
    }

}
