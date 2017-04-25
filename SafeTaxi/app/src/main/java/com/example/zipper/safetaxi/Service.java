package com.example.zipper.safetaxi;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ZIPPER on 4/18/2017.
 */

public class Service extends android.app.Service {
int count=0;
    private String Uid,name,status,state;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("user");

    private ArrayList<StatusFriend> statusFriend = new ArrayList<StatusFriend>();
    List<String> set3 = new ArrayList<>();
//    List<String> name = new ArrayList<>();
//    List<String> check = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        try
        {
            Uid = intent.getExtras().getString("UID");


        }
        catch (Exception e)
        {
            onDestroy();
        }


        DatabaseReference mHis = mUsersRef.child(Uid);
        final DatabaseReference mUid = mHis.child("Friend");
        final DatabaseReference mLog = mUid.getRef();

        mLog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                final List<String> set = new ArrayList<String>();
                 final List<String> set1 = new ArrayList<String>();


                Iterator i = dataSnapshot.getChildren().iterator();
                final Iterator j = dataSnapshot.getChildren().iterator();

                while (i.hasNext()) {

                    set.add(((DataSnapshot) i.next()).getKey());



                }

                while (j.hasNext()) {

                    set1.add(String.valueOf(((DataSnapshot) j.next()).getValue()));


                }

                statusFriend.add(new StatusFriend(set, set1));
                Log.d("asdfg", String.valueOf(set));
                Log.d("asdfg", String.valueOf(set1));
                for(int count=0;count<set1.size();count++)
                {
                    Log.d("count",set1.get(count));
                    String tmp = set1.get(count);
                    tmp.trim();
                    String tmp1 = "On";
                    tmp1.trim();
                    String tmp2 = "Off";
                    tmp2.trim();
                    String tmp3 = "Near";
                    tmp3.trim();
                    String tmp4 = "Alert";
                    tmp4.trim();
                    if(tmp.equals(tmp1) )
                    { NotificationCompat.Builder notification1 = new NotificationCompat.Builder(Service.this)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                            .setVibrate(new long[]{5000})
                            .setContentTitle("Safe Taxi")
                            .setContentText("เพื่อนของคุณกำลังเดินทางด้วยเเท๊กซี่")
                            .setTicker("เพื่อนของคุณกำลังเดินทางด้วยเเท๊กซี่")
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.safe_taxi_logo);
                        final Intent more1 = new Intent(Service.this,HomeActivity.class);
                        more1.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP ));
                        more1.setAction(Intent.ACTION_MAIN);
                        more1.addCategory(Intent.CATEGORY_LAUNCHER);
                        PendingIntent pen = PendingIntent.getActivity(Service.this,0,more1,0);
                        notification1.setContentIntent(pen);
                        NotificationManager notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager1.notify(0,notification1.build());
                        notification1.setOngoing(true);
                        notification1.setAutoCancel(true);
                    }
                    else  if(tmp.equals(tmp2) )
                    { NotificationCompat.Builder notification1 = new NotificationCompat.Builder(Service.this)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                            .setVibrate(new long[]{5000})
                            .setContentTitle("Safe Taxi")
                            .setContentText("เพื่อนของคุณเดินทางถึงที่หมายเเล้ว")
                            .setTicker("เพื่อนของคุณเดินทางถึงที่หมายเเล้ว ")
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.safe_taxi_logo);
                        final Intent more1 = new Intent(Service.this,HomeActivity.class);
                        more1.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP ));
                        more1.setAction(Intent.ACTION_MAIN);
                        more1.addCategory(Intent.CATEGORY_LAUNCHER);
                        PendingIntent pen = PendingIntent.getActivity(Service.this,0,more1,0);
                        notification1.setContentIntent(pen);
                        NotificationManager notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager1.notify(0,notification1.build());
                        notification1.setOngoing(true);
                        notification1.setAutoCancel(true);
                    }
                    else  if(tmp.equals(tmp3) )
                    { NotificationCompat.Builder notification1 = new NotificationCompat.Builder(Service.this)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                            .setVibrate(new long[]{5000})
                            .setContentTitle("Safe Taxi")
                            .setContentText("อีก 1 กิโลเมตรเพื่อนของคุณจะถึงที่หมาย")
                            .setTicker("อีก 1 กิลโลเมตรเพื่อนของคุณจะถึงที่หมาย ")
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.safe_taxi_logo);
                        final Intent more1 = new Intent(Service.this,HomeActivity.class);
                        more1.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP ));
                        more1.setAction(Intent.ACTION_MAIN);
                        more1.addCategory(Intent.CATEGORY_LAUNCHER);
                        PendingIntent pen = PendingIntent.getActivity(Service.this,0,more1,0);
                        notification1.setContentIntent(pen);
                        NotificationManager notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager1.notify(0,notification1.build());
                        notification1.setOngoing(true);
                        notification1.setAutoCancel(true);
                    }
                    else  if(tmp.equals(tmp4) )
                    { NotificationCompat.Builder notification1 = new NotificationCompat.Builder(Service.this)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                            .setVibrate(new long[]{50000,100})
                            .setContentTitle("Safe Taxi")
                            .setContentText("ฉุกเฉิน เพื่อนของคุณต้องการให้ติดต่อกลับ ")
                            .setTicker("ฉุกเฉิน เพื่อนของคุณต้องการให้ติดต่อกลับ  ")
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.safe_taxi_logo);
                        final Intent more1 = new Intent(Service.this,HomeActivity.class);
                        more1.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP ));
                        more1.setAction(Intent.ACTION_MAIN);
                        more1.addCategory(Intent.CATEGORY_LAUNCHER);
                        PendingIntent pen = PendingIntent.getActivity(Service.this,0,more1,0);
                        notification1.setContentIntent(pen);
                        NotificationManager notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager1.notify(0,notification1.build());
                        notification1.setOngoing(true);
                        notification1.setAutoCancel(true);
                    }
                }


            }











           ///*////////////////////////////////////////////////////////////











            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









        return START_STICKY;
    }

            @Override
    public void onDestroy(){
        Toast.makeText(this,"Bkk stop",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

}
