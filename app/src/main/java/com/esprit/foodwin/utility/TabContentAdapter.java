package com.esprit.foodwin.utility;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.mask.PorterCircularImageView;

import java.util.List;

import com.esprit.foodwin.R;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.RestaurantDetails;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class TabContentAdapter extends RecyclerView.Adapter<TabContentAdapter.myViewHolder> {

    static Context context;
    List<Etablissement> estabList;


    public TabContentAdapter(Context context, List<Etablissement> estabList) {
        this.context = context;
        this.estabList = estabList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewallbycategorycell, viewGroup, false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, final int i) {

        myViewHolder.name.setText(estabList.get(i).getName());
        myViewHolder.location.setText(estabList.get(i).getRegion());
        myViewHolder.subCat.setText(estabList.get(i).getSubCategory());
        myViewHolder.nbrreview.setText(String.valueOf(estabList.get(i).getNbrRev()));


        float rate = (float) ((estabList.get(i).getQuality() + estabList.get(i).getService()) / 2);
        if (rate >= 4.5) {
            myViewHolder.ratingBar.setBackgroundColor(Color.parseColor("#006929"));
        } else if (rate < 4.5 && rate >= 3.5) {
            myViewHolder.ratingBar.setBackgroundColor(Color.parseColor("#75a11d"));
        } else if (rate < 3.5 && rate >= 2.5) {
            myViewHolder.ratingBar.setBackgroundColor(Color.parseColor("#afba0e"));
        } else if (rate < 2.5 && rate >= 0.1) {
            myViewHolder.ratingBar.setBackgroundColor(Color.parseColor("#EED10A"));
        } else if (rate == 0) {
            myViewHolder.ratingBar.setBackgroundColor(Color.parseColor("#878787"));
        }
        myViewHolder.ratingBar.setText(String.valueOf(rate).substring(0, 3));
        byte[] imageByteArray5 = Base64.decode(estabList.get(i).getImg1(), Base64.DEFAULT);

        Glide.with(context)
                .load(imageByteArray5)
                .apply(bitmapTransform(new BlurTransformation(10, 1))).into(myViewHolder.image);
        //  .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image

        byte[] imageByteArray4 = Base64.decode(estabList.get(i).getImg2(), Base64.DEFAULT);
        Glide.with(context)

                .load(imageByteArray4)                     // Set image url
                // .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(myViewHolder.logo);

        myViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", estabList.get(i).getId());

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return estabList.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        PorterCircularImageView logo;
        TextView name;
        ImageView image;
        TextView subCat;
        TextView location;
        TextView ratingBar;
        TextView nbrreview;
        CardView parent;


        myViewHolder(@NonNull View itemView) {
            super(itemView);
            nbrreview = itemView.findViewById(R.id.nbrReviewAllResto);
            logo = itemView.findViewById(R.id.roundedImageViewEstab);
            image = itemView.findViewById(R.id.estabImage);
            name = itemView.findViewById(R.id.estabName);
            subCat = itemView.findViewById(R.id.estabSubCat);
            location = itemView.findViewById(R.id.estabLocation);
            ratingBar = itemView.findViewById(R.id.estabRating);
            parent = itemView.findViewById(R.id.parentCardview);


        }
    }

    public void filter(List<Etablissement> list) {

        estabList = list;
        notifyDataSetChanged();
    }
}

