package com.example.zipper.safetaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisActivity extends AppCompatActivity {
    private EditText txtmail;
    private EditText txtpass;
    private  EditText txtconpass;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);
        txtmail =(EditText) findViewById(R.id.mail);
        txtpass =(EditText) findViewById(R.id.pass);
            firebaseAuth = FirebaseAuth.getInstance();
    }
    public void reg(View v){


            final ProgressDialog progressDialog=ProgressDialog.show(RegisActivity.this,"Please wait...","Processing...",true);
            (firebaseAuth.createUserWithEmailAndPassword(txtmail.getText().toString(),txtpass.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if(task.isSuccessful()){
                        Toast.makeText(RegisActivity.this,"สมัครสมาชิกสำเร็จ",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(RegisActivity.this,LoginActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Log.e("Error",task.getException().toString());
                        Toast.makeText(RegisActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });





    }
}
