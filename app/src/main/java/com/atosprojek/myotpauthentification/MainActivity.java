package com.atosprojek.myotpauthentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.atosprojek.myotpauthentification.databinding.MainActivityBinding;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{

    private MainActivityBinding binding;
    DatabaseReference FReff;

//    static final int PERMISSION_READ_STATE = 123;
    private static final int CREDENTIAL_PICKER_REQUEST = 1;

    private static int INTENT_AUTHENTICATE = 241;

    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FReff = FirebaseDatabase.getInstance().getReference();

        PD = new ProgressDialog(this);
        PD.setTitle("Please wait...");
        PD.setCanceledOnTouchOutside(false);

        binding.gotoadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AdminLogin.class));
            }
        });

        binding.gotosignup.setVisibility(View.GONE);

        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRegistration();
            }
        });

    }

    private void checkRegistration() {

        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);
        try
        {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0,new Bundle());
        }
        catch (IntentSender.SendIntentException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK)
        {
            // Obtain the phone number from the result
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            String PhoneNo = credentials.getId().substring(3);
            String FullPhoneNo = "+60" + PhoneNo;
            binding.setTxt.setText(FullPhoneNo + " not registered on SRMAP");
            readPhoneno(PhoneNo);
        }
        else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE)
        {
            // *** No phone numbers available ***
            Toast.makeText(MainActivity.this, "No phone numbers found", Toast.LENGTH_LONG).show();
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case PERMISSION_READ_STATE:{
//                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    MyTelephonyManager();
//                }else{
//                    Toast.makeText(this,"blablablabla",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//    private void MyTelephonyManager(){
//        TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//
//        String IMEINumber = manager.getDeviceId();
//        readIMEIno(IMEINumber);
//
//    }

    private void readPhoneno(String phoneNo) {
        FReff.child("Phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(phoneNo)){
                    PD.dismiss();
                    Intent intent = new Intent(MainActivity.this, UserProfile.class);
                    intent.putExtra("keyno1",phoneNo);
                    startActivity(intent);;

//                    KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//
//                    if (km.isKeyguardSecure()) {
//                        Intent authIntent = km.createConfirmDeviceCredentialIntent("Smart Rollcall Management Application", "Please enter your lockscreen password to proceed");
//                        startActivityForResult(authIntent, INTENT_AUTHENTICATE);
//                        startActivity(new Intent(MainActivity.this, UserProfile.class));
//                    }

                }else{
                    PD.dismiss();
                    binding.gotosignup.setVisibility(View.VISIBLE);
                    binding.signupbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, UserSignup.class);
                            intent.putExtra("keyno",phoneNo);
                            startActivity(intent);
//                startActivity(new Intent(MainActivity.this, UserSignup.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void readPhone(String imeiNumber) {
//
//        FReff.child("Phone").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.hasChild(imeiNumber)){
//                    PD.dismiss();
//
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//
//                        if (km.isKeyguardSecure()) {
//                            Intent authIntent = km.createConfirmDeviceCredentialIntent("Smart Rollcall Management Application", "Please enter your lockscreen password to proceed");
//                            startActivityForResult(authIntent, INTENT_AUTHENTICATE);
//                        }
//                    }
//
//                }else{
//                    PD.dismiss();
//                    binding.gotosignup.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK && requestCode==INTENT_AUTHENTICATE)
//        {
//            startActivity(new Intent(MainActivity.this, UserProfile.class));
//            Toast.makeText(MainActivity.this,"You had login successfully",Toast.LENGTH_LONG).show();
//        }
//        else
//        {
//            Toast.makeText(this, "Failure: Unable to verify user's identity", Toast.LENGTH_SHORT).show();
//        }
//    }


}