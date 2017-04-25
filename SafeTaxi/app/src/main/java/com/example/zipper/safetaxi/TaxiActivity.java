package com.example.zipper.safetaxi;

import android.content.Context;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TaxiActivity extends AppCompatActivity {
    private String Uid;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("taxi");
    List<String> name = new ArrayList<>();
    List<String> check = new ArrayList<>();
    List<String> set = new ArrayList<String>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi);
        Uid = getIntent().getExtras().getString("UID");
        DatabaseReference mHis = mUsersRef.getRef();


        mHis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterator i = dataSnapshot.getChildren().iterator();
                Iterator j = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {

                    set.add(((DataSnapshot) i.next()).getKey());


                }
                name.clear();
                name.addAll(set);


                Log.d("job", name.size() + "555" + set);

                list = (ListView) findViewById(R.id.list1);
                MyAdapter adapter = new MyAdapter(TaxiActivity.this, name);
                list.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


            class MyAdapter extends ArrayAdapter {
                Context context;
                List<String> check;
                List<String> myname;

                MyAdapter(Context c, List<String> name) {
                    super(c, R.layout.list_taxi, R.id.tell, name);
                    this.context = c;
                    this.check = check;
                    this.myname = name;
                }


                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = inflater.inflate(R.layout.list_taxi, parent, false);

                    TextView myTitle = (TextView) row.findViewById(R.id.textViewName);
                    myTitle.setText(myname.get(position));
                    myTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getApplicationContext(), TaxiListActivity.class);
                            intent.putExtra("Uid", Uid);
                            intent.putExtra("his", myname.get(position));
                            startActivity(intent);

                        }


                    });
                    return row;


                }
            }
        });
    }
}







