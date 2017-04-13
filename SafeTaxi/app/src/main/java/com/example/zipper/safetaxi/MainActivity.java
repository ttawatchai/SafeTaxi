package com.example.zipper.safetaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

import static com.example.zipper.safetaxi.R.id.btnFindPath;

public class MainActivity extends AppCompatActivity implements DirectionFinderListener {
    String origin;
    String destination;
    CharSequence originN ;
    CharSequence destinationN ;
    private Button btnFindPath,start;
    LocationListener listener;
    private ProgressDialog progressDialog;
    Route route;
    private String Uid;
    private String txtEmail;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("History");
    private String calMeter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uid = getIntent().getExtras().getString("Email");

        //txtEmail = getIntent().getExtras().getString("Email");
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

public void gogo(View v){
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
    Date date1 = new Date();
    String date = String.valueOf(dateFormat.format(date1));




    /*Long tsLong = System.currentTimeMillis()/1000;
    String ts = tsLong.toString();*/
    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
    DatabaseReference mUID= mUsersRef.child(Uid);
    DatabaseReference mhis= mUID.child("His");
      final DatabaseReference mHIS= mhis.child(date);
    mHIS.child("เส้นทางจาก").setValue(originN+"  ไป  "+destinationN);
    mHIS.child("cost").setValue(calMeter);
   /* intent.putExtra("HIS",Uid+ts);*/
     intent.putExtra("HIS",date);
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
  /************Setting && navigation*****************/
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


}


