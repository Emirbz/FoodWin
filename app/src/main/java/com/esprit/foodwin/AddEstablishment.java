package com.esprit.foodwin;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.esprit.foodwin.fragments.StepperAddEstablishment;

public class AddEstablishment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_establishment);
        Toolbar toolbar = findViewById(R.id.toolbarAdd);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();

            }
        });
        Fragment framgent = new StepperAddEstablishment();
        replaceFragment(framgent);

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.add_container, fragment).commit();
    }
}
