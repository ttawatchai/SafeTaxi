package com.example.zipper.safetaxi;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import Modules.ListFriendStructur;

public class FriendListActivity extends AppCompatActivity {
    private String Uid;
    ListView list;
    String[] titles;
    List<String> name = new ArrayList<>();
    String[] description;
    int[] imgs = {R.drawable.cast_expanded_controller_seekbar_thumb};
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
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {
                    //Log.d("name",((DataSnapshot) i.next()).getKey());
                    set.add(((DataSnapshot) i.next()).getKey());


                }
                name.clear();
                name.addAll(set);




                list = (ListView) findViewById(R.id.list1);
                MyAdapter adapter = new MyAdapter(FriendListActivity.this, name, imgs);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


            class MyAdapter extends ArrayAdapter {
                Context context;
                int[] images;
                List<String> myname;

                MyAdapter( Context c, List<String> name, int imgs[]) {
                    super( c, R.layout.low, R.id.text1, name);
                    this.context = c;
                    this.images = imgs;
                    this.myname = name;
                }


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = inflater.inflate(R.layout.low, parent, false);
                    ImageView myImage = (ImageView) row.findViewById(R.id.icon);
                    TextView myTitle = (TextView) row.findViewById(R.id.text1);

                    myTitle.setText(myname.get(position));
                    return row;
                }
            }


        });
    }
}