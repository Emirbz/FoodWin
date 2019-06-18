package com.esprit.foodwin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

import com.esprit.foodwin.Entity.User;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    public static  GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatTextView signInButton = findViewById(R.id.googleButton);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d("abboura", "Not logged in");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w("abir", "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
    }


    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, HomeActivity.class);

        User user = new User(googleSignInAccount.getDisplayName(), googleSignInAccount.getEmail(), googleSignInAccount.getId());

     if(googleSignInAccount.getPhotoUrl()== null){
         user.setImage("https://cdn2.iconfinder.com/data/icons/ios-7-icons/50/user_male4-512.png");
     }else {
         user.setImage(googleSignInAccount.getPhotoUrl().toString());
     }

        Log.d("mpmpmpmpmp", user.toString());
        SharedPreferences loginsharedPref = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);



        String JSON_URL = IP.ip+"registerx";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("success") || response.contains("exists")) {
                    Log.d("dachraoui1",response);
                    Log.d("dachraoui2",user.toString());

                    SharedPreferences.Editor editor = loginsharedPref.edit();

                    editor.putString("name", user.getName());
                    editor.putString("name", user.getName());
                    editor.putString("email", user.getEmail());
                    editor.putString("id", user.getId());
                    editor.putString("image", user.getImage());
                    editor.apply();

                    editor.putString("grade","Taster").apply();


                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // params.put("id", String.valueOf(user.getId()));


                params.put("name", user.getName());
                params.put("email", user.getEmail());
                params.put("id", user.getId());
                params.put("image", user.getImage());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(stringRequest);


        // Signed in successfully, show authenticated UI.

        startActivity(intent);
        finish();
    }
}


