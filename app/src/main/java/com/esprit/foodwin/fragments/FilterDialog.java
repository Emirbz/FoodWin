package com.esprit.foodwin.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.util.List;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.R;

public class FilterDialog extends AppCompatDialogFragment {

    RadioButton rating;
    RadioButton reviews;
    CheckBox wifi;
    CheckBox parking;
    CheckBox terrace;
    CheckBox alcohol;
    CheckBox clim;
    CheckBox card;
    String wi = "no";
    String park = "no";
    String ter = "no";
    String ac = "no";
    String cardd = "no";
    String alc = "no";
    String rat = "no";
    String rev = "no";
    List<Etablissement> etablissements;
    filter listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_dialog, null);

        rating = view.findViewById(R.id.rating);
        reviews = view.findViewById(R.id.reviews);
        wifi = view.findViewById(R.id.wifi);
        parking = view.findViewById(R.id.Parking);
        terrace = view.findViewById(R.id.Terrace);
        alcohol = view.findViewById(R.id.Alcohol);
        clim = view.findViewById(R.id.Ac);
        card = view.findViewById(R.id.debitCard);
        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wi = "yes";
                } else {
                    wi = "no";
                }
            }
        });
        card.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cardd = "yes";
                } else {
                    cardd = "no";
                }
            }
        });
        alcohol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alc = "yes";
                } else {
                    alc = "no";
                }
            }
        });
        parking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    park = "yes";
                } else {
                    park = "no";
                }
            }
        });
        clim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ac = "yes";
                } else {
                    ac = "no";
                }
            }
        });
        terrace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ter = "yes";
                } else {
                    ter = "no";
                }
            }
        });
        rating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rat = "yes";
                } else {
                    rat = "no";
                }
            }
        });
        reviews.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rev="yes";
                }else
                {
                    rev="no";
                }
            }
        });
        builder.setView(view)
                .setTitle("Filter")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.filterr(alc, ac, ter, park, wi, cardd,rat,rev);
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        try {
            listener = (filter) getTargetFragment();
        } catch (ClassCastException e) {
            System.out.println("must implement interface");
        }
        super.onAttach(context);
    }

    public interface filter {
        void filterr(String alc, String ac, String ter, String park, String wi, String cardd,String rat,String rev);
    }
}
