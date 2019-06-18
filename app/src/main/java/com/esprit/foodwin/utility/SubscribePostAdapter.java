package com.esprit.foodwin.utility;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.siyamed.shapeimageview.mask.PorterCircularImageView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.Entity.Reviews;
import com.esprit.foodwin.HomeActivity;
import com.esprit.foodwin.R;
import com.esprit.foodwin.RestaurantDetails;

public class SubscribePostAdapter extends  RecyclerView.Adapter<SubscribePostAdapter.myViewHolder>
{

    Context context;
    List<Etablissement> estabList;
    List<Reviews> reviewsList;





    public SubscribePostAdapter(Context context, List<Etablissement> estabList, List<Reviews> reviewsList) {
        this.context = context;
        this.estabList = estabList;
        this.reviewsList = reviewsList;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_post_cell, viewGroup, false);


        return new myViewHolder(view);
    }

    @Override
public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {

        myViewHolder.subNameUser.setText(reviewsList.get(i).getNameUser());
        myViewHolder.subTime.setText(reviewsList.get(i).getDate());
        myViewHolder.subEstabName.setText(estabList.get(i).getName());
        myViewHolder.subEstabAddress.setText(estabList.get(i).getAddress());

        byte[] imageByteArray2 = Base64.decode(estabList.get(i).getImg1(), Base64.DEFAULT);

        Glide.with(context)
                .load(reviewsList.get(i).getImgUser())                     // Set image url
                //      .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(myViewHolder.subImgUser);
        Glide.with(context)
                .load(imageByteArray2)                     // Set image url
                //      .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(myViewHolder.subEstabImage);
        myViewHolder.SubCommentaire.setText(reviewsList.get(i).getCommmentaire());
        myViewHolder.subEstabQuality.setRating((float)reviewsList.get(i).getQuality());
        myViewHolder.subEstabService.setRating((float)reviewsList.get(i).getService());
        myViewHolder.subImgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckProfile dialog = new CheckProfile();
                dialog.populate(reviewsList.get(i).getIdUser());
                Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();

                dialog.show(((HomeActivity)context).getSupportFragmentManager(),"profiledialog");

            }
        });

        myViewHolder.parentSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",estabList.get(i).getId());

                context.startActivity(intent);
            }
        });

        //   String URLLL = "http://192.168.1.19:3000/likecheck/" + reviewsList.get(i).getId()+"/"+reviewsList.get(i).getIdUser();
        String URLLL = IP.ip+"likecheck/" + reviewsList.get(i).getId() + "/" + reviewsList.get(i).getIdUser();
        JsonObjectRequest jsonObjectRequestt = new JsonObjectRequest(Request.Method.GET, URLLL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            int nbr = Integer.valueOf(String.valueOf(response.get("count")));
                            if (nbr == 0) {
                                myViewHolder.likeButton.setLiked(false);
                            } else {
                                myViewHolder.likeButton.setLiked(true);
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


        //  String URLL = "http://192.168.1.19:3000/nbrlikes/" + reviewsList.get(i).getId();
        String URLL = IP.ip+"nbrlikes/" + reviewsList.get(i).getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("id", reviewsList.get(i).getId());

                            int nbr = Integer.valueOf(String.valueOf(response.get("count")));
                            myViewHolder.nbrLikes.setText(String.valueOf(nbr) + " Likes");
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

        myViewHolder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
                //  String URL = "http://192.168.1.19:3000/like";
                String URL = IP.ip+"like";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")) {
                            String s = myViewHolder.nbrLikes.getText().toString().replace(" Likes", "");

                            int a = Integer.valueOf(s) + 1;

                            updateReview(reviewsList.get(i).getId(),a);
                            myViewHolder.nbrLikes.setText(a + " Likes");
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
                //  String URL = "http://192.168.1.19:3000/unlike";
                String URL = IP.ip+"unlike";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")) {
                            String s = myViewHolder.nbrLikes.getText().toString().replace(" Likes", "");
                            int a = Integer.valueOf(s) - 1;
                            updateReview(reviewsList.get(i).getId(),a);
                            myViewHolder.nbrLikes.setText(a + " Likes");
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



        }
    private void updateReview(String id,int nbrlikes) {




        final String URL = IP.ip+"updatereview";
        //  final String URL = "http://192.168.1.19:3000/updateetab";

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

@Override
public int getItemCount() {
        return estabList.size();
        }

static class myViewHolder extends RecyclerView.ViewHolder {

    PorterCircularImageView subImgUser;
    TextView subNameUser;
    TextView subTime;
    TextView subEstabName;
    TextView subEstabAddress;
    RatingBar subEstabQuality;
    RatingBar subEstabService;
    TextView SubCommentaire;
    RoundedImageView subEstabImage;
    TextView nbrLikes;
    LikeButton likeButton;
    ConstraintLayout parentSub;


    myViewHolder(@NonNull View itemView) {
        super(itemView);
         subImgUser = itemView.findViewById(R.id.subImgUser);
         likeButton = itemView.findViewById(R.id.like_button1);
         parentSub = itemView.findViewById(R.id.parentSub);
         subNameUser  = itemView.findViewById(R.id.subNameUser);
         subTime =  itemView.findViewById(R.id.subTime);
         subEstabName  = itemView.findViewById(R.id.subNameEstab);
         subEstabAddress  = itemView.findViewById(R.id.subAddressEstab);
         subEstabQuality   = itemView.findViewById(R.id.subQualityEstab);
         subEstabService  = itemView.findViewById(R.id.subServiceEstab);
         SubCommentaire  = itemView.findViewById(R.id.subCommentaire);
         subEstabImage  = itemView.findViewById(R.id.subImgEstab);
         nbrLikes = itemView.findViewById(R.id.nbr_likes1);
    }
}
}
