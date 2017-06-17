package com.example.parkingApp.parkme.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.parkingApp.parkme.MainActivity;
import com.example.parkingApp.parkme.fragments.MapFragment;
import com.example.parkingApp.parkme.R;
import com.example.parkingApp.parkme.adapters.DrawerListAdapter;
import com.example.parkingApp.parkme.model.NavItem;
import com.example.parkingApp.parkme.servicecall.ApiUtils;
import com.example.parkingApp.parkme.servicecall.ParkingService;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPane;
    private ArrayList<NavItem> mNavItems = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    String pref_userName;
    android.support.v4.app.Fragment fragment;

    TextView username, user_email;
    EditText search;
    ImageView searchIcon;
    ImageView user_picture;
    BottomNavigationView bottomNavigationView;
    JSONObject response, profile_pic_data, profile_pic_url;
    RelativeLayout wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        prepareMenu(mNavItems);

        CharSequence mDrawerTitle;
        mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.navList);

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);


        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);

        // set a custom shadow that overlays the main content when the drawer opens
        //mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        search = (EditText) findViewById(R.id.search);
        searchIcon = (ImageView) findViewById(R.id.seaarchImage);
        wrapper = (RelativeLayout) findViewById(R.id.wrapper);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        ((MapFragment) fragment).setMarkersOnMap();
                        search.setVisibility(View.VISIBLE);
                        searchIcon.setVisibility(View.VISIBLE);
                        break;
                    case R.id.action_schedules:
                        if (fragment instanceof MapFragment)
                            ((MapFragment) fragment).getNearestParking();
                        break;
                    case R.id.action_music:
                        if (fragment instanceof MapFragment)
                            ((MapFragment) fragment).getCheapestParking();
                        break;
                }
                return false;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );
        
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                bottomNavigationView.setVisibility(View.INVISIBLE);
                search.setVisibility(View.INVISIBLE);
                searchIcon.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                bottomNavigationView.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                searchIcon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        if (savedInstanceState == null) {
            selectItemFromDrawer(0);
        }

        MapFragment map = new MapFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, map);
        ft.commit();


        username = (TextView) findViewById(R.id.userName);

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        pref_userName = sharedPreferences.getString("username", "");
        username.setText(pref_userName);

        user_picture = (ImageView) findViewById(R.id.avatar);


        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");
        setUserProfile(jsondata);


        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MapFragment) fragment).searchParking(search.getText().toString());
            }
        });

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(search.getText().toString().equals("")){
                    ((MapFragment) fragment).setMarkersOnMap();
                    return true;
                }else{
                    return false;
                }
            }
        });

        if (this.getIntent().getExtras() != null) {
            Toast.makeText(this, this.getIntent().getExtras().getString("value"), Toast.LENGTH_LONG).show();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void setUserProfile(String jsondata) {
        try {
            response = new JSONObject(jsondata);
            username.setText(response.get("name").toString());
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
            Picasso.with(this).load(profile_pic_url.getString("url"))
                    .into(user_picture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareMenu(ArrayList<NavItem> mNavItems) {
        mNavItems.add(new NavItem(getString(R.string.places), getString(R.string.places_long), R.drawable.ic_nearest_icon));
        mNavItems.add(new NavItem(getString(R.string.preferences), getString(R.string.preferences_long), R.drawable.ic_action_settings));
        mNavItems.add(new NavItem(getString(R.string.about), getString(R.string.about_long), R.drawable.ic_action_about));
        mNavItems.add(new NavItem(getString(R.string.sync_data), getString(R.string.sync_data_long), R.drawable.ic_action_refresh));
        mNavItems.add(new NavItem("Odjava", "Izlogujte se iz aplikacije", R.drawable.ic_action_logout));
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private void selectItemFromDrawer(int position) {
        if (position == 0) {
            /*FragmentTra*//*Intent preference = new Intent(MainActivity.this,ReviewerPreferenceActivity.class);
            startActivity(preference);*//*nsition.to(MyFragment.newInstance(), this, false);*/

        } else if (position == 1) {
            Intent preference = new Intent(MainPageActivity.this, PreferenceActivity.class);
            startActivity(preference);
            //..
        } else if (position == 2) {
            Intent in = new Intent(MainPageActivity.this, AboutActivity.class);
            startActivity(in);

        } else if (position == 3) {
            Intent preference = new Intent(MainPageActivity.this, MyReservationsActivity.class);
            startActivity(preference);
        } else if (position == 4) {
            logout();
            disconnectFromFacebook();
        } else if (position == 5) {
            //...
        } else {
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
        if (position != 5) // za sve osim za sync
        {
            setTitle(mNavItems.get(position).getmTitle());
        }
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    private void logout() {
        Intent log = new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(log);
        finish();
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }
}
