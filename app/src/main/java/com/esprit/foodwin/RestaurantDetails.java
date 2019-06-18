package com.esprit.foodwin;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;


import com.esprit.foodwin.fragments.DetailsRestaurant;
import com.esprit.foodwin.fragments.ReviewsProfile;
import com.esprit.foodwin.utility.BottomNavigationViewBehavior;

public class RestaurantDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String phone = intent.getStringExtra("phone");
        final String address = intent.getStringExtra("address");
        final String email = intent.getStringExtra("email");
        final String wifi = intent.getStringExtra("wifi");
        final String climatisation = intent.getStringExtra("climatisation");
        final String terrace = intent.getStringExtra("terrace");
        final String parking = intent.getStringExtra("parking");
        final String alchool = intent.getStringExtra("alchool");
        final String debitcard = intent.getStringExtra("debitcard");
        final String opentime = intent.getStringExtra("opentime");
        final String closetime= intent.getStringExtra("closetime");
        final int id = intent.getIntExtra("id",0);
        final double totalquality = intent.getDoubleExtra("totalquality",0);
        final double totalservice = intent.getDoubleExtra("totalservice",0);
        final double service = intent.getDoubleExtra("service",0);
        final double quality = intent.getDoubleExtra("quality",0);


        setContentView(R.layout.activity_restaurant_details);






        final Bundle bundle = new Bundle();
        bundle.putInt("id",intent.getIntExtra("id",122));

        final Fragment fragmentDetail = DetailsRestaurant.newInstance();
        final Fragment fragmentReviews = new ReviewsProfile();
        fragmentDetail.setArguments(bundle);
        fragmentReviews.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragement_container, fragmentDetail).commit();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.view_details:

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, fragmentDetail).commit();

                        break;
                    case R.id.view_reviews:


                        getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, fragmentReviews).commit();
                        break;
                }
                return true;
            }
        });






    }



}



