package com.example.zipper.yodkapug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dejavuz.yodkapok.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstance();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.contentContainer, MainFragment.newInstance()).commit();
        }
    }
    private void initInstance() {
    }
}


