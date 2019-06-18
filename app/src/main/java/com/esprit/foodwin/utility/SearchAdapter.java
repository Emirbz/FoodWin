package com.esprit.foodwin.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.R;

public class SearchAdapter extends ArrayAdapter<Etablissement> {
    private List<Etablissement> estabList;
    private List<Etablissement> tempList;
    private List<Etablissement> suggestionList;


    public SearchAdapter(@NonNull Context context, int resource, @NonNull List<Etablissement> objects) {
        super(context, resource, objects);
        estabList = objects;
        tempList = new ArrayList<>(estabList);
        suggestionList = new ArrayList<>();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_raw_layout, parent, false);


        TextView textView = convertView.findViewById(R.id.simple_text);
        TextView textView2 = convertView.findViewById(R.id.simple_text2);
        ImageView imageView = convertView.findViewById(R.id.simple_image);


        Etablissement estab = estabList.get(position);
        byte[] imageByteArray2 = Base64.decode(estab.getImg1(), Base64.DEFAULT);
        Glide.with(getContext())
                .load(imageByteArray2)                     // Set image url
                //     .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                .into(imageView);

        textView.setText(estab.getName() );
        textView2.setText(estab.getAddress() );

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return userFilter;
    }


    Filter userFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Etablissement estab = (Etablissement) resultValue;

            return estab.getName() + "  " + estab.getAddress() ;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                suggestionList.clear();
                constraint = constraint.toString().trim().toLowerCase();

                for (Etablissement estab : tempList) {

                    if (estab.getName().toLowerCase().contains(constraint)
                            || estab.getName().toLowerCase().contains(constraint)
                            || estab.getAddress().toLowerCase().contains(constraint)
                            ) {
                        suggestionList.add(estab);
                    }
                }

                filterResults.count = suggestionList.size();
                filterResults.values = suggestionList;

            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Etablissement> uList = (ArrayList<Etablissement>) results.values;

            if (results != null && results.count > 0) {
                clear();
                for (Etablissement u : uList) {
                    add(u);
                    notifyDataSetChanged();
                }
            }

        }
    };
}
