package com.esprit.foodwin.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esprit.foodwin.Entity.Reviews;
import com.esprit.foodwin.R;
import com.esprit.foodwin.RestaurantDetails;

public class RestaurantReviewsAdapter extends RecyclerView.Adapter<RestaurantReviewsAdapter.estabViewHolder> {
    List<Reviews> reviewsList;


    Context context;

    public RestaurantReviewsAdapter() {

    }

    public RestaurantReviewsAdapter(List<Reviews> reviewsList, Context context) {
        this.reviewsList = reviewsList;
        this.context = context;

    }

    @NonNull
    @Override
    public estabViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.reviews_cell, viewGroup, false);


        return new estabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final estabViewHolder eventsViewHolder, final int i) {
        eventsViewHolder.reviewRestaurrant.setText(reviewsList.get(i).getCommmentaire().toString());


        eventsViewHolder.rating1.setRating((float) reviewsList.get(i).getQuality());
        eventsViewHolder.rating2.setRating((float) reviewsList.get(i).getService());

        eventsViewHolder.name.setText(reviewsList.get(i).getNameUser());
        eventsViewHolder.date.setText(reviewsList.get(i).getDate().substring(0, 10) + " " + reviewsList.get(i).getDate().substring(11, 16));


        Glide.with(context)
                .load(reviewsList.get(i).getImgUser())                     // Set image url
                //   .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(eventsViewHolder.image);
        eventsViewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckProfile dialog = new CheckProfile();
                dialog.populate(reviewsList.get(i).getIdUser());
                Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();

                dialog.show(((RestaurantDetails)context).getSupportFragmentManager(),"profiledialog");

            }
        });


        String URLLL = IP.ip+"likecheck/" + reviewsList.get(i).getId() + "/" + reviewsList.get(i).getIdUser();
        JsonObjectRequest jsonObjectRequestt = new JsonObjectRequest(Request.Method.GET, URLLL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int nbr = Integer.valueOf(String.valueOf(response.get("count")));
                            if (nbr == 0) {
                                eventsViewHolder.likeButton.setLiked(false);
                            } else {
                                eventsViewHolder.likeButton.setLiked(true);
                            }
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

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequestt);


        String URLL = IP.ip+"nbrlikes/" + reviewsList.get(i).getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("id", reviewsList.get(i).getId());

                            int nbr = Integer.valueOf(String.valueOf(response.get("count")));
                            eventsViewHolder.nbrLikes.setText(String.valueOf(nbr) + " Likes");
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


        eventsViewHolder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
                String URL = IP.ip+"like";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")) {
                            String s = eventsViewHolder.nbrLikes.getText().toString().replace(" Likes", "");
                            int a = Integer.valueOf(s) + 1;
                            updateReview(reviewsList.get(i).getId(),a);
                            eventsViewHolder.nbrLikes.setText(a + " Likes");
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
                        params.put("fk_user_id", reviewsList.get(i).getIdUser());
                        params.put("fk_review_id", reviewsList.get(i).getId());


                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


            }


            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
                String URL = IP.ip+"unlike";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")) {
                            String s = eventsViewHolder.nbrLikes.getText().toString().replace(" Likes", "");
                            int a = Integer.valueOf(s) - 1;
                            updateReview(reviewsList.get(i).getId(),a);
                            eventsViewHolder.nbrLikes.setText(a + " Likes");
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
                        params.put("fk_user_id", reviewsList.get(i).getIdUser());
                        params.put("fk_review_id", reviewsList.get(i).getId());


                        return params;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


            }

        });



     /*   eventsViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return reviewsList.size();

    }

    public static class estabViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        RatingBar rating1;
        RatingBar rating2;
        TextView reviewRestaurrant;
        TextView nbrLikes;
        LikeButton likeButton;
        TextView date;


        public estabViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.circularImageViewReviews);
            name = itemView.findViewById(R.id.textView9);
            reviewRestaurrant = itemView.findViewById(R.id.reviewResto);
            nbrLikes = itemView.findViewById(R.id.nbr_likes);
            likeButton = itemView.findViewById(R.id.like_button);
            rating1 = itemView.findViewById(R.id.ratingBar3);
            rating2 = itemView.findViewById(R.id.ratingBar4);
            date = itemView.findViewById(R.id.reviewsDate);

        }
    }
    private void updateReview(String id,int nbrlikes) {




        final String URL = IP.ip+"updatereview";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {


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
                params.put("id", id);

                params.put("nbrlikes", String.valueOf(nbrlikes));


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


    }


}

