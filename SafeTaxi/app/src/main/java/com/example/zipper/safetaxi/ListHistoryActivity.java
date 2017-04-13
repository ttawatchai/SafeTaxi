package com.example.zipper.safetaxi;

import android.*;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Modules.DirectionFinder;
import Modules.Location;
import Modules.SnapToRoad;
import Modules.SnapToRoadListener;

import static com.example.zipper.safetaxi.R.id.map;


public class ListHistoryActivity extends AppCompatActivity implements SnapToRoadListener,OnMapReadyCallback {

    private EditText historyname;
    private ListView listView;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    public static String txt;
    private GoogleMap mMap;
public  Button btnFindPath;
    private ArrayList<String> list_of_history = new ArrayList<>();
    private String name;
    private ProgressDialog progressDialog;
    private List<Polyline> polylinePaths = new ArrayList<>();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("History");
    private String Uid, hisname;
    private ArrayList<Position> postions = new ArrayList<Position>();
    PolylineOptions polylineOptions = new PolylineOptions();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history);
        Uid = getIntent().getExtras().getString("UID");
        hisname = getIntent().getExtras().getString("hisname");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        DatabaseReference mHis = mUsersRef.child(Uid);
        DatabaseReference mUid = mHis.child("His");
        DatabaseReference mLog = mUid.child(hisname);
        DatabaseReference mHs = mLog.child("loc");
        DatabaseReference mLoc = mHs.getRef();
        historyname = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_history);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        mLoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {

                    String lat = (String) ((DataSnapshot) i.next()).getValue();
                    String lng = (String) ((DataSnapshot) i.next()).getValue();
                    postions.add(new Position(lat, lng));

                }
                txt = "";
                int count = postions.size();
                for (int a = 0; a < postions.size(); a++) {
                    if (a == postions.size() - 1) {
                        txt += postions.get(a).latitude + "," + postions.get(a).longtitde;
                        Log.d("Position OBJ: ", txt);

                    } else {

                        txt += postions.get(a).latitude + "," + postions.get(a).longtitde + "|";
                        Log.d("Position OBJ: ", txt);
                    }
                }
                txt += "&interpolate=true&key=AIzaSyCaQQKFYXOWltKhlQyA-C_DRmniF1BdRig";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void sendRequest() {

        if (txt == "") {
            Toast.makeText(this, "กรุณาใส่จุดปลายทาง!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            new SnapToRoad( this , txt).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        btnFindPath.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onSnapToRoadStart() {
        progressDialog = ProgressDialog.show(this, "กำลังค้นหา",
                "ค้นหาเส้นทาง", true);





    }

    @Override
    public void onSnapToRoadSuccess(List<Location> snap) {
        progressDialog.dismiss();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();



        for( Location location: snap)
        {


                    polylineOptions.geodesic(true).
                    color(Color.BLUE).
                    width(10);


            for (int i = 0; i < location.location.size(); i++) {

                polylineOptions.add(location.location.get(i));


            }
//
            Log.d("aa", String.valueOf(location.location.size()));


            mMap.addPolyline(polylineOptions);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((snap.get(0).location.get(0)), 15));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .position((snap.get(0).location.get(0)))));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .position((snap.get(snap.size()-1).location.get(0)))));


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





