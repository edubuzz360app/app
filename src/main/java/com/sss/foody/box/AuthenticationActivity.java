package com.sss.foody.box;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class AuthenticationActivity extends AppCompatActivity {
    private EditText mobilenum, otp;
    private EditText name;
    private FirebaseUser cUser;
    private String Verificationcode,username,mobilenumber,loginverification,touse;
    private FirebaseAuth mAuth;
    private Spinner spinner;
    DatePickerDialog datePickerDialog;
    private ProgressBar progressBar;
    private static final String TAG = "Aut";

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        final EditText date;
        setContentView ( R.layout.activity_authentication );


        cUser  = mAuth.getInstance().getCurrentUser();
 /*           if (cUser != null) {

                startActivity(new Intent (AuthenticationActivity.this, splash.class));

            }
*/

        mobilenum = findViewById ( R.id.mobilenumber );
        setupUIViews ();
        otp= findViewById ( R.id.verification );
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance ();
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter ( new ArrayAdapter<String> ( this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));


        findViewById(R.id.sendsmsbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition ()];

                String number = mobilenum.getText ().toString ().trim ();

                if (number.isEmpty () || number.length () < 10) {
                    mobilenum.setError ( "Valid number is required" );
                    mobilenum.requestFocus ();
                    return;
                }

                String phonenumber = "+" + code + number;
                    progressBar.setVisibility(View.VISIBLE);

                    sendsms ( phonenumber );

                }
            }
        });

        findViewById(R.id.verify0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginvalidate()){
                    loginverification = otp.getText ().toString ().trim ();

                    if (loginverification.length() != 6) {
                        otp.setError ( "Please enter valid OTP" );
                        otp.requestFocus ();
                        return;
                    }
                    verifyphonenumber(Verificationcode,loginverification);

                }
            }
        });

        mcallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks () {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String otpcode = phoneAuthCredential.getSmsCode();
                progressBar.setVisibility(View.VISIBLE);
                if (otpcode == null) {
                    otpisnull();
                } else {
                    otp.setText(otpcode);
                    verifyphonenumber(Verificationcode,otpcode);

                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(AuthenticationActivity.this, "Error while SignIn, Contact Customer Support!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    Verificationcode = s;

                Toast.makeText ( getApplicationContext (), "Verification OTP sent.. Check SMS", Toast.LENGTH_LONG ).show ();
                progressBar.setVisibility(View.INVISIBLE);


            }
        };

    }

    private void setupUIViews(){
        name = (EditText)findViewById(R.id.username);
        progressBar = (ProgressBar) findViewById(R.id.progressbarauthentication);

    }
    private Boolean validate(){
        Boolean result = false;

        username = name.getText().toString();
        mobilenumber = mobilenum.getText ().toString ();
        if(username.isEmpty()){
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }

        return result;
    }
    private Boolean loginvalidate(){
        Boolean result = false;

        touse = otp.getText ().toString ();
        if(touse.isEmpty()){
            Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }

        return result;
    }

    public void sendsms(String mobilenumber)
    {
            PhoneAuthProvider.getInstance ().verifyPhoneNumber (
                    mobilenumber,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mcallback
            );

    }
    public void otpisnull(){
        sendUserData();
        Toast.makeText ( getApplicationContext (),"Validated...Signing you In..",Toast.LENGTH_LONG).show ();
        progressBar.setVisibility(View.INVISIBLE);
        startActivity(new Intent (AuthenticationActivity.this, MainActivity.class));
    }
    public void signinWithphone(PhoneAuthCredential credential){
        mAuth.signInWithCredential (credential)
                .addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful ())
                            {
                                sendUserData ();
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();
                                Toast.makeText ( getApplicationContext (),"User SignIn Successful",Toast.LENGTH_LONG).show ();
                                startActivity(new Intent (AuthenticationActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText ( getApplicationContext (),"Please Enter Correct OTP",Toast.LENGTH_LONG).show ();
                            }
                    }
                } );

    }

    public void verifyphonenumber(String actual,String inputcode){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential ( actual,inputcode );
        signinWithphone ( credential );

    }
    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference();
        final String userKey = mAuth.getUid();
        Userprofile userprofile = new Userprofile ( username,mobilenumber);
        myRef.child("users").child(userKey).setValue (userprofile);

    }
}
