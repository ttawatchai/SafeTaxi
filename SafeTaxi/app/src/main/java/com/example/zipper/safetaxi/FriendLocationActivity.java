package com.example.zipper.safetaxi;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.example.zipper.safetaxi.R.id.listView;
import static com.example.zipper.safetaxi.R.id.map;

public class FriendLocationActivity extends AppCompatActivity implements OnMapReadyCallback{


    private String chat_lut,chat_long,tell,driver,code;
    private TextView show,txt1,txt2,txt3;
    private GoogleMap mMap;
    private Double lat,loti;
    private Marker marker;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("user");
    DatabaseReference mHisref = mRootRef.child("History");
    private String Uid,fri,tra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_location);
        Uid = getIntent().getExtras().getString("UID");
        fri = getIntent().getExtras().getString("fri");
        DatabaseReference mHis = mUsersRef.child(fri);
        DatabaseReference mCur = mHis.child("CurrentLocation");
        show = (TextView) findViewById(R.id.latlong_show);
        txt1 = (TextView) findViewById(R.id.tell);
        txt2 = (TextView) findViewById(R.id.driver);
        txt3 = (TextView) findViewById(R.id.bus);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        Log.d("check",fri);
        Log.d("check", String.valueOf(mHis));


        mHis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {


                    Log.d("check1", (String) ((DataSnapshot) i.next()).getKey());
                    Log.d("check2", (String) ((DataSnapshot) i.next()).getKey());
                    Log.d("check3", (String) ((DataSnapshot) i.next()).getValue());
                    tra = (String) ((DataSnapshot) i.next()).getValue();
                    Log.d("check3", tra);
//


                }


                DatabaseReference mHis1 = mHisref.child(fri);
                DatabaseReference mCur2 = mHis1.child("His");

                if(tra != null)
                {
                    DatabaseReference mGo = mCur2.child(tra);
                    mGo.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator i = dataSnapshot.getChildren().iterator();
                             code  = (String) ((DataSnapshot) i.next()).getValue();
                            Log.d("1",code);
                            Log.d("2", (String) ((DataSnapshot) i.next()).getKey());
                            Log.d("3", (String) ((DataSnapshot) i.next()).getKey());
                             driver  = (String) ((DataSnapshot) i.next()).getValue();
                            Log.d("4",driver);
                            Log.d("5", (String) ((DataSnapshot) i.next()).getKey());
                            Log.d("6", (String) ((DataSnapshot) i.next()).getKey());
                            Log.d("7", (String) ((DataSnapshot) i.next()).getKey());
                            Log.d("check8", (String) ((DataSnapshot) i.next()).getKey());
                            tell  = (String) ((DataSnapshot) i.next()).getValue();
                            Log.d("9",tell);


                                    txt1.setText(tell);
                            if(driver == "โปรดใส่ทะเบียนรถยนต์" || driver == "")
                            {
                                txt2.setText("เพื่อนของคุณยังไม่ได้ใส่ข้อมูล");
                            }
                            else
                            {
                                txt2.setText(driver);
                            }


                            try
                            {
                                if(code == "โปรดใส่ทะเบียนรถยนต์" || code == "")
                                {
                                    txt3.setText("เพื่อนของคุณยังไม่ได้ใส่ข้อมูล");
                                }
                                else
                                {
                                    txt3.setText(code);
                                }


                            }
                            catch (Exception e)
                            {

                            }



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        mCur.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();


                chat_lut = (String) ((DataSnapshot) i.next()).getValue();
                chat_long = (String) ((DataSnapshot) i.next()).getValue();

                show.setText("long       " + chat_long + "       " + "lat     " + chat_lut);

                lat = Double.parseDouble(chat_lut);
                loti = Double.parseDouble(chat_long);


                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, loti))
                        .title("My Location").icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                LatLng victory = new LatLng(lat, loti);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(victory, 15));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng victory = new LatLng(13.762779141602536, 100.53704158465575);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(victory, 15));







        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mMap.setMyLocationEnabled(true);


    }





}
