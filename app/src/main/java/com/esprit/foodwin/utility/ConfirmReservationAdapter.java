package com.esprit.foodwin.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esprit.foodwin.Entity.Reservation;
import com.esprit.foodwin.R;


public class ConfirmReservationAdapter extends RecyclerView.Adapter<ConfirmReservationAdapter.myViewHolder> {

    private final String img;
    Context context;
    List<Reservation> reservations;
    String nameEtab;
    int j;

    public ConfirmReservationAdapter(Context context, List<Reservation> reservations,String idetab,String  img) {
        this.context = context;
        this.reservations = reservations;
        this.nameEtab=idetab;
        this.img=img;

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_cell, viewGroup, false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {

        myViewHolder.date.setText(reservations.get(i).getDate());
        myViewHolder.time.setText(reservations.get(i).getTime());
        myViewHolder.nbrPpl.setText(reservations.get(i).getNbrppl());
        myViewHolder.nameUser.setText(reservations.get(i).getUsername());

        //creating a hero object and giving them the values from json object


        Glide.with(context)
                .load(reservations.get(i).getUserimage())                     // Set image url
                //      .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(myViewHolder.imgUser);
        j = i;
        myViewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = IP.ip+"updatereservation";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")) {
                            reservations.remove(i);

                            notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", reservations.get(j).getId());


                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);

                AddNotification addNotification = new AddNotification();
                addNotification.postDataNotifications(nameEtab, " Confirmed your Reservation",img , reservations.get(j).getUserId());
            }
        });
        myViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = IP.ip+"deletereservation";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")) {
                            reservations.remove(j);

                            notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", reservations.get(j).getId());


                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);
                AddNotification addNotification = new AddNotification();

                addNotification.postDataNotifications(nameEtab, " Declined your Reservation",img , reservations.get(j).getUserId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public void delete() {

        String URL = IP.ip+"deletereservation";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    reservations.remove(j);

                    notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", reservations.get(j).getId());


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);

    }

    public void confirm() {
        String URL = IP.ip+"updatereservation";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    reservations.remove(j);

                   notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", reservations.get(j).getId());


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        CircularImageView imgUser;
        TextView nameUser;
        TextView date;
        TextView time;
        TextView nbrPpl;
        Button confirm;
        Button cancel;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.ResUserImg);
            nameUser = itemView.findViewById(R.id.ResUserName);
            date = itemView.findViewById(R.id.ResDate);
            time = itemView.findViewById(R.id.ResTime);
            nbrPpl = itemView.findViewById(R.id.ResNbr);
            confirm = itemView.findViewById(R.id.ResConfirm);
            cancel = itemView.findViewById(R.id.ResReject);
        }
    }


}