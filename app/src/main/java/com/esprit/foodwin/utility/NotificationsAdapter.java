package com.esprit.foodwin.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.List;

import com.esprit.foodwin.Entity.Notifications;
import com.esprit.foodwin.R;

public class NotificationsAdapter extends  RecyclerView.Adapter<NotificationsAdapter.myViewHolder>
{

    Context context;
    List<Notifications> estabList;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }






    public NotificationsAdapter(Context context, List<Notifications> estabList) {
        this.context = context;
        this.estabList = estabList;

    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.notifications_cell, viewGroup, false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {

        myViewHolder.text.setText(estabList.get(i).getText());
        myViewHolder.title.setText(estabList.get(i).getTitle());
        myViewHolder.time.setText(estabList.get(i).getDate());

        if (estabList.get(i).getImage().length() > 300)
        {
            byte[] imageByteArray2 = Base64.decode(estabList.get(i).getImage(), Base64.DEFAULT);

        Glide.with(context)
                .load(imageByteArray2)                     // Set image url
                //      .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(myViewHolder.image);
    }
    else
        {


        Glide.with(context)
                .load(estabList.get(i).getImage())                     // Set image url
                //      .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(myViewHolder.image);
        }



    }

    @Override
    public int getItemCount() {
        return estabList.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        CircularImageView image;
        TextView title;
        TextView text;
        TextView time;




        myViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notificationTitle);
            text = itemView.findViewById(R.id.notificationText);
            time = itemView.findViewById(R.id.notificationtTime);
            image = itemView.findViewById(R.id.notificationImg);

        }
    }
}
