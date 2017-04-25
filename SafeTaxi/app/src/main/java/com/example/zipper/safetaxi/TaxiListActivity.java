package com.example.zipper.safetaxi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaxiListActivity extends AppCompatActivity {
    private String Uid,His;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("taxi");
    List<String> name = new ArrayList<>();
    List<String> nameTaxi = new ArrayList<>();
    List<Float> check = new ArrayList<>();
    List<String> travel = new ArrayList<>();
    List<String> commend = new ArrayList<>();

    List<String> Des = new ArrayList<>();
    List<String> set = new ArrayList<String>();
    private RatingBar ratingBarComment;
    TextView txt1;

    double count =0;
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_list);
        Uid = getIntent().getExtras().getString("UID");
        His = getIntent().getExtras().getString("his");
        final DatabaseReference mHis = mUsersRef.child(His);
        DatabaseReference mHis1 = mHis.getRef();
        ratingBarComment = (RatingBar) findViewById(R.id.ratingBarPlaceList);
        txt1 =(TextView)findViewById(R.id.textViewName) ;


        mHis1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterator i = dataSnapshot.getChildren().iterator();


                while (i.hasNext()) {

                    set.add(((DataSnapshot) i.next()).getKey());
                    Log.d("sss", String.valueOf(set));







                }
                name.clear();
                name.addAll(set);


                for(int j=0;j<set.size();j++)
                {
                    DatabaseReference mHis2 = mHis.child(set.get(j));
                    DatabaseReference mHis3 = mHis2.getRef();
                    mHis3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            List<String> setDes = new ArrayList<String>();
                            List<String> setName = new ArrayList<String>();
                            List<String> setComment = new ArrayList<String>();
                            List<String> setCost = new ArrayList<String>();
                            List<Float> setRate = new ArrayList<Float>();
                            Iterator i = dataSnapshot.getChildren().iterator();

                            int count1 = 0;

                            while (i.hasNext()) {

//                                set1.add(String.valueOf(((DataSnapshot) i.next()).getValue()));

//                                    Log.d("sss563", String.valueOf (((DataSnapshot) i.next()).getValue()));
                                setDes.add(String.valueOf(((DataSnapshot) i.next()).getValue()));
                                setName.add(String.valueOf(((DataSnapshot) i.next()).getValue()));
                                setComment.add(String.valueOf(((DataSnapshot) i.next()).getValue()));
                                setCost.add(String.valueOf(((DataSnapshot) i.next()).getValue()));
                                setRate.add(Float.valueOf(((String.valueOf(((DataSnapshot) i.next()).getValue())))));


                            }

                            nameTaxi.addAll(setName);
                            travel.addAll(setDes);
                            check.addAll(setRate);
                            commend.addAll(setComment);
                            count =0;
                            for (int k=0;k<check.size();k++)
                            {
                                count +=check.get(k);

                            }
                            count = count/check.size();
float tmp1= (float) count;
                            ratingBarComment.setRating(0);
                            ratingBarComment.setRating(tmp1);

                            txt1.setText(His);



                            list = (ListView) findViewById(R.id.list1);
                            MyAdapter adapter = new MyAdapter(TaxiListActivity.this, name,travel,nameTaxi,check,commend);
                            list.setAdapter(adapter);


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


            class MyAdapter extends ArrayAdapter {
                Context context;

                List<String> name;
                List<String> nameTaxi;
                List<String> travel;
                List<Float> check;
                List<String> commend;




//                MyAdapter(Context c, List<String> name, List<Double> check, List<String> Des, List<Double> set) {
//                    super( c, R.layout.comment_list, R.id.textViewName, name);
//                    this.context = c;
//                    this.check = check;
//                    this.name = name;
//                    this.Des = Des;
//                    this.set = set;
//
//
//                }

                public MyAdapter(TaxiListActivity c, List<String> name, List<String> travel, List<String> nameTaxi, List<Float> check, List<String> commend) {
                    super( c, R.layout.comment_list,  name);
                    this.context = c;
                    this.check = check;
                    this.name = name;
                    this.nameTaxi = nameTaxi;
                    this.travel = travel;
                    this.commend = commend;





                }


                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = inflater.inflate(R.layout.comment_list, parent, false);
                    TextView myDate = (TextView) row.findViewById(R.id.user);
                    myDate.setText(name.get(position));
                    Log.d("555123", String.valueOf(nameTaxi));
                   TextView myTaxi = (TextView) row.findViewById(R.id.textViewName);
                    myTaxi.setText("คนขับ : "+nameTaxi.get(position));
                    TextView myDes = (TextView) row.findViewById(R.id.travel);
                    myDes.setText("เดินทางไป : "+travel.get(position));
                    RatingBar myRate = (RatingBar)row.findViewById(R.id.ratingBarPlaceList);
                    myRate.setRating(check.get(position));
                    TextView myCommend =(TextView)row.findViewById(R.id.textViewType);
                    myCommend.setText("คำเเนะนำ : "+commend.get(position));





                    return row;


                }
            }
        });
    }
}