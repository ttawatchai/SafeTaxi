package com.example.zipper.safetaxi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FriendListActivity extends AppCompatActivity {
    private String Uid;
    ListView list;
    String[] titles;
    List<String> name = new ArrayList<>();
    List<String> check = new ArrayList<>();
    String[] description;
    private ArrayList<StatusFriend> statuss = new ArrayList<StatusFriend>();


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);


        Uid = getIntent().getExtras().getString("UID");
        DatabaseReference mHis = mUsersRef.child(Uid);
        DatabaseReference mUid = mHis.child("Friend");
        final DatabaseReference mLog = mUid.getRef();

        mLog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Set<String> set = new HashSet<String>();
                Set<String> set1 = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                Iterator j = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {

                    set.add(((DataSnapshot) i.next()).getKey());
//                    Log.d("job", String.valueOf(((DataSnapshot) i.next()).getValue()));


                }
                while (j.hasNext()) {

                 set1.add(String.valueOf(((DataSnapshot) j.next()).getValue()));
//                    String status = (String) ((DataSnapshot) j.next()).getValue();
//                    String on = (String) ((DataSnapshot) j.next()).getValue();
//                    Log.d("job", String.valueOf(((DataSnapshot) j.next()).getValue()));
//                    Log.d("name",status);
//
//
              }


                name.clear();
                name.addAll(set);
                check.clear();
                check.addAll(set1);





                list = (ListView) findViewById(R.id.list1);
                MyAdapter adapter = new MyAdapter(FriendListActivity.this, name, check);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


            class MyAdapter extends ArrayAdapter {
                Context context;
                List<String> check;
                List<String> myname;

                MyAdapter(Context c, List<String> name, List<String> check) {
                    super( c, R.layout.low, R.id.text1, name);
                    this.context = c;
                    this.check = check;
                    this.myname = name;
                }


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = inflater.inflate(R.layout.low, parent, false);

                    TextView myTitle = (TextView) row.findViewById(R.id.text1);
                    myTitle.setText(myname.get(position));

                    return row;
                }




            }


        });



    }
}