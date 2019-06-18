package com.esprit.foodwin.utility;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.esprit.foodwin.Entity.Reservation;
import com.esprit.foodwin.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class ProfileReservationsAdapter extends RecyclerView.Adapter<ProfileReservationsAdapter.myViewHolder> {

    Context context;
    List<Reservation> reservations;
    List<String> names;
    List<String> images;


    public ProfileReservationsAdapter(Context context, List<Reservation> reservations,List<String> estabImage,List<String> estabName) {
        this.context = context;
        this.reservations = reservations;
        this.images = estabImage;
        this.names = estabName;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.reseravtions_profile_cell, viewGroup, false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        Log.d("cardib",names.get(i));
        myViewHolder.name.setText(names.get(i));
        myViewHolder.time.setText(reservations.get(i).getTime());
        myViewHolder.date.setText(reservations.get(i).getDate());
        myViewHolder.nbrppl.setText(reservations.get(i).getNbrppl());
        byte[] imageByteArray5 = Base64.decode(images.get(i), Base64.DEFAULT);
        Glide.with(context)
                .load(imageByteArray5)
                .apply(bitmapTransform(new BlurTransformation(10,1))).into(myViewHolder.image);





    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;
        TextView date;
        TextView time;
        TextView nbrppl;


        myViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.estabImageRes);
            name = itemView.findViewById(R.id.estabNameRes);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            nbrppl = itemView.findViewById(R.id.nbrPpl);
        }
    }


}
