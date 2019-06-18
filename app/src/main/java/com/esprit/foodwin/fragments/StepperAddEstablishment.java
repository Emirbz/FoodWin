package com.esprit.foodwin.fragments;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import net.steamcrafted.loadtoast.LoadToast;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cdflynn.android.library.checkview.CheckView;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.GalleryAdapter;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;
import moe.feng.common.stepperview.VerticalStepperItemView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class StepperAddEstablishment extends Fragment {
    private VerticalStepperItemView mSteppers[] = new VerticalStepperItemView[4];
    private Button mNextBtn0, mNextBtn1, mPrevBtn1, mNextBtn2, mPrevBtn2, mPrevBtn3, mNextBtn3;
    ArrayList<String> imagesEtab = new ArrayList();
    ArrayList<String> dataArray = new ArrayList<>();
    public static final String[] data = {};
    private List<String> subCatList;
    public static final String[] openTimeValues = {"09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00"};
    public static final String[] closeTimeValues = {"17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "00:00", "00:30", "01:00"};
    NiceSpinner spinner;
    ArrayAdapter<String> adapter;
    TextView textView6;
    String idsubCat;
    TextInputEditText etabName;
    TextInputLayout etabNameError;
    TextInputLayout etabAddressError;
    TextInputLayout etabPhoneError;
    private static final int PICK_FROM_GALLERY = 1;
    TextInputLayout etabEmailError;
    TextInputEditText etabAddress;
    TextInputEditText etabEmail;
    TextInputEditText etabPhone;
    Spinner openTime;
    Spinner closeTime;
    CheckBox wifi;
    public static final String MY_PREFS_NAME2 = "MAP";
    CheckBox climatisation;
    CheckBox parking;
    NestedScrollView nestedScrollView;
    CheckBox terrace;
    private ToolTipView mRedToolTipView;
    private ToolTipRelativeLayout mToolTipFrameLayout;

    CheckBox debitcard;
    CheckBox alchool;
    Button addPhoto;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;


    private int mActivatedColorRes = R.color.material_blue_500;
    private int mDoneIconRes = R.drawable.ic_done_white_16dp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stepper_add_establishment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mToolTipFrameLayout = view.findViewById(R.id.activity_main_tooltipframelayout);
        ImageView catFood = view.findViewById(R.id.catFood);
        ImageView catDrink = view.findViewById(R.id.catDrink);
        ImageView catCoffee = view.findViewById(R.id.catCoffee);
        spinner = view.findViewById(R.id.spinnerSubCat);
        nestedScrollView = view.findViewById(R.id.addNested);
        textView6 = view.findViewById(R.id.textView6);
        openTime = view.findViewById(R.id.openTime);
        closeTime = view.findViewById(R.id.closeTime);
        etabName = view.findViewById(R.id.etabName);
        etabNameError = view.findViewById(R.id.etabNameError);
        etabPhoneError = view.findViewById(R.id.etabPhoneError);
        etabAddressError = view.findViewById(R.id.etabAddressError);
        etabEmailError = view.findViewById(R.id.etabEmailError);
        etabAddress = view.findViewById(R.id.etabAddress);
        etabEmail = view.findViewById(R.id.etabEmail);
        etabPhone = view.findViewById(R.id.etabPhone);
        etabPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        wifi = view.findViewById(R.id.wifi);
        climatisation = view.findViewById(R.id.climatisation);
        parking = view.findViewById(R.id.parking);
        terrace = view.findViewById(R.id.terrace);
        debitcard = view.findViewById(R.id.debitCard);
        alchool = view.findViewById(R.id.alchool);
        addPhoto = view.findViewById(R.id.etabAddPhoto);
        gvGallery = view.findViewById(R.id.gridView);
        dataArray.add(0, "img1");
        dataArray.add(1, "img2");
        dataArray.add(2, "img3");
        dataArray.add(3, "img4");
        dataArray.add(4, "img5");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, openTimeValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        openTime.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, closeTimeValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        closeTime.setAdapter(adapter2);

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME2, MODE_PRIVATE);

        String addresse = prefs.getString("addresse", "aaaadate");

        etabAddress.setText(addresse);


        final CheckView checkFood = view.findViewById(R.id.checkFood);
        final CheckView checkDrink = view.findViewById(R.id.checkDrink);
        final CheckView checkCoffee = view.findViewById(R.id.checkCoffee);
        mSteppers[0] = view.findViewById(R.id.stepper_0);
        mSteppers[1] = view.findViewById(R.id.stepper_1);
        mSteppers[2] = view.findViewById(R.id.stepper_2);
        mSteppers[3] = view.findViewById(R.id.stepper_3);

        VerticalStepperItemView.bindSteppers(mSteppers);

        mNextBtn0 = view.findViewById(R.id.button_next_0);
        mNextBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppers[0].nextStep();
                if (spinner.getText().toString().length() < 1) {
                    mSteppers[0].setErrorText("Complete your informations");
                } else {
                    mSteppers[0].setErrorText(null);
                }

            }
        });


        mPrevBtn1 = view.findViewById(R.id.button_prev_1);
        mPrevBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppers[1].prevStep();
            }
        });

        mNextBtn1 = view.findViewById(R.id.button_next_1);
        mNextBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppers[1].nextStep();
                if ((etabName.getText().toString().length() < 1) || (etabAddress.getText().toString().length() < 1) || (etabPhone.getText().toString().length() < 1) || (etabEmail.getText().toString().length() < 1) || !(etabEmail.getText().toString().contains("@"))) {
                    mSteppers[1].setErrorText("Complete your informations");
                    if (etabName.getText().toString().length() < 1) {
                        etabNameError.setError("Enter a valid name");
                    } else {
                        etabNameError.setError(null);
                    }
                    if (etabPhone.getText().toString().length() < 1) {
                        etabPhoneError.setError("Enter a valid phone number");
                    } else {
                        etabPhoneError.setError(null);
                    }
                    if (etabAddress.getText().toString().length() < 1) {
                        etabAddressError.setError("Enter a valid address");
                    } else {
                        etabAddressError.setError(null);
                    }
                    if ((etabEmail.getText().toString().length() < 1) || !(etabEmail.getText().toString().contains("@"))) {
                        etabEmailError.setError("Enter a valid Email");
                    } else {
                        etabEmailError.setError(null);
                    }
                } else {


                    mSteppers[1].setErrorText(null);
                }

            }
        });

        mPrevBtn2 = view.findViewById(R.id.button_prev_2);
        mPrevBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppers[2].prevStep();
            }
        });

        mNextBtn2 = view.findViewById(R.id.button_next_2);
        mNextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSteppers[2].nextStep();
            }
        });

        mNextBtn3 = view.findViewById(R.id.button_next_3);
        mNextBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagesEtab.size() != 5) {
                    mSteppers[3].setErrorText("Select 5 images");

                } else {
                    mSteppers[3].setErrorText(null);
                }
                if (mSteppers[0].getErrorText() != null || mSteppers[1].getErrorText() != null || mSteppers[2].getErrorText() != null || mSteppers[3].getErrorText() != null) {
                    Snackbar.make(view, "Check errors", Snackbar.LENGTH_LONG).show();
                } else {
                    PostData();
                }

            }
        });
        mPrevBtn3 = view.findViewById(R.id.button_prev_3);
        mPrevBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSteppers[3].prevStep();
            }
        });

        catFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFood.check();
                checkCoffee.uncheck();
                checkDrink.uncheck();
                Toast.makeText(getContext(), "FOOD", Toast.LENGTH_LONG).show();
                String url = IP.ip+"subcat/1";
                //String url = "http://192.168.1.19:3000/subcat/1";
                getData(url);
                spinner.setVisibility(View.VISIBLE);
                textView6.setVisibility(View.VISIBLE);
            }
        });
        catDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFood.uncheck();
                checkCoffee.uncheck();
                checkDrink.check();
                Toast.makeText(getContext(), "FOOD", Toast.LENGTH_LONG).show();
                String url = IP.ip+"subcat/2";
                //String url = "http://192.168.1.19:3000/subcat/2";
                getData(url);
                spinner.setVisibility(View.VISIBLE);
                textView6.setVisibility(View.VISIBLE);
            }
        });
        catCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFood.uncheck();
                checkCoffee.check();
                checkDrink.uncheck();
                String url = IP.ip+"subcat/3";
                //   String url = "http://192.168.1.19:3000/subcat/3";
                getData(url);
                spinner.setVisibility(View.VISIBLE);
                textView6.setVisibility(View.VISIBLE);


            }
        });
        spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
                }
            }
        });

    }


    private void getData(String url) {


        subCatList = new LinkedList<>(Arrays.asList(""));


        spinner.attachDataSource(subCatList);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);


                        subCatList.add(jsonObject.getString("name"));
                        idsubCat = jsonObject.getString("id");
                        Toast.makeText(getContext(), jsonObject.getString("name"), Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void PostData() {
        nestedScrollView.setAlpha(0.5f);
        final LoadToast lt = new LoadToast(getContext());
        lt.setText(" please wait ...");
        lt.setTranslationY(900);
        lt.show();
        final String URL = IP.ip+"addetab";
        // final String URL = "http://192.168.1.19:3000/addetab";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sirine", response);
                if (response.contains("success")) {

                    lt.success();
                    lt.hide();
                    nestedScrollView.setAlpha(1);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().onBackPressed();
                        }
                    }, 500);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("sawsaw",volleyError.getMessage());
                lt.error();
                lt.hide();
                nestedScrollView.setAlpha(1);


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences loginsharedPref = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                params.put("fk_user_id", loginsharedPref.getString("id", ""));
                for (int i = 0; i < imagesEncodedList.size(); i++) {
                    params.put(dataArray.get(i), imagesEtab.get(i));
                }

                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME2, MODE_PRIVATE);
                String addresse = prefs.getString("addresse", "aaaadate");
                String lat = prefs.getString("lat", "aaaadate");
                String lng = prefs.getString("lng", "aaaadate");
                Log.d("far3ii",lat+""+lng);

                String[] separated = addresse.split(",");


                params.put("address", separated[2]);


                params.put("opentime", openTime.getSelectedItem().toString());
                params.put("closetime", closeTime.getSelectedItem().toString());
                params.put("name", etabName.getText().toString());
                params.put("lat", lat);
                params.put("long", lng);
                params.put("fk_sub_categories_id", getSubCatByName());
                params.put("climatisation", getCheckbox(climatisation));
                params.put("parking", getCheckbox(parking));
                params.put("debitcard", getCheckbox(debitcard));
                params.put("alchool", getCheckbox(alchool));
                params.put("terrace", getCheckbox(terrace));
                params.put("wifi", getCheckbox(wifi));
                params.put("phone", etabPhone.getText().toString());
                params.put("email", etabEmail.getText().toString());
               params.put("region", etabAddress.getText().toString());

                return params;
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(stringRequest);

    }


    private String getSubCatByName() {

        String name;
        name = spinner.getText().toString();
        String url = IP.ip+"subcatname/" + name;
        //String url = "http://192.168.1.19:3000/subcatname/" + name;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);


                        idsubCat = jsonObject.getString("id");


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {



            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
        return idsubCat;
    }

    public String getCheckbox(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            return "yes";
        }
        return "no";
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
                    Cursor cursor = getContext().getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();

                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getActivity().getApplicationContext(), mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(2, gvGallery.getHorizontalSpacing(), 2, 2);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            final InputStream imagestream = getActivity().getApplicationContext().getContentResolver().openInputStream(uri);
                            final Bitmap selectedimage = BitmapFactory.decodeStream(imagestream);
                            String encodedimage = encodeImage(selectedimage);
                            imagesEtab.add(i, encodedimage);
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, filePathColumn, null, null, null);

                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);


                            imageEncoded = cursor.getString(columnIndex);
                            Log.d("dachraouix", "image" + imageEncoded);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getContext(), mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            //    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            //    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("louloumiboun", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(getContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);
        return encImage;
    }

    private void addRedToolTipView(String text, View view) {
        ToolTip toolTip = new ToolTip()
                .withText(text)
                .withColor(getResources().getColor(R.color.material_red_500))
                .withShadow();

        mRedToolTipView = mToolTipFrameLayout.showToolTipForView(toolTip, view);

    }


}


