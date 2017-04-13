package com.example.zipper.safetaxi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class MenuActivity extends AppCompatActivity  {
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Uid = getIntent().getExtras().getString("UID");
    }

    public void cal(View v){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("UID",Uid);
        startActivity(intent);


    }
    public void his(View v){
        Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
        intent.putExtra("UID",Uid);
        startActivity(intent);


    }

    public void show(View v)
    {
        Intent intent = new Intent(getApplicationContext(),FriendLocationActivity.class);
        intent.putExtra("UID",Uid);
        startActivity(intent);

    }



}
