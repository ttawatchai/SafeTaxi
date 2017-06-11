package com.example.zipper.safetaxi;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

import static android.app.PendingIntent.*;

import static com.example.zipper.safetaxi.R.id.map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    private String Uid;
    private String HIS;
    private GoogleMap mMap;
    private Button btnFindPath;
    int count = 0,count1=0;
    LocationManager locManage;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("History");
    DatabaseReference mUserName = mRootRef.child("user");
    DatabaseReference mTaxiName = mRootRef.child("taxi");

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    String origin;
    String destination;
    CharSequence originName;
    CharSequence desName;
    private Button bu, fi,pop;
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastLocation;
    private LatLng locationDes,victory1;
    private LocationManager locationManager;
    private android.location.LocationListener listener;
    NotificationManager notificationManager,notificationManager1;
    boolean isNotificActive =false;
    Location temp = new Location(LocationManager.GPS_PROVIDER);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Uid = getIntent().getExtras().getString("UID");
        HIS = getIntent().getExtras().getString("HIS");
        final DatabaseReference mUID = mUsersRef.child(Uid);
        final DatabaseReference mUID1 = mUserName.child(Uid);
        final DatabaseReference mhis = mUID.child("His");
        final DatabaseReference mHIS = mhis.child(HIS);






        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);


        mapFragment.getMapAsync(this);
        Bundle bundle = getIntent().getExtras();
        origin = bundle.getString("origin");
        destination = bundle.getString("destination");
        originName = bundle.getCharSequence("originN");
        desName = bundle.getCharSequence("destinationN");



        TextView originNameshow = (TextView) findViewById(R.id.originName);
        originNameshow.setText(originName);
        TextView desNameshow = (TextView) findViewById(R.id.desName);
        desNameshow.setText(desName);
        pop = (Button) findViewById(R.id.pop);
        bu = (Button) findViewById(R.id.buttongo);
        fi = (Button) findViewById(R.id.Finish);
        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });




//*/*/*/*/*/*
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle("Safe Taxi")
                .setContentText("อีก 1 กิโลเมตรจะถึงจุดหมายของท่าน")
                .setTicker("1 กิโลเมตรก่อนถึงจุดหมาย ")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.safe_taxi_logo);

        final Intent more = new Intent(this,MapsActivity.class);
        more.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP ));
        more.setAction(Intent.ACTION_MAIN);
        more.addCategory(Intent.CATEGORY_LAUNCHER);
//        PendingIntent pen = PendingIntent.getActivity(MapsActivity.this,0,more,0);
//
//
//
//        notification.setContentIntent(pen);
//
//        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0,notification.build());
//        notification.setOngoing(true);
//        notification.setAutoCancel(true);




/*/*//*/*//*///*/

        pop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(MapsActivity.this,PopUp.class);
                intent.putExtra("UID",Uid);
                Log.d("name",Uid);
                intent.putExtra("HIS",HIS);
                startActivity(intent);


            }
        });

        pop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final DatabaseReference mUID = mUserName.child(Uid);
                DatabaseReference mfri = mUID.child("Friend");
                DatabaseReference mFri = mfri.getRef();
                mFri.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        List<String> set = new ArrayList<String>();
                        Iterator i = dataSnapshot.getChildren().iterator();
                        Iterator j = dataSnapshot.getChildren().iterator();

                        while (i.hasNext()) {

                            set.add(((DataSnapshot) i.next()).getKey());
//                    Log.d("job", String.valueOf(((DataSnapshot) i.next()).getValue()));
                        }
                        for (int k = 0; k < set.size(); k++) {


                            final DatabaseReference mUID = mUserName.child(set.get(k));
                            DatabaseReference mfri = mUID.child("Friend");

                            mfri.child(Uid).setValue("Alert");


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//                final NotificationCompat.Builder notification1 = new NotificationCompat.Builder(MapsActivity.this)
//                        .setContentTitle("Safe Taxi")
//                        .setContentText("เพื่อนของของคุณเกิดปัญหาในการเดินทาง ต้องการให้ติดต่อกลับ โดยด่วน")
//                        .setTicker("ฉุกเฉิน เพื่อนของคุณต้องการให้ติดต่อกลับ ")
//                        .setAutoCancel(true)
//                        .setSmallIcon(R.drawable.safe_taxi_logo);
//                final Intent more1 = new Intent(MapsActivity.this,MapsActivity.class);
//                more1.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP ));
//                more1.setAction(Intent.ACTION_MAIN);
//                more1.addCategory(Intent.CATEGORY_LAUNCHER);
//                PendingIntent pen = PendingIntent.getActivity(MapsActivity.this,0,more,0);
//                notification1.setContentIntent(pen);
//                notification1.setContentIntent(pen);
//                notificationManager1 = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager1.notify(0,notification1.build());
//                notification1.setOngoing(true);
//                notification1.setAutoCancel(true);
//
//
//
//
              return false;
            }
        });




