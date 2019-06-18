package com.esprit.foodwin.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.esprit.foodwin.Entity.Reservation;
import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.ConfirmReservationAdapter;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;

public class ConfirmReservation extends AppCompatDialogFragment {
    RecyclerView recyclerView;
    List<Reservation> list = new ArrayList<>();
    String idEtab;
    AVLoadingIndicatorView progressSub;
    String nameEtab;
    private String img;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_confirm_reservation, null);

        recyclerView = view.findViewById(R.id.ResRecyclerView);
        progressSub = view.findViewById(R.id.progressSub);
        loadeventsList(idEtab);


        builder.setView(view)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }


    public void populate(String s,String nameEtab,String img) {
        idEtab = s;
        this.nameEtab = nameEtab;
        this.img = img;
    }


    private void loadeventsList(String user) {
        progressSub.show();
        String JSON_URL = IP.ip+"reservationconfirm/" + idEtab;
        //   String JSON_URL = "http://192.168.1.19:3000/events";
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
                            JSONArray jsonArray = response.getJSONArray("reservation");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                Reservation item = new Reservation();
                                item.setId(jsonObject.getString("id"));
                                item.setDate(jsonObject.getString("date"));
                                item.setTime(jsonObject.getString("time"));
                                item.setNbrppl(jsonObject.getString("nbr"));
                                JSONObject jsonObjectx = jsonObject.getJSONObject("user");
                                item.setUsername(jsonObjectx.getString("name"));
                                item.setUserimage(jsonObjectx.getString("image"));
                                item.setUserId(jsonObject.getString("fk_user_id"));


                                //creating a hero object and giving them the values from json object
                                list.add(item);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(linearLayoutManager);
                            //creating custom adapter object
                            ConfirmReservationAdapter mAdapter = new ConfirmReservationAdapter(getActivity(), list,nameEtab,img);
                            //adding the adapter to list view
                            recyclerView.setAdapter(mAdapter);
                  /*          ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                                @Override
                                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                                    return false;
                                }

                                @Override
                                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                                    mAdapter.delete();
                                }

                                @Override
                                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                                        // Get RecyclerView item from the ViewHolder
                                        View itemView = viewHolder.itemView;

                                        Paint p = new Paint();
                                        if (dX > 0) {
                                            *//* Set your color for positive displacement *//*
                                            // Draw Rect with varying right side, equal to displacement dX
                                            c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                                    (float) itemView.getBottom(), p);
                                        } else {
                                            *//* Set your color for negative displacement *//*
                                         *//*   String text = "\n\n\n Decline";
                                            TextPaint textPaint = new TextPaint();
                                            textPaint.setAntiAlias(true);
                                            textPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
                                            textPaint.setColor(0xFF000000);
                                            int width = (int) textPaint.measureText(text);
                                            StaticLayout staticLayout = new StaticLayout(text, textPaint, (int) width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);
                                            staticLayout.draw(c);*//*
                                            drawDecline(c, p);

                                        }

                                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                                    }
                                }

                            });
                            itemTouchHelper.attachToRecyclerView(recyclerView);
                            ItemTouchHelper itemTouchHelperr = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                                @Override
                                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                                    return false;
                                }

                                @Override
                                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                                    mAdapter.confirm();
                                }
                            });
                            itemTouchHelperr.attachToRecyclerView(recyclerView);*/
                            mAdapter.notifyDataSetChanged();
                            progressSub.hide();

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

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

   /* void drawDecline(Canvas c,Paint p){
        View view = getLayoutInflater().inflate(R.layout.decline, null);
        FrameLayout parent = view.findViewById(R.id.frameparent);


        parent.setDrawingCacheEnabled(true);
        // this is the important code :)
        // Without it the view will have a dimension of 0,0 and the bitmap will be null

        parent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        parent.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
/////////


        /////
        parent.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        parent.setDrawingCacheEnabled(false); // clear drawing cache

        //sharedEvent.setImageBitmap(b);
        c.drawBitmap(b,550,0,p);
    }*/


}
