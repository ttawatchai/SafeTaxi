package com.example.zipper.safetaxi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

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

public class ShowHistoryActivity extends AppCompatActivity {
    private String Uid;
    ListView list;
    String[] titles;
    List<String> name = new ArrayList<>();
    String[] description;
    int[] imgs = {R.drawable.cast_expanded_controller_seekbar_thumb};
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("History");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        Uid = getIntent().getExtras().getString("UID");
        DatabaseReference mHis = mUsersRef.child(Uid);
        DatabaseReference mUid = mHis.child("His");
        final DatabaseReference mLog = mUid.getRef();

        mLog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator i = dataSnapshot.getChildren().iterator();
                   i = dataSnapshot.getChildren().iterator();

                    while (i.hasNext()) {
                        Log.d("name", (String) ((DataSnapshot) i.next()).getValue());



                    }


                }


            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }
}
