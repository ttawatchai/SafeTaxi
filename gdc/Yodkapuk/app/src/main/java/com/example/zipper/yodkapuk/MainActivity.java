package com.example.zipper.yodkapuk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static class LoginAppActivity extends AppCompatActivity implements  View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

        private static final String TAG = LoginActivity.class.getSimpleName();
        private static final int RC_SIGN_IN = 420;

        private GoogleApiClient mGoogleApiClient;
        private ProgressDialog mProgressDialog;

        private SignInButton btnSignIn;
        private Button btnSignOut , btnAddLocation;
        private LinearLayout llProfileLayout;
        private ImageView imgProfilePic;
        private TextView txtName, txtEmail;
        private FirebaseDatabase database;

        private int rb = 60;
        private String name="-",mail="-",photo="-";
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mUsersRef = mRootRef.child("user");

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login_app);
            Toast.makeText(this, "Welcome.", Toast.LENGTH_LONG).show();

            initializeControls();
            initializeGPlusSettings();
            database = FirebaseDatabase.getInstance();
            //rb = radioGroup.getId();
            Button bt = (Button) findViewById(R.id.buttonSearch);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent y = new Intent(LoginAppActivity.this, Service.class);
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
            });

        }

        private void initializeControls(){
            btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
            btnSignOut = (Button) findViewById(R.id.btn_sign_out);
            llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
            imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
            txtName = (TextView) findViewById(R.id.txtName);
            txtEmail = (TextView) findViewById(R.id.txtEmail);

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
            btnSignIn.setSize(SignInButton.SIZE_STANDARD);
            btnSignIn.setScopes(gso.getScopeArray());
        }


        private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
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
                //String personPhotoUrl = acct.getPhotoUrl().toString();
                Uri personPhotoUrl = acct.getPhotoUrl();
                String email = acct.getEmail();
                //String familyName = acct.getFamilyName();
                //Uri imageUrl = acct.getPhotoUrl();

                if(personPhotoUrl==null){
                    photo="-";
                }else{
                    photo=personPhotoUrl.toString();
                }

                Log.e(TAG, "Name: " + personName +
                        ", email: " + email +
                        ", Image: " + personPhotoUrl );
                //  ", Family Name: " + familyName);

                //Set values
                txtName.setText(personName);
                txtEmail.setText(email);
                name=personName;
                mail=email;
                //photo=personPhotoUrl;
                //imgProfilePic.setImageURI(imageUrl);


                //Set profile pic with the help of Glide
                if(photo!="-") {
                    Glide.with(getApplicationContext()).load(photo)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgProfilePic);
                }
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
                case R.id.btn_sign_in:
                    signIn();
                    break;
                case R.id.btn_sign_out:
                    signOut();
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
            if (isSignedIn) {
                btnSignIn.setVisibility(View.GONE);
                btnSignOut.setVisibility(View.VISIBLE);
                btnSignOut.setEnabled(true);
                llProfileLayout.setVisibility(View.VISIBLE);
            } else {
                btnSignIn.setVisibility(View.VISIBLE);
                btnSignOut.setVisibility(View.GONE);
                llProfileLayout.setVisibility(View.GONE);
            }
        }
    }
}
