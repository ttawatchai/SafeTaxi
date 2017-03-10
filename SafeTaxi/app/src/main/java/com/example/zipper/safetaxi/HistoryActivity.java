package com.example.zipper.safetaxi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {

    private EditText historyname;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    private ArrayList<String> list_of_history = new ArrayList<>();
    private String name;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("History");
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Uid = getIntent().getExtras().getString("UID");
        DatabaseReference mHis = mUsersRef.child(Uid);
        final DatabaseReference mUid = mHis.getRef();





        /*DatabaseReference UID = FirebaseDatabase.getInstance().getReference().getRoot();*/
        historyname = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list_of_history);
        listView.setAdapter(arrayAdapter);
        mUid.orderByChild("st");

        mUid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                list_of_history.clear();

                list_of_history.addAll(set);


                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),ListHistoryActivity.class);
                intent.putExtra("historyname",((TextView)view).getText().toString() );
                intent.putExtra("UID",Uid);
                startActivity(intent);
            }
        });
    }

}
