package com.e_sathiuser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar) ;
        setSupportActionBar(toolbar) ;
        DrawerLayout drawer = findViewById(R.id.drawerlayout ) ;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer , toolbar , R.string.navigation_drawer_open ,
                R.string.navigation_drawer_close ) ;
        drawer.addDrawerListener(toggle) ;
        toggle.syncState() ;
        NavigationView navigationView = findViewById(R.id.navview ) ;
        navigationView.setNavigationItemSelectedListener(this) ;
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,new HomedrawerActivity()).addToBackStack(null).commit();
            //Intent intent=new Intent(this,HomedrawerActivity.class);
            //startActivity(intent);
            navigationView.setCheckedItem(R.id.home);
        }




    }
    @Override
    public void onBackPressed () {
        DrawerLayout drawer = findViewById(R.id.drawerlayout ) ;
        if (drawer.isDrawerOpen(GravityCompat. START )) {
            drawer.closeDrawer(GravityCompat. START ) ;
        } else {
            super .onBackPressed() ;
        }
    }
    @Override
    public boolean onNavigationItemSelected ( @NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId() ;
        switch (id) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomedrawerActivity()).addToBackStack(null).commit();
                // Intent intent=new Intent(this,HomedrawerActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(intent);
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.currentRide:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new CurrentRideFragment()).addToBackStack(null).commit();
                break;
            case R.id.recentRides:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new RecentRideFragment()).addToBackStack(null).commit();
                break;
            case R.id.signOut:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new SignOutFragment()).addToBackStack(null).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new SettingsFragment()).addToBackStack(null).commit();
                break;
            case R.id.Rate:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new RateFragment()).addToBackStack(null).commit();
                break;
            case R.id.Communication:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ContactFragment()).addToBackStack(null).commit();
                break;
            case R.id.aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new AboutFragment()).addToBackStack(null).commit();
                break;
        }
        DrawerLayout drawer = findViewById(R.id. drawerlayout ) ;
        drawer.closeDrawer(GravityCompat. START ) ;


        return true;

    }
}