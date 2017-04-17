package com.example.zipper.safetaxi;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static com.example.zipper.safetaxi.R.id.map;

/**
 * Created by ZIPPER on 4/9/2017.
 */

public class RateCommentActivity extends Activity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    EditText name,code;
    DatabaseReference mUsersRef = mRootRef.child("History");
    DatabaseReference mTaxisRef = mRootRef.child("taxi");
    private String Uid, hisname,Driver,taxicode,tmp,tmp1;
    private ArrayList<Position> postions = new ArrayList<Position>();
    private ArrayList<History> history = new ArrayList<History>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_comment);
        Uid = getIntent().getExtras().getString("UID");
        hisname = getIntent().getExtras().getString("HIS");

        DatabaseReference mHis = mUsersRef.child(Uid);
        DatabaseReference mUid = mHis.child("His");
        DatabaseReference mLog = mUid.child(hisname);
        DatabaseReference mHs = mLog.child("loc");
        DatabaseReference mhis = mLog.getRef();
        DatabaseReference mLoc = mHs.getRef();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.6),(int)(height *.4));

        mhis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {

                    //Log.d("testval", String.valueOf(((DataSnapshot) i.next()).getValue()));

                    taxicode = (String) ((DataSnapshot) i.next()).getValue();

                    String Des = (String) ((DataSnapshot) i.next()).getValue();

                    String rate = (String) ((DataSnapshot) i.next()).getValue();

                    Driver = (String) ((DataSnapshot) i.next()).getValue();

                    String cost = (String) ((DataSnapshot) i.next()).getValue();

                    String form = (String) ((DataSnapshot) i.next()).getValue();

                    String meter = (String) ((DataSnapshot) i.next()).getValue();





                    try{
                        Object location = ((DataSnapshot) i.next()).getValue();


                    }
                    catch (Exception e)
                    {
//                        Toast.makeText(getApplicationContext(),"NO Route", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    try{
                        String tell = (String) ((DataSnapshot) i.next()).getValue();


                    }
                    catch (Exception e)
                    {
//                        Toast.makeText(getApplicationContext(),"No Save", Toast.LENGTH_SHORT).show();
                        break;
                    }




                    history.add(new History(Des, cost,taxicode,Driver));






                }

                Log.d("Des",Driver);


                name = (EditText) findViewById(R.id.editText);
                tmp=Driver;
                name.setHint(tmp);
// EditText name = (EditText) findViewById(R.id.editText);
                code = (EditText) findViewById(R.id.editText2);
                tmp1=taxicode;
                code.setHint(tmp1);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void save(View v)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        Date date1 = new Date();
        String date = String.valueOf(dateFormat.format(date1));

        tmp = name.getText().toString();
        tmp1 = code.getText().toString();
        DatabaseReference mHis = mUsersRef.child(Uid);
        DatabaseReference mUid = mHis.child("His");
        DatabaseReference mLog = mUid.child(hisname);
        mLog.child("Taxi Driver").setValue(tmp);
        mLog.child("Bus Registration").setValue(tmp1);


        DatabaseReference mCode = mTaxisRef.child(tmp1);
        DatabaseReference mTime = mCode.child(date);
        mTime.child("comment").setValue("No Comment");
        mTime.child("rate").setValue("-");
        mTime.child("Driver").setValue(tmp);

        Toast.makeText(getApplicationContext(),"บันทึกเเสร็จสิ้น", Toast.LENGTH_SHORT).show();
    }

}
