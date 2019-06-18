package com.esprit.foodwin.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.Entity.Reviews;
import com.esprit.foodwin.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ProfileReviewsAdapter extends RecyclerView.Adapter<ProfileReviewsAdapter.myViewHolder> {

  Context context;
  List<Reviews> reservations;
  List<String> names;
  List<String> images;
  List<String> address;
  List<Double> totalquality;
  List<Double> totalservice;
  List<Integer> idetab;
  List<Integer> nbrreviews;



  public ProfileReviewsAdapter(Context context, List<Reviews> reservations, List<String> estabImage, List<String> estabName,List<String> address,List<Double> totalservice,List<Double> totalquality,List<Integer> nbrreviews,List<Integer> id) {
    this.context = context;
    this.reservations = reservations;
    this.images = estabImage;
    this.names = estabName;
    this.idetab = id;
    this.totalquality = totalquality;
    this.nbrreviews = nbrreviews;
    this.totalservice = totalservice;
    this.address = address;
  }

  @NonNull
  @Override
  public ProfileReviewsAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(context).inflate(R.layout.reviews_profile_cell, viewGroup, false);


    return new myViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ProfileReviewsAdapter.myViewHolder myViewHolder, final int i) {

    myViewHolder.name.setText(names.get(i));
    myViewHolder.address.setText(address.get(i));
    myViewHolder.date.setText(reservations.get(i).getDate().substring(0,10));
    myViewHolder.commentaire.setText(reservations.get(i).getCommmentaire());
    myViewHolder.quality.setRating((float)reservations.get(i).getQuality());
    myViewHolder.service.setRating((float)reservations.get(i).getService());
    byte[] imageByteArray5 = Base64.decode(images.get(i), Base64.DEFAULT);
    Glide.with(context)
            .load(imageByteArray5)
            .apply(bitmapTransform(new BlurTransformation(10,1))).into(myViewHolder.image);

    final Reviews reviews = new Reviews();
    reviews.setId(reservations.get(i).getId());
    reviews.setQuality(reservations.get(i).getQuality());
    reviews.setService(reservations.get(i).getService());

    final Etablissement etablissement = new Etablissement();
    etablissement.setTotalquality(totalquality.get(i));
    etablissement.setTotalservice(totalservice.get(i));
    etablissement.setId(idetab.get(i));
    etablissement.setNbrreviews(nbrreviews.get(i));



    myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this review!")
                .setCancelText("No,cancel!")
                .setConfirmText("Yes,delete it!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                  @Override
                  public void onClick(SweetAlertDialog sDialog) {
                    // reuse previous dialog instance, keep widget user state, reset them if you need
                    sDialog.setTitleText("Cancelled!")
                            .setContentText("Your review  is safe :)")
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
                            .setContentText("Your Review  has been deleted!")
                            .setConfirmText("OK")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    DeleteReveiew(reviews,context);
                    UpdateEtabReviews(etablissement,reviews);

                    totalquality.set(i,etablissement.getTotalquality() - reviews.getQuality());



                    totalservice.set(i,etablissement.getTotalservice() - reviews.getQuality());
                    nbrreviews.set(i,etablissement.getNbrreviews()-1);
                    reservations.remove(i);
                    notifyDataSetChanged();







                  }

                })
                .show();
      }
    });


  }

  @Override
  public int getItemCount() {
    return reservations.size();
  }

  static class myViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView address;
    ImageView image;
    TextView date;
    TextView commentaire;
    AppCompatRatingBar quality;
    AppCompatRatingBar service;
    ImageView delete;


    myViewHolder(@NonNull View itemView) {
      super(itemView);
      delete = itemView.findViewById(R.id.deleteReview);
      image = itemView.findViewById(R.id.estabImageRev);
      name = itemView.findViewById(R.id.estabNameRev);
      date = itemView.findViewById(R.id.estabDateRev);
      address = itemView.findViewById(R.id.estabAddressRev);
      commentaire = itemView.findViewById(R.id.estabCommentaireRev);
      quality = itemView.findViewById(R.id.estabQualityRev);
      service = itemView.findViewById(R.id.estabServiceRev);
    }
  }

  private void UpdateEtabReviews(final Etablissement etablissement, final Reviews reviews) {


    final String URL = IP.ip+"updateetab";
    //  final String URL = "http://192.168.1.19:3000/updateetab";

    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        if (response.contains("success")) {
          //  reviewQuality.setRating((float) (etablissement.getTotalquality() + reviews.getQuality()) / (nbrReviews + 1));
          //     reviewService.setRating((float) (etablissement.getTotalservice() + reviews.getQuality()) / (nbrReviews + 1));


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
        params.put("id", String.valueOf(etablissement.getId()));
        if (etablissement.getNbrreviews() == 1)
        {
          params.put("totalquality", "0");
          params.put("nbrreviews", "0");
          params.put("totalservice", "0");
          params.put("quality", "0");
          params.put("service", "0");


        }
        else {
          params.put("totalqualinoty", String.valueOf(etablissement.getTotalquality() - reviews.getQuality()));
          params.put("nbrreviews", String.valueOf(etablissement.getNbrreviews() - 1));
          params.put("totalservice", String.valueOf(etablissement.getTotalservice() - reviews.getService()));
          params.put("quality", String.valueOf((etablissement.getTotalquality() - reviews.getQuality()) / (etablissement.getNbrreviews() - 1)));
          params.put("service", String.valueOf((etablissement.getTotalservice() - reviews.getService()) / (etablissement.getNbrreviews() - 1)));
        }

        return params;
      }
    };

    stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


  }
  public void DeleteReveiew(final Reviews reviews,final Context context) {



    StringRequest stringRequest = new StringRequest(Request.Method.POST, IP.ip+"removereview", new Response.Listener<String>() {
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
        params.put("id", reviews.getId());



        return params;
      }
    };

    stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);

  }


}

