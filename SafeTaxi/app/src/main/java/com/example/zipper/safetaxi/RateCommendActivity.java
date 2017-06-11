package com.example.zipper.safetaxi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class RateCommendActivity extends AppCompatActivity {


    private String Uid, His,tmp,tmp1,name,name1,name2;
    private String Pic;
    EditText comment,meter1;
    TextView driver, code;
    private ArrayList<String> list_of_history = new ArrayList<>();
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("History");
    DatabaseReference mTaxiRef = mRootRef.child("taxi");
    DatabaseReference mUserName = mRootRef.child("user");
    private ArrayList<Position> postions = new ArrayList<Position>();
    private ArrayList<History> history = new ArrayList<History>();
    private RatingBar ratingBarComment;
    String taxicode,Des,meter,cost,Driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_commend);
        comment = (EditText) findViewById(R.id.comment);
        meter1 = (EditText) findViewById(R.id.meter);
        ratingBarComment = (RatingBar) findViewById(R.id.ratingBar);

        Uid = getIntent().getExtras().getString("UID");
        Pic = getIntent().getExtras().getString("Pic");
        His = getIntent().getExtras().getString("His");
        DatabaseReference mHis = mUsersRef.child(Uid);
        DatabaseReference mUid = mHis.child("His");
        DatabaseReference mLog = mUid.child(His);
        DatabaseReference mhis = mLog.getRef();

        mhis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {

                    //Log.d("testval", String.valueOf(((DataSnapshot) i.next()).getValue()));
                     taxicode = (String) ((DataSnapshot) i.next()).getValue();
                    Log.d("tazi",taxicode+"456");
                    Des = (String) ((DataSnapshot) i.next()).getValue();
                    Log.d("Data_Des", Des);

                        String rate = (String) ((DataSnapshot) i.next()).getValue();




                    Driver = (String) ((DataSnapshot) i.next()).getValue();
//                    Log.d("Data_Driver", Taxi);

                     cost = (String) ((DataSnapshot) i.next()).getValue();
                    Log.d("Data_Cost", cost);
                    String form = (String) ((DataSnapshot) i.next()).getValue();
                    Log.d("Data_form", form);
                    try {
                        Object location = ((DataSnapshot) i.next()).getValue();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "NO Data", Toast.LENGTH_SHORT).show();

                        break;
                    }
                    String meter = (String) ((DataSnapshot) i.next()).getValue();
                    Log.d("Data_meter", meter);
                    String tell;
                    try {
                        tell = (String) ((DataSnapshot) i.next()).getValue();
                        Log.d("Data_tell", tell);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "No DATA", Toast.LENGTH_SHORT).show();
                        break;
                    }


                    history.add(new History(Des, meter, Driver, cost, taxicode));


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
    public void stop(View v)
    {
        if(comment.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter the text.",Toast.LENGTH_LONG).show();
        }else if(ratingBarComment.getRating()==0) {
            Toast.makeText(getApplicationContext(),"Please set Rating.",Toast.LENGTH_LONG).show();
        }
        else if (meter1.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please enter meter.",Toast.LENGTH_LONG).show();
        }
        else
        {
            DatabaseReference mHis = mUsersRef.child(Uid);
            DatabaseReference mUid = mHis.child("His");
            DatabaseReference mLog = mUid.child(His);
            mLog.child("Rate").setValue(String.valueOf(ratingBarComment.getRating()));
            mLog.child("meter").setValue(meter1.getText().toString());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
            Date date1 = new Date();
            String date = String.valueOf(dateFormat.format(date1));

            DatabaseReference mCode = mTaxiRef.child(taxicode);
        DatabaseReference mTime = mCode.child(date);
            Log.d("data_rate",comment.getText().toString());
        mTime.child("comment").setValue(comment.getText().toString());
        mTime.child("rate").setValue(ratingBarComment.getRating());
        mTime.child("Driver").setValue(Driver);
            mTime.child("meter").setValue(meter1.getText().toString());

            final DatabaseReference mUid2 = mUserName.child(Uid);
            DatabaseReference mfri1 = mUid2.child("Friend");
            DatabaseReference mFri = mfri1.getRef();
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
                        mfri.child(Uid).setValue("Off");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            mTime.child("Des").setValue(Des);
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            i.putExtra("UID",Uid);
            i.putExtra("photo",Pic);
startActivity(i);
        }



    }
}
