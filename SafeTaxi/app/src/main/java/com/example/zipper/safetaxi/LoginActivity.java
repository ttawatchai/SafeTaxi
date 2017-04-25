package com.example.zipper.safetaxi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private EditText id;
    private EditText pass;
    private TextView txt;
    private FirebaseAuth firebaseAuth;
    private String Uid;
    private Button btnSignIn, btnSignOut;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("user");
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 420;
    private String name = "-", mail = "-", photo = "-";
    private FirebaseDatabase database;
    private NavigationView navigationView;
    private View navHeaderView;
    private DrawerLayout drawer;

    private int rb = 60, check=0;


    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        id = (EditText) findViewById(R.id.id);
        pass =(EditText)findViewById(R.id.pass);
        firebaseAuth =FirebaseAuth.getInstance();




        initializeControls();
        initializeGPlusSettings();
        database = FirebaseDatabase.getInstance();


//            mGoogleApiClient.disconnect();
            // mGoogleApiClient.connect();

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
                            Intent i =new Intent(LoginActivity.this, HomeActivity.class);
                            i.putExtra("UID", firebaseAuth.getCurrentUser().getUid());
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            Uid = firebaseAuth.getCurrentUser().getEmail();
                           /* DatabaseReference mUID = mUsersRef.child(Uid);
                            mUID.child("Status").setValue("Off");
                            DatabaseReference mCur = mUID.child("current location");
                            mCur.child("lat").setValue("0");
                            mCur.child("long").setValue("0");*/
                            startActivity(i);
                        }else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void regis(View v) {
        Intent i = new Intent(LoginActivity.this, RegisActivity.class);
        startActivity(i);
    }
    public void go(View v){
        Intent y = new Intent(LoginActivity.this, Service.class);
        y.putExtra("UID",name);
        startService(y);
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("rb",rb);
        i.putExtra("UID",name);
        i.putExtra("email",mail);
        i.putExtra("name",name);
        i.putExtra("email",mail);
        i.putExtra("photo",photo);
        DatabaseReference mUID2 = mUsersRef.child(name);
        DatabaseReference mUID3 = mUID2.child("CurrentLocation");
        mUID3.child("lat").setValue("0");
        mUID3.child("long").setValue("0");
        mUID2.child("TravelTo").setValue("-");
        mUID2.child("Status").setValue("Off");
        startActivity(i);
    }
    private void initializeControls(){
        btnSignIn = (Button) findViewById(R.id.google);
        btnSignOut = (Button) findViewById(R.id.closegg);





        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
    }

    private void initializeGPlusSettings(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //check=0;
        Log.d("555","55");
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
        name="-";
        mail="-";
        photo="-";

    }

    private void handleGPlusSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            //Fetch values
            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
//            Uri personPhotoUrl = acct.getPhotoUrl();
            String email = acct.getEmail();
            //String familyName = acct.getFamilyName();
//            Uri imageUrl = acct.getPhotoUrl();



            Log.e(TAG, "Name: " + personName +
                    ", email: " + email +
                    ", Image: " + personPhotoUrl );
            //  ", Family Name: " + familyName);

            //Set values

            name=personName;
            mail=email;
            photo=personPhotoUrl;
//            imgProfilePic.setImageURI(imageUrl);



            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.google:
                signIn();
                Toast.makeText(getApplicationContext(),"log in",Toast.LENGTH_LONG).show();
                check=0;
                break;
            case R.id.closegg:
                signOut();
                Toast.makeText(getApplicationContext(),"log out",Toast.LENGTH_LONG).show();
                Log.d("555","555out");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGPlusSignInResult(result);

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleGPlusSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleGPlusSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            //mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn && check ==0) {
            btnSignIn.setVisibility(View.INVISIBLE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnSignOut.setEnabled(true);

            Intent y = new Intent(LoginActivity.this, Service.class);
            y.putExtra("UID",name);

            startService(y);
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            i.putExtra("rb",rb);
            i.putExtra("UID",name);
            i.putExtra("email",mail);
            i.putExtra("name",name);
            i.putExtra("email",mail);
            i.putExtra("photo",photo);

            DatabaseReference mUID2 = mUsersRef.child(name);
            DatabaseReference mUID3 = mUID2.child("CurrentLocation");
            mUID3.child("latitude").setValue("0");
            mUID3.child("longitude").setValue("0");
            mUID2.child("TravelTo").setValue("-");
            mUID2.child("Status").setValue("Off");
            startActivity(i);



        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);

        }
    }



}