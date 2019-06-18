package com.esprit.foodwin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esprit.foodwin.utility.AddNotification;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;

import net.steamcrafted.loadtoast.LoadToast;


public class AddEventActivity extends AppCompatActivity implements Validator.ValidationListener {
    ImageView image;
    @NotEmpty
    EditText name;
    @NotEmpty
    EditText description;
    @NotEmpty
    @Digits(integer = 3, message = "Use only digits")
    EditText fee;
    @NotEmpty
    TextView startTime;
    @NotEmpty
    TextView endTime;
    @NotEmpty
    TextView date;
    ConstraintLayout eventLayout;
    LinearLayout dateHolder;
    LinearLayout startTimeHolder;
    LinearLayout endTimeHolder;
    CheckBox free;
    CheckBox alchool;
    Button addPhoto;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mtimeSetListener;
    int PICK_IMAGE_MULTIPLE = 1;
    List<String> imagesEncodedList;
    String imageEncoded;
    List<String> imagesEtab;
    Validator validator;
    List<String> idUsersEtab;
    List<String> idUsers;
    String idEtab;
    String nameEtab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Toolbar toolbar = findViewById(R.id.toolbar_add);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_dark_arrow);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imagesEtab = new ArrayList();
        idEtab = getIntent().getStringExtra("idetab");
        nameEtab = getIntent().getStringExtra("nameetab");
eventLayout = findViewById(R.id.eventLayout);
        validator = new Validator(this);
        validator.setValidationListener(this);
        idUsersEtab = new ArrayList<>();
        idUsers = new ArrayList<>();
        getEtabSubs(idEtab);

        name = findViewById(R.id.eventName);
        image = findViewById(R.id.eventImage);
        description = findViewById(R.id.eventDescription);
        fee = findViewById(R.id.fee);
        free = findViewById(R.id.free);
        date = findViewById(R.id.eventDate);
        alchool = findViewById(R.id.alchool);
        dateHolder = findViewById(R.id.dateHolder);
        endTime = findViewById(R.id.eventEndTime);
        startTime = findViewById(R.id.eventStartTime);
        startTimeHolder = findViewById(R.id.startTimeHolder);
        endTimeHolder = findViewById(R.id.endTimeHolder);
        addPhoto = findViewById(R.id.eventAddPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
        free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fee.setEnabled(false);
                } else {
                    fee.setEnabled(true);
                }
            }
        });


        startTimeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(startTime);
            }
        });
        endTimeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime(endTime);
            }
        });


        dateHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddEventActivity.this,
                        mDateSetListener,
                        year, month, day);
                dialog.setTitle("Choose a date");
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                datePicker.setMinDate(System.currentTimeMillis() - 1000);
                month = month + 1;
                Log.d("popopopo", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String datee = month + "/" + day + "/" + year;
                date.setText(datee);
            }
        };


    }


    public void showTime(final TextView textView) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textView.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void validate(View view) {
        validator.validate();


    }

    public void saveData() {
        eventLayout.setAlpha(0.5f);
        final LoadToast lt = new LoadToast(this);
        lt.setText(" please wait ...");
        lt.setTranslationY(900);
        lt.show();


        String JSON_URL = IP.ip+"addevent";
        //  String JSON_URL = "http://192.168.1.19:3000/addevent";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success") || response.contains("exists")) {

                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < idUsersEtab.size(); i++) {


                        AddNotification addNotification = new AddNotification();
                        addNotification.postDataNotifications(nameEtab, "Added  a new event : " + name.getText().toString(), imagesEtab.get(0), idUsersEtab.get(i));

                    }
                    lt.success();
                    lt.hide();
                    eventLayout.setAlpha(1);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                        }
                    }, 500);
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
                lt.error();
                lt.hide();
                eventLayout.setAlpha(1);

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", imagesEtab.get(0));
                params.put("name", name.getText().toString());
                params.put("description", description.getText().toString());
                params.put("starttime", startTime.getText().toString());
                params.put("endtime", endTime.getText().toString());
                params.put("date", date.getText().toString());
                if (free.isChecked()) {
                    params.put("fee", "Free");
                } else {
                    params.put("fee", fee.getText().toString());
                }
                SharedPreferences loginsharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                params.put("alchool", String.valueOf(alchool.isChecked()));
                params.put("userid", loginsharedPref.getString("id", ""));
                params.put("etabid", getIntent().getStringExtra("idetab"));

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);
        return encImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    final InputStream imagestream = getApplicationContext().getContentResolver().openInputStream(mImageUri);
                    final Bitmap selectedimage = BitmapFactory.decodeStream(imagestream);
                    String encodedimage = encodeImage(selectedimage);
                    imagesEtab.add(0, encodedimage);


                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();


                    image.setImageURI(mImageUri);
                    image.setScaleType(ImageView.ScaleType.FIT_XY);


                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            final InputStream imagestream = getApplicationContext().getContentResolver().openInputStream(uri);
                            final Bitmap selectedimage = BitmapFactory.decodeStream(imagestream);
                            String encodedimage = encodeImage(selectedimage);
                            imagesEtab.add(i, encodedimage);
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getApplicationContext().getContentResolver().query(uri, filePathColumn, null, null, null);

                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);


                            imageEncoded = cursor.getString(columnIndex);
                            Log.d("dachraouix", "image" + imageEncoded);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();
                            image.setImageURI(uri);
                            image.setScaleType(ImageView.ScaleType.FIT_XY);


                        }
                        Log.v("louloumiboun", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onValidationSucceeded() {
        if (image.getDrawable() == null) {

        } else {
            saveData();
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                if (view.equals(description))
                    ((EditText) view).setError("Please provide a description");
                else if (view.equals(name))
                    ((EditText) view).setError("Please provide a name");
                else
                    ((EditText) view).setError("Please set a price");
            } else if (view instanceof TextView) {
                ((TextView) view).setError(message);
            }
            if(name.getText().toString().trim().equals("")){
                name.setError("Please provide a name");
            }
        }
    }

    private void getEtabSubs(String idEtab) {
        idUsersEtab.clear();


        String JSON_URL = IP.ip+"subs/" + idEtab;
        Log.d("userid1", JSON_URL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            JSONArray jsonArray = response.getJSONArray("subscribe");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object

                                // item.setImage(jsonObject.getString("image"));
                                idUsersEtab.add(jsonObject.getString("fk_user_id"));


                            }


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

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();  //displaying the error in toast if occurred

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void getUsers() {
        idUsers.clear();


        String JSON_URL = IP.ip+"usersx/";
        // String JSON_URL = "http://192.168.1.19:3000/reviews/"+String.valueOf(bundle.getInt("id"));
        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //hiding the progressbar after cogridView.setAdapter(mAdapter);mpletion and showing list view
                        // Showing json data in log monitor

                        try {
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray jsonArray = response.getJSONArray("subscribe");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object

                                // item.setImage(jsonObject.getString("image"));
                                idUsers.add(jsonObject.getString("id"));

                            }


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
}
