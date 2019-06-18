package com.esprit.foodwin.utility;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.R;
import com.esprit.foodwin.RestaurantDetails;

public class TopEstablishmentsAdapter extends RecyclerView.Adapter<TopEstablishmentsAdapter.estabViewHolder> {

    List<Etablissement> estabList;
    Context context;

    public TopEstablishmentsAdapter() {

    }

    public TopEstablishmentsAdapter(List<Etablissement> eventList, Context context) {
        this.estabList = eventList;
        this.context = context;

    }

    @NonNull
    @Override
    public estabViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.estab_cell, viewGroup, false);


        return new estabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull estabViewHolder eventsViewHolder, final int i) {
        byte[] imageByteArray1 = Base64.decode(estabList.get(i).getImg1(), Base64.DEFAULT);

        Glide.with(context)
                .load(imageByteArray1)                     // Set image url
                //   .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(eventsViewHolder.image);
      /*  Glide.with(context)
                .load(imageByteArray2)                     // Set image url
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(eventsViewHolder.logo);*/

        eventsViewHolder.name.setText(estabList.get(i).getName());
        //  eventsViewHolder.rating.setRating((float)(estabList.get(i).getQuality()+estabList.get(i).getQuality())/2);
        float rate = (float)((estabList.get(i).getQuality()+estabList.get(i).getService())/2);
        if (rate>=4.5)
        {
            eventsViewHolder.rating.setBackgroundColor(Color.parseColor("#006929"));
        }
        else if (rate <4.5 && rate >=3.5)
        {
            eventsViewHolder.rating.setBackgroundColor(Color.parseColor("#75a11d"));
        }
        else if (rate <3.5 && rate >=2.5)
        {
            eventsViewHolder.rating.setBackgroundColor(Color.parseColor("#afba0e"));
        }
        else if (rate <2.5 && rate >=0.1)
        {
            eventsViewHolder.rating.setBackgroundColor(Color.parseColor("#EED10A"));
        }
        else if (rate==0)
        {
            eventsViewHolder.rating.setBackgroundColor(Color.parseColor("#878787"));
        }
        eventsViewHolder.rating.setText(String.valueOf(rate).substring(0,3));
        Log.d("gafsa8","a"+estabList.get(i).getSubCategory());
        eventsViewHolder.subCat.setText(estabList.get(i).getSubCategory() + " - " + estabList.get(i).getAddress());
        eventsViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",estabList.get(i).getId());

                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {

        return estabList.size();

    }

    public static class estabViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView rating;
        //  ImageView logo;
        TextView subCat;
        RelativeLayout parentLayout;

        public estabViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.topEstabImage);
            name = itemView.findViewById(R.id.topEstabName
            );
            // logo = itemView.findViewById(R.id.topEstabLogo);
            subCat = itemView.findViewById(R.id.topEstabSubCat);
            rating = itemView.findViewById(R.id.topEstabRating);
            parentLayout = itemView.findViewById(R.id.estabParentLayout);
        }
    }
}