//lat
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Uid = getIntent().getExtras().getString("UID");
                HIS = getIntent().getExtras().getString("HIS");
                DatabaseReference mUID = mUsersRef.child(Uid);
                DatabaseReference mhis = mUID.child("His");
                final DatabaseReference mHIS = mhis.child(HIS);
                final DatabaseReference mLoc = mHIS.child("loc");
                final DatabaseReference mUser = mUserName.child(Uid);
                final DatabaseReference mCur = mUser.child("CurrentLocation");

                mUser.child("TravelTo").setValue(HIS);



                //remove previous current location Marker

                if (location != null) {
                    String time = DateFormat.getTimeInstance().format(location.getTime());



                    String lati = Double.toString(location.getLatitude());
                    String loti = Double.toString(location.getLongitude());

                    mLoc.child(time + "," + "lat").setValue(lati);
                    mLoc.child(time + "," + "long").setValue(loti);
                    mCur.child("lat").setValue(lati);
                    mCur.child("long").setValue(loti);



                    if(location.distanceTo(temp)/1000 < 0.75 && count1 == 0)
                    {
                        DatabaseReference mfri = mUID.child("Friend");
                        DatabaseReference mFri = mfri.getRef();
                        mFri.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                List<String> set = new ArrayList<String>();
                                Iterator i = dataSnapshot.getChildren().iterator();
                                Iterator j = dataSnapshot.getChildren().iterator();

                                while (i.hasNext()) {

                                    set.add(((DataSnapshot) i.next()).getKey());
//                    Log.d("job", String.valueOf(((DataSnapshot) i.next()).getValue()));
                                }
                                for (int k = 0; k < set.size(); k++) {


                                    final DatabaseReference mUID = mUserName.child(set.get(k));
                                    DatabaseReference mfri = mUID.child("Friend");

                                    mfri.child(Uid).setValue("Near");


                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Log.d("distanceto", String.valueOf(location.distanceTo(temp)/1000));
                        PendingIntent pen = PendingIntent.getActivity(MapsActivity.this,0,more,0);
                        notification.setContentIntent(pen);
                        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0,notification.build());
                        notification.setOngoing(true);
                        notification.setAutoCancel(true);

                        //check


                        count1++;
                    }


                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();




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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }




        btnFindPath.setVisibility(View.INVISIBLE);
        bu.setVisibility(View.VISIBLE);



    }



    //location
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng victory = new LatLng(13.762779141602536, 100.53704158465575);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(victory, 15));



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "กำลังค้นหา",
                "ค้นหาเส้นทาง", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 15));

            ((TextView) findViewById(R.id.dura)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            //calculate  meter***************************************
            float distance_cal = route.distance.value;
            float duration_cal = route.duration.value;
            double time_cal = 0;
            //medium speed 30 km/hr*****
            double speed = 8.33333;
            time_cal = distance_cal / speed;
            double traffic = ((duration_cal - time_cal) / 60) * 2;

            distance_cal = (distance_cal / 1000);

            float cost = 35;
            while (true) {
                if (distance_cal < 1) {
                    break;
                } else if (distance_cal >= 1 && distance_cal <= 10) {
                    cost += 5.5;
                    distance_cal = distance_cal - 1;

                } else if (distance_cal > 10 && distance_cal <= 20) {
                    cost += 6.5;
                    distance_cal = distance_cal - 1;


                } else if (distance_cal > 20 && distance_cal <= 40) {
                    cost += 7.5;
                    distance_cal = distance_cal - 1;


                } else if (distance_cal > 40 && distance_cal <= 60) {
                    cost += 8;
                    distance_cal = distance_cal - 1;


                } else if (distance_cal > 60 && distance_cal <= 80) {
                    cost += 9;
                    distance_cal = distance_cal - 1;


                } else if (distance_cal > 80) {
                    cost += 10.5;
                    distance_cal = distance_cal - 1;


                } else {
                    break;
                }

            }

            double cal = cost + traffic;

            int cost1 = (int) cal;

            if (cost1 % 2 == 0) {
                cost1 = cost1 - 1;
            }
            int cost2 = cost1 + (cost1 / 4);

            String cost_meter = Integer.toString(cost1);
            String cost_meter1 = Integer.toString(cost2);

            TextView metershow = (TextView) findViewById(R.id.meter);
            metershow.setText(cost_meter + "-" + cost_meter1 + "บาท");
            //end cal....................


            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.endAddress)
                    .position(route.endLocation)));
            Log.d("last", String.valueOf(route.endLocation));
            double lat = route.endLocation.latitude;
            double longi=route.endLocation.longitude;

           temp .setLatitude(lat);
                    temp.setLongitude(longi);




            PolylineOptions polylineOptions = new PolylineOptions().

                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }




    public void stop(View v) {
        final DatabaseReference mTax = mTaxiName.child(Uid);

        Intent i = new Intent(getApplicationContext(),RateCommendActivity.class);
        super.onPause();
        locationManager.removeUpdates(listener);
        locationManager = null;
        i.putExtra("UID",Uid);
        i.putExtra("His",HIS);

        startActivity(i);
    }




            @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }

        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
                bu.setVisibility(View.INVISIBLE);
                fi.setVisibility(View.VISIBLE);
                final DatabaseReference mUID = mUserName.child(Uid);
                DatabaseReference mfri = mUID.child("Friend");
                DatabaseReference mFri = mfri.getRef();
                mFri.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        List<String> set = new ArrayList<String>();
                        Iterator i = dataSnapshot.getChildren().iterator();
                        Iterator j = dataSnapshot.getChildren().iterator();

                        while (i.hasNext()) {

                            set.add(((DataSnapshot) i.next()).getKey());
//                    Log.d("job", String.valueOf(((DataSnapshot) i.next()).getValue()));
                        }
                        for (int k = 0; k < set.size(); k++) {


                                final DatabaseReference mUID = mUserName.child(set.get(k));
                                DatabaseReference mfri = mUID.child("Friend");

                                mfri.child(Uid).setValue("On");


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}



