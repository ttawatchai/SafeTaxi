package com.example.zipper.safetaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText id;
    private  EditText  pass;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id = (EditText) findViewById(R.id.id);
        pass =(EditText)findViewById(R.id.pass);
        firebaseAuth =FirebaseAuth.getInstance();
    }
    public void onAuth(View v){
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait ...","Processing...",true);

        (firebaseAuth.signInWithEmailAndPassword(id.getText().toString(),pass.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Login successful", Toast.LENGTH_LONG).show();
                            /*Intent i =new Intent(LoginActivity.this, MainActivity.class);*/
                            /*test*/
                            Intent i =new Intent(LoginActivity.this, HistoryActivity.class);
                            i.putExtra("UID", firebaseAuth.getCurrentUser().getUid());
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);
                        }else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void regis(View v){
        Intent i =new Intent(LoginActivity.this, RegisActivity.class);
        startActivity(i);
    }

    public void go(View v){
        Intent i =new Intent(LoginActivity.this, MapFriendActivity.class);
        startActivity(i);
    }





}