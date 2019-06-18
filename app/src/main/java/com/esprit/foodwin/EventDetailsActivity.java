package com.esprit.foodwin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.esprit.foodwin.Entity.Event;
import com.esprit.foodwin.fragments.QrDialog;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import com.wang.avi.AVLoadingIndicatorView;

public class EventDetailsActivity extends AppCompatActivity {

    ImageView eventDetailsImage;
    ImageView backarrow;
    TextView eventDetailsDate;
    TextView eventDetailsName;
    TextView eventStartTime;
    TextView eventEndTime;
    TextView eventEntrance;
    TextView eventEstabName;
    ImageView eventEstabImage;
    TextView eventEstabRegion;
    TextView eventDescription;
    TextView alchool;
    Toolbar toolbar;
    Event item;
    Button participate;
    Button participated;
    ImageView share;
    ConstraintLayout toestab;
    TextView countdown;
    NestedScrollView nestedScrollView;
    FrameLayout frameLayout;
    AVLoadingIndicatorView avLoadingIndicatorView;
    int tst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        backarrow = findViewById(R.id.backarrow);
        eventDescription = findViewById(R.id.eventDescription);
        eventDetailsName = findViewById(R.id.eventDetailsName);
        eventDetailsDate = findViewById(R.id.eventDetailsDate);
        eventDetailsImage = findViewById(R.id.eventDetailsImage);
        eventEntrance = findViewById(R.id.eventEntrance);
        eventStartTime = findViewById(R.id.eventStartTime);
        eventEndTime = findViewById(R.id.eventEndTime);
        eventEstabImage = findViewById(R.id.eventEstabImage);
        eventEstabName = findViewById(R.id.eventEstabName);
        eventEstabRegion = findViewById(R.id.eventEstabRegion);
        alchool = findViewById(R.id.alchool);
        toestab = findViewById(R.id.toestab);
        nestedScrollView = findViewById(R.id.nestedScrollView5);
        frameLayout = findViewById(R.id.frameLayout5);
        avLoadingIndicatorView = findViewById(R.id.progressDetails5);

        Log.d("dachraouia", "aa" + getIntent().getStringExtra("id"));
        countdown = findViewById(R.id.countdown);
        loadeventsList(getIntent().getStringExtra("id"));
        participate = findViewById(R.id.participate);
        participated = findViewById(R.id.participated);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        participated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences loginsharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                generateCode(eventDetailsName.getText().toString(), loginsharedPref.getString("name", ""));
            }
        });
        share = findViewById(R.id.share);
        share.setEnabled(true);
        checkParticipate();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent();
            }
        });

        participate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void check(int t) {
        if (t > 0) {
            participate.setVisibility(View.GONE);
            participated.bringToFront();
        }
    }

    void generateCode(String text, String user) {
        QrDialog dialog = new QrDialog();
        dialog.populate(text, user);
        dialog.show(getSupportFragmentManager(), "qr");

    }

    private void checkParticipate() {
        SharedPreferences loginsharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String JSON_URL = IP.ip+"partcheck/" + getIntent().getStringExtra("id") + "/" + loginsharedPref.getString("id", "");
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            tst = Integer.valueOf(String.valueOf(response.get("msg")));
                            check(tst);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);


    }


    private void loadeventsList(String id) {
        avLoadingIndicatorView.show();
        String JSON_URL = IP.ip+"events/" + id;
        Log.d("jerba", "a" + JSON_URL);
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //hiding the progressbar after completion and showing list view
                        // Showing json data in log monitor
                        Log.d("json", response.toString());
                        try {
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONObject jsonObject = response.getJSONObject("events");
                            //now looping through all the elements of the json array

                            //creating a hero object and giving them the values from json object
                            item = new Event();

                            item.setId(jsonObject.getString("id"));
                            //item.setImage(jsonObject.getString("image"));
                            item.setName(jsonObject.getString("name"));
                            item.setStarttime(jsonObject.getString("starttime"));
                            item.setEndtime(jsonObject.getString("endtime"));
                            item.setDate(jsonObject.getString("date"));
                            item.setAlchool(jsonObject.getString("alchool"));
                            item.setDescription(jsonObject.getString("description"));
                            item.setFee(jsonObject.getString("fee"));
                            Glide.with(getApplicationContext())
                                    .load(jsonObject.getString("image"))                     // Set image url
                                    //  .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                                    .into(eventDetailsImage);
                            //adding the json data to list
                            byte[] imageByteArray2 = Base64.decode(jsonObject.getString("image"), Base64.DEFAULT);
                            Glide.with(getApplicationContext())
                                    .load(imageByteArray2)                     // Set image url
                                    //     .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                                    .into(eventDetailsImage);
                            eventDetailsName.setText(item.getName());
                            eventDetailsDate.setText(item.getDate());
                            eventStartTime.setText(item.getStarttime());
                            eventEndTime.setText(item.getEndtime());

                            if (item.getAlchool().equals("true")) {
                                alchool.setText("Permitted");
                                alchool.setTextColor(Color.GREEN);
                            } else {
                                alchool.setText("Prohibited");
                            }
                            eventEntrance.setText(item.getFee());
                            eventDescription.setText(item.getDescription());
                            JSONObject etab = jsonObject.getJSONObject("establishment");
                            //ne9es image
                            byte[] imageByteArray3 = Base64.decode(etab.getString("img1"), Base64.DEFAULT);
                            Glide.with(getApplicationContext())
                                    .load(imageByteArray3)                     // Set image url
                                    //     .diskCacheStrategy(DiskCacheStrategy.ALL)   // Cache for image
                                    .into(eventEstabImage);
                            eventEstabName.setText(etab.getString("name"));
                            eventEstabRegion.setText(etab.getString("address"));
                            toestab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Intent intent = new Intent(EventDetailsActivity.this, RestaurantDetails.class);
                                        intent.putExtra("id", Integer.valueOf(jsonObject.getString("fk_establishment_id")));
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });

                            String dateString = eventDetailsDate.getText().toString() + " " + eventStartTime.getText().toString();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm");

                            Date convertedDate = new Date();
                            try {
                                convertedDate = dateFormat.parse(dateString);
                                System.out.println(convertedDate.toString());
                            } catch (ParseException e) {

                                e.printStackTrace();
                            }

                            Calendar c = new GregorianCalendar(Locale.ENGLISH);
                            Date newDate = c.getTime();
                            System.out.println(newDate.toString());

                            long total_millis = convertedDate.getTime() - newDate.getTime();

                            //1000 = 1 second interval
                            CountDownTimer cdt = new CountDownTimer(total_millis, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                                    millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                                    long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                                    millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                                    long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                                    millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                                    countdown.setText(days + " Days " + hours + " Hours " + minutes + " minutes " + seconds + " seconds"); //You can compute the millisUntilFinished on hours/minutes/seconds
                                }

                                @Override
                                public void onFinish() {
                                    countdown.setText("Event Finished!");
                                }
                            };
                            cdt.start();
                            frameLayout.setAlpha(1);
                            nestedScrollView.setAlpha(1);
                            avLoadingIndicatorView.hide();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void saveData() {

        String JSON_URL = IP.ip+"participate";
        final SharedPreferences loginsharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    checkParticipate();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                }  else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("idevent", getIntent().getStringExtra("id"));
                params.put("iduser", loginsharedPref.getString("id", ""));

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


    }

    private void shareContent() {
        View view = getLayoutInflater().inflate(R.layout.shared_image, null);
        ImageView sharedimg = view.findViewById(R.id.sharedImage);
        ConstraintLayout parent = view.findViewById(R.id.sharedParent);
        TextView shareddate = view.findViewById(R.id.sharedDate);
        TextView sharedname = view.findViewById(R.id.sharedName);
        sharedimg.setImageBitmap(getBitmapFromView(eventDetailsImage));
        shareddate.setText(eventDetailsDate.getText().toString());
        sharedname.setText(eventDetailsName.getText().toString());
        ImageView sharedEvent = view.findViewById(R.id.sharedEvent);
        parent.setDrawingCacheEnabled(true);
        // this is the important code :)
        // Without it the view will have a dimension of 0,0 and the bitmap will be null

        parent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        parent.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        parent.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        parent.setDrawingCacheEnabled(false); // clear drawing cache

        sharedEvent.setImageBitmap(b);

        //  Bitmap bitmap =getBitmapFromView(sharedEvent);
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            File file = new File(this.getExternalCacheDir(), "logicchip.png");
            FileOutputStream fOut = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

}
