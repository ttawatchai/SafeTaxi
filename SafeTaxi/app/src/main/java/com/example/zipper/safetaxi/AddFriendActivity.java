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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddFriendActivity extends AppCompatActivity {

        private String Uid,name1;
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("user");
         private ArrayList<Friend> fri = new ArrayList<Friend>();
    private Button search;
    EditText name;
    TextView namefound;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_friend);
            name = (EditText)findViewById(R.id.nameSearch) ;
            namefound = (TextView)findViewById(R.id.nameFound);

            Uid = getIntent().getExtras().getString("UID");
            DatabaseReference mHis = mUsersRef.getRef();

        search = (Button)findViewById(R.id.button8);
            mHis.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


//                    Set<String> set = new HashSet<String>();
//                    Set<String> set1 = new HashSet<String>();
                    Iterator i = dataSnapshot.getChildren().iterator();
//                    Iterator j = dataSnapshot.getChildren().iterator();

                    while (i.hasNext()) {

                        String name = (((DataSnapshot) i.next()).getKey());
//                              Log.d("job", String.valueOf(((DataSnapshot) i.next()).getKey()));

                        fri.add(new Friend(name));



                    }
                     Log.d("job", String.valueOf(fri.size()));


//                    while (j.hasNext()) {
//
//                        set1.add(String.valueOf(((DataSnapshot) j.next()).getValue()));
////                    String status = (String) ((DataSnapshot) j.next()).getValue();
////                    String on = (String) ((DataSnapshot) j.next()).getValue();
////                    Log.d("job", String.valueOf(((DataSnapshot) j.next()).getValue()));
////                    Log.d("name",status);
////
////
//                    }


                }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }








            });



        }
        public void search(View v)
        {
            int count =0;
            String name3 = Uid;
            name3.trim();
            name1 = name.getText().toString();
            name1.trim();
                Log.d("name_gg",name1);
            for(int y =0 ;y<fri.size();y++)
            {
                Log.d("name_gg1",fri.get(y).Name);
                    String name2 = fri.get(y).Name.trim();
                if(name1.equals(name2)   && count ==0 && name1.equals(name3)  == false )
                {
                    namefound.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    namefound.setText(fri.get(y).Name);
                    count++;
                    Toast.makeText(AddFriendActivity.this, "Found Username", Toast.LENGTH_SHORT).show();

                }


            }
            if(count == 0)
            {
                Toast.makeText(AddFriendActivity.this, "Not Found Username", Toast.LENGTH_SHORT).show();
                namefound.setVisibility(View.INVISIBLE);
                search.setVisibility(View.INVISIBLE);

            }


        }

        public void add(View v)
        {

            String name = namefound.getText().toString();
            DatabaseReference mHis = mUsersRef.child(Uid);
            DatabaseReference mFri = mHis.child("Friend");
            mFri.child(name).setValue("true");
            DatabaseReference mHis1 = mUsersRef.child(name);
            DatabaseReference mFri1 = mHis1.child("Friend");
            mFri1.child(Uid).setValue("true");

            Toast.makeText(AddFriendActivity.this, "คุณได้เป็นเพื่อนกับ"+ name, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),FriendListActivity.class);
            i.putExtra("Uid",Uid);
            finish();



        }

    }


