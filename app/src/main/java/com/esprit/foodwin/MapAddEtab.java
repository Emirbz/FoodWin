package com.esprit.foodwin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import com.esprit.foodwin.Map.PlacePicker;


public class MapAddEtab extends AppCompatActivity {

    private static final int REQUEST_CODE = 5678;
    private TextView selectedLocationTextView;
    public static final String MY_PREFS_NAME = "MAP";
    Double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_add_etab);




        Intent myIntent = getIntent(); // gets the previously created intent
        lat = myIntent.getDoubleExtra("lat",0); // will return "FirstKeyValue"
        lng= myIntent.getDoubleExtra("lng",0); // will return "SecondKeyValue"
        Mapbox.getInstance(this, getString(R.string.access_token));
        selectedLocationTextView = findViewById(R.id.selected_location_info_textview);
        goToPickerActivity();
    }



    private void goToPickerActivity() {
        startActivityForResult(
                new PlacePicker.IntentBuilder()
                        .accessToken(getString(R.string.access_token))
                        .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(lat, lng)).zoom(16).build())
                                .build())
                        .build(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
// Show the button and set the OnClickListener()
            Button goToPickerActivityButton = findViewById(R.id.go_to_picker_button);
            goToPickerActivityButton.setVisibility(View.VISIBLE);
            goToPickerActivityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToPickerActivity();
                }
            });
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
// Retrieve the information from the selected location's CarmenFeature
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);

// Set the TextView text to the entire CarmenFeature. The CarmenFeature
// also be parsed through to grab and display certain information such as
// its placeName, text, or coordinates.
            selectedLocationTextView.setText(String.format(
                    getString(R.string.selected_place_info), carmenFeature.toJson()));


            String s=carmenFeature.geometry().toString();
            String requiredString = s.substring(s.indexOf("[") , s.indexOf("}"));
            Log.d("11111111", "onActivityResult: 2222222"+requiredString);
            String lngg = requiredString.substring(requiredString.indexOf("[") + 1, requiredString.indexOf(","));
            String latt = requiredString.substring(requiredString.indexOf(",") + 2, requiredString.indexOf("]"));
            Log.d("11111111", "onActivityResult: 333333 "+latt);
            Log.d("11111111", "onActivityResult: 4444444 "+lngg);



            Log.d("11111111", "onActivityResult: 1111111"+carmenFeature.geometry());

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("addresse", String.valueOf(carmenFeature.placeName()));
            editor.putString("lng", lngg);
            editor.putString("lat", latt);
            editor.apply();
            editor.commit();

            Intent ii = new Intent(getApplicationContext(),AddEstablishment.class);
            startActivity(ii);
            finish();
        }
    }
}
