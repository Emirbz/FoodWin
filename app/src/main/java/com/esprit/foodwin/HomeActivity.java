package com.esprit.foodwin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.pusher.pushnotifications.PushNotifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.esprit.foodwin.Entity.Etablissement;
import com.esprit.foodwin.fragments.AllEventsFragment;
import com.esprit.foodwin.fragments.FragementHome;
import com.esprit.foodwin.fragments.MyNotifications;
import com.esprit.foodwin.fragments.Profile;
import com.esprit.foodwin.fragments.SubscribesPost;
import com.esprit.foodwin.utility.BottomNavigationViewBehavior;
import com.esprit.foodwin.utility.IP;
import com.esprit.foodwin.utility.MyApplication;

public class HomeActivity extends AppCompatActivity implements MyNotifications.bottom {

    public BottomNavigationView bottomNavigationView;
    private View notificationBadge;
    List<Etablissement> myetabs;
    int countNotif = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //PushNotifications.start(getApplicationContext(), "f53c6aa1-c55a-4c6b-a0b3-599269fcc0d1");


       // PushNotifications.subscribe("loulou");
        myetabs = new ArrayList<>();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.homePageContainer);
        if (fragment == null) {

            getSupportFragmentManager().beginTransaction().add(R.id.homePageContainer, new FragementHome()).commit();
        }


        bottomNavigationView = findViewById(R.id.bottomNavigationHome);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        /////


        // notifyRes();
      /*  addBadge(4);
        addBadge(1);
        addBadge(0);
        addBadge(2);*/
        //  addBadge(3);
        ///////
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.view_all:

                        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, new FragementHome()).commit();
                        myetabs();
                        NbrSun();
                        break;
                    case R.id.view_events:


                        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, new AllEventsFragment()).commit();
                        myetabs();
                        NbrSun();
                        break;
                    case R.id.view_profil:
                        removeBadge3(bottomNavigationView);

                        Fragment fragment2 = Profile.newInstance();

                        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, fragment2).commit();
                        break;
                    case R.id.view_subs:


                        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, new SubscribesPost()).commit();
                        myetabs();
                        NbrSun();
                        break;
                    case R.id.view_notif:


                        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, new MyNotifications()).commit();
                        //   removeBadge4(bottomNavigationView);
                        myetabs();
                        NbrSun();
                        break;
                }
                return true;
            }
        });

//initUI();
    }

    public void addBadge(int i) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);

        notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification, itemView, false);

        itemView.addView(notificationBadge);


    }

/*    private void refreshBadgeView(int i) {


        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);

        //  notificationBadge = LayoutInflater.from(this).inflate(R.layout.view_notification2, itemView, false);
        itemView.removeView(itemView.getChildAt(i));
        //   itemView.addView(notificationBadge);
        //itemView.removeView(itemView.getChildAt(i));


    }*/


    public void removeBadge4() {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(4);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        if (itemView.getChildCount() == 3)
            itemView.removeViewAt(2);
    }

    public void removeBadge1(BottomNavigationView navigationView) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        if (itemView.getChildCount() == 3)
            itemView.removeViewAt(2);
    }

    public void removeBadge3(BottomNavigationView navigationView) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        if (itemView.getChildCount() == 3)
            itemView.removeViewAt(2);
    }

    public void removeBadge2(BottomNavigationView navigationView) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        if (itemView.getChildCount() == 3)
            itemView.removeViewAt(2);
    }

    public void removeBadge0(BottomNavigationView navigationView) {
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        if (itemView.getChildCount() == 3)
            itemView.removeViewAt(2);
    }

    @Override
    public void getbottom() {
        removeBadge4();
    }


    void myetabs() {

        SharedPreferences loginsharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String iduser = loginsharedPref.getString("id", "");
        Log.d("sassy6",iduser);
        String JSON_URL = IP.ip+"myetabsid/" + iduser;
        Log.d("sassy1",JSON_URL);
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
                            JSONArray jsonArray = response.getJSONArray("etabs");
                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //creating a hero object and giving them the values from json object
                                Etablissement item = new Etablissement();

                           /*     item.setId(jsonObject.getInt("id"));


                                //adding the json data to list

                                myetabs.add(item);*/
                                String JSON_URL = IP.ip+"reservationetab/" + jsonObject.getInt("id");
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                try {

                                                    countNotif = countNotif + Integer.valueOf(String.valueOf(response.get("count")));
                                                    Log.d("nouuu",String.valueOf(response.get("count")));
                                                    Log.d("nouuu2",String.valueOf(countNotif));
                                                    if (countNotif != 0) {
                                                        addBadge(3);

                                                    }
                                                    countNotif = 0;
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurred

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,3,                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void NbrSun() {
        SharedPreferences loginsharedPref = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        String iduser = loginsharedPref.getString("id", "");
        Log.d("sassy5",iduser);

        String JSON_URL = IP.ip+"notifcount/" +iduser;
        Log.d("sassy2",JSON_URL);

        //creating a string request to send request to the url
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Integer nbrSub = Integer.valueOf(String.valueOf(response.get("count")));

                            if (nbrSub > 0) {
                                addBadge(4);
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


  /*  private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {

            return true;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_details),
                        Color.parseColor(colors[0]))
                        .title("Heart")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_details),
                        Color.parseColor(colors[1]))
                        .title("Cup")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_details),
                        Color.parseColor(colors[2]))
                        .title("Diploma")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);

        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
        navigationTabBar.setBehaviorEnabled(true);

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, new FragementHome()).commit();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.parent);


    }


    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txt.setText(String.format("Navigation Item #%d", position));
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(final View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
            }
        }
    }


*/



