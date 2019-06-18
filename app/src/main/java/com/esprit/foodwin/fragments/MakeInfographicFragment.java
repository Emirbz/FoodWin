package com.esprit.foodwin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esprit.foodwin.FirstTime;
import com.esprit.foodwin.LoginActivity;
import com.esprit.foodwin.R;


public class MakeInfographicFragment extends android.support.v4.app.Fragment {
    private FirstTime mSplashScreen;


    public MakeInfographicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_make_infographic, container, false);

        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Store action in preference so that we know when to display info graphics
                startActivity(new Intent(mSplashScreen, LoginActivity.class));
                mSplashScreen.finish();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSplashScreen = (FirstTime) context;
    }
}
