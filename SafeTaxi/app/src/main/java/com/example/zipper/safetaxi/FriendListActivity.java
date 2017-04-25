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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        final DatabaseReference mUid = mHis.child("Friend");
        final DatabaseReference mLog = mUid.getRef();

        mLog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                List<String> set= new ArrayList<String>();
                List<String> set1= new ArrayList<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                Iterator j = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {

                    set.add(((DataSnapshot) i.next()).getKey());


                }
                while (j.hasNext()) {

                 set1.add(String.valueOf(((DataSnapshot) j.next()).getValue()));

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
                    super( c, R.layout.low, R.id.tell, name);
                    this.context = c;
                    this.check = check;
                    this.myname = name;
                }


                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = inflater.inflate(R.layout.low, parent, false);
                    final ImageView mySwitch =(ImageView)row.findViewById(R.id.statusFri);

                    String name1 = String.valueOf(check.get(position)).trim();
                    String name2 = "Off" ;
                    name2.trim();
                    String name3 = "On" ;
                    name3.trim();
                    String name4 = "Alert" ;
                    name4.trim();
                    String name5 = "Near" ;
                    name5.trim();
                    ImageView img= (ImageView) findViewById(R.id.statusFri);
                    Log.d("555",name1+name2 );
                    if(name1.equals(name2))
                    {
                        Log.d("55512", String.valueOf(check.get(position)));
                        Log.d("55513", String.valueOf(name.get(position)));

                        Toast.makeText(getApplicationContext(),"off", Toast.LENGTH_SHORT).show();



                        mySwitch.setImageResource(R.drawable.off);

                    }
                    else if(name1.equals(name3) || name1.equals(name4)|| name1.equals(name5))
                    {

                        mySwitch.setImageResource(R.drawable.on);

                        Toast.makeText(getApplicationContext(),"on", Toast.LENGTH_SHORT).show();


                    }


                    TextView myTitle = (TextView) row.findViewById(R.id.tell);
                    myTitle.setText(myname.get(position));
                    myTitle.setOnClickListener(new View.OnClickListener() {
                        String name1 = String.valueOf(check.get(position)).trim();
                        String name3 = "On" ;
                        @Override
                        public void onClick(View v) {
                            name3.trim();
                            if(name1.equals(name3))
                            {
                                Intent intent = new Intent(getApplicationContext(),FriendLocationActivity.class);
                                intent.putExtra("Uid",Uid);
                                intent.putExtra("fri",myname.get(position));
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"เพื่อนของคุณไม่ได้ใช้งานในขณะนี้", Toast.LENGTH_SHORT).show();                            }


                        }
                    });

                    return row;
                }




            }


        });




    }

    public void addFriend(View v)
    {

        Intent i = new Intent(getApplicationContext(),AddFriendActivity.class);
        i.putExtra("UID",Uid);
        startActivity(i);
    }
}