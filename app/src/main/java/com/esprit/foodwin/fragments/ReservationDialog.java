package com.esprit.foodwin.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
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
import com.android.volley.toolbox.StringRequest;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esprit.foodwin.R;
import com.esprit.foodwin.utility.AddNotification;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;

public class ReservationDialog extends AppCompatDialogFragment implements Validator.ValidationListener {
    @NotEmpty
    TextView date;
    Spinner time;
    NumberPicker nbr;
    Button book;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private List<String> listtimes = new ArrayList<>();
    public static final String[] times = {"09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
            "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "00:00", "00:30", "01:00"};
    ArrayAdapter<String> adapter;
    String idEtab;
    String idUser;
    String open;
    String close;
    String nameEtab;
    SharedPreferences loginsharedPref;

    String prop;
    String getIdEtab;
    Validator validator;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.reservation_dialog, null);
        loginsharedPref = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.spinnertime);
        nbr = view.findViewById(R.id.number_picker);
        listtimes.addAll(Arrays.asList(times));

        book = view.findViewById(R.id.book);
        validator = new Validator(this);
        validator.setValidationListener(this);
        List<String> list = listtimes.subList(listtimes.indexOf(open), listtimes.indexOf(close));

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adapter);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
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

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        builder.setView(view)
                .setTitle("Book a table");
        return builder.create();
    }


    public void populate(String user, String etab, String op, String cl, String nameEtab, String prop) {
        idUser = user;
        idEtab = etab;
        open = op;
        close = cl;
        this.nameEtab = nameEtab;
        this.prop = prop;
    }

    @Override
    public void onValidationSucceeded() {

        if (nbr.getValue() == 0) {
            Toast.makeText(getContext(), "Number of people must be over 0", Toast.LENGTH_SHORT).show();
        }else {
            book();

        }


    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof TextView) {
                ((TextView) view).setError(message);
                Toast.makeText(getContext(), "Select a date", Toast.LENGTH_SHORT).show();
            }

        }
    }

    void book() {
        String JSON_URL = IP.ip+"book";
        //  String JSON_URL = "http://192.168.1.19:3000/addevent";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")) {
                    dismiss();
                    AddNotification addNotification = new AddNotification();
                    addNotification.postDataNotifications(loginsharedPref.getString("name", ""), "Booked a table in " + nameEtab, loginsharedPref.getString("image", ""), prop);
                    Toast.makeText(getContext(), "Your booking is saved and awaiting confirmation", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("idetab", idEtab);
                params.put("iduser", idUser);
                params.put("date", date.getText().toString());
                params.put("time", time.getSelectedItem().toString());
                params.put("nbr", String.valueOf(nbr.getValue()));

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);
    }
}