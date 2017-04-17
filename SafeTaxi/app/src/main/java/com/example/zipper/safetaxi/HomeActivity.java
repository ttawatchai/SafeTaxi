package com.example.zipper.safetaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DirectionFinderListener  {
    String origin;
    String destination;
    String latdes,longdes;
    CharSequence originN ;
    CharSequence destinationN ;
    private Button btnFindPath,start;
    LocationListener listener;
    private ProgressDialog progressDialog;
    Route route;
    private String Uid,pic;
    private String txtEmail;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("History");
    private String calMeter;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Uid = getIntent().getExtras().getString("UID");
        pic = getIntent().getExtras().getString("photo");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        txtEmail = getIntent().getExtras().getString("Email");
        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });


        PlaceAutocompleteFragment places= (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setHint("ใส่สถานที่ต้นทาง");
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                origin = place.getId();
                originN= place.getName();
            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getApplicationContext(),status.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        PlaceAutocompleteFragment places1= (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
        places1.setHint("ใส่สถานที่ปลายทาง");
        places1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place1) {

                destination = place1.getId();
                destinationN = place1.getName();


            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getApplicationContext(),status.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_camera) {
            // Handle the camera action
            Toast.makeText(this,"Home" , Toast.LENGTH_SHORT).show();
            Intent in = new Intent(this,HomeActivity.class);
            in.putExtra("UID",Uid);
            startActivity(in);
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this,"history" , Toast.LENGTH_SHORT).show();
            Intent in = new Intent(this,HistoryActivity.class);
            in.putExtra("UID",Uid);
            startActivity(in);

        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this,"friend" , Toast.LENGTH_SHORT).show();
            Intent in = new Intent(this,FriendListActivity.class);
            in.putExtra("UID",Uid);
            startActivity(in);


        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(this,FriendLocationActivity.class);
            in.putExtra("UID",Uid);
            startActivity(in);
        }


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void gogo(View v){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        Date date1 = new Date();
        String date = String.valueOf(dateFormat.format(date1));




    Long tsLong = System.currentTimeMillis()/1000;
    String ts = tsLong.toString();
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        DatabaseReference mUID= mUsersRef.child(Uid);
        DatabaseReference mhis= mUID.child("His");
        final DatabaseReference mHIS= mhis.child(date + originN+"  ไป  "+destinationN);
        mHIS.child("เส้นทางจาก").setValue(originN+"  ไป  "+destinationN);
        mHIS.child("form").setValue(originN);
        mHIS.child("cost").setValue(calMeter);
        mHIS.child("Des").setValue(destinationN);
        mHIS.child("Taxi Driver").setValue("โปรดใส่ชื่อคนขับ");
        mHIS.child("Bus Registration").setValue("โปรดใส่ทะเบียนรถยนต์");
        mHIS.child("meter").setValue("ราคาจริง");
        mHIS.child("Rate").setValue("score");
        intent.putExtra("HIS",Uid+ts);
        intent.putExtra("HIS",date + originN+"  ไป  "+destinationN);
        intent.putExtra("UID",Uid);
        intent.putExtra("origin",origin );
        intent.putExtra("destination",destination );
        intent.putExtra("destinationN",destinationN );
        intent.putExtra("originN",originN );



        startActivity(intent);


    }




    private void sendRequest() {
        if (origin == "") {
            Toast.makeText(this, "กรุณาใส่ที่อยู่ของคุณ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination == "") {
            Toast.makeText(this, "กรุณาใส่จุดปลายทาง!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }



    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "กำลังค้นหา",
                "ค้นหาเส้นทาง", true);


    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        start = (Button) findViewById(R.id.start);
        start.setVisibility(View.VISIBLE);


        for (Route route : routes) {


            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistan)).setText(route.distance.text);
            //calculate  meter***************************************
            float distance_cal = route.distance.value;
            float duration_cal = route.duration.value;
            double time_cal = 0;
            //medium speed 30 km/hr*****
            double speed = 8.33333;
            time_cal=distance_cal/speed;
            double traffic = ((duration_cal-time_cal)/60)*2;

            distance_cal = (distance_cal/1000);

            float cost = 35;
            while (true)
            {
                if(distance_cal<1)
                {
                    break;
                }
                else if(distance_cal>=1 && distance_cal <=10){
                    cost += 5.5;
                    distance_cal = distance_cal-1;

                }
                else if(distance_cal>10 && distance_cal <=20){
                    cost += 6.5;
                    distance_cal = distance_cal-1;


                }
                else if(distance_cal>20 && distance_cal <=40){
                    cost += 7.5;
                    distance_cal = distance_cal-1;


                }
                else if(distance_cal>40 && distance_cal <=60){
                    cost += 8;
                    distance_cal = distance_cal-1;


                }
                else if(distance_cal>60 && distance_cal <=80){
                    cost += 9;
                    distance_cal = distance_cal-1;


                }
                else if(distance_cal>80){
                    cost += 10.5;
                    distance_cal = distance_cal-1;


                }
                else {
                    break;
                }

            }

            double cal = cost + traffic;

            int cost1 = (int) cal;

            if(cost1%2==0)
            {
                cost1=cost1-1;
            }
            int cost2 = cost1+(cost1/4);

            String cost_meter = Integer.toString(cost1);
            String cost_meter1 = Integer.toString(cost2);

            TextView metershow = (TextView)findViewById(R.id.tvCost);
            metershow.setText(cost_meter+"-"+cost_meter1+"บาท");
            calMeter=cost_meter+"-"+cost_meter1+"บาท";
            //end cal....................





        }
    }



}
