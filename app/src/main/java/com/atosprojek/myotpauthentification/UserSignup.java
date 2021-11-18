package com.atosprojek.myotpauthentification;

import  androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.atosprojek.myotpauthentification.databinding.UserSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class UserSignup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    UserSignupBinding binding;

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks pCallBacks;
    private String VerificationId;
    private static final String TAG = "MAIN_TAG";

    private FirebaseAuth FAuth;
    private DatabaseReference FReff;

    private ProgressDialog PD;

//    private static final int REQ_USER_CONSENT = 200;
//    SMSBroadcastReceiver SMSBroadcastReceiver;
//    static final int PERMISSION_READ_STATE = 123;

    Spinner spinnerRank;
    Spinner spinnerCompany;

    ArrayList<String> CompanyList;
    ArrayAdapter<String> CompanyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinnerCompany = binding.spincompany;
        CompanyList = new ArrayList<>();
        CompanyAdapter = new ArrayAdapter<>(this,R.layout.list_item,CompanyList);
        CompanyAdapter.setDropDownViewResource(R.layout.list_item);
        spinnerCompany.setAdapter(CompanyAdapter);
        spinnerCompany.setOnItemSelectedListener(this);

        spinnerRank = binding.spinrank;
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Rank, R.layout.list_item);
        adapter2.setDropDownViewResource(R.layout.list_item);
        spinnerRank.setAdapter(adapter2);
        spinnerRank.setOnItemSelectedListener(this);

        String Phoneno = getIntent().getStringExtra("keyno");
        String FullPhoneNo = "+60" + Phoneno;
        binding.phonenumber.setText(FullPhoneNo);

//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//
//        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
//            MyTelephonyManager();
//        }else{
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_PHONE_STATE},
//                    PERMISSION_READ_STATE);
//        }
//
        binding.phonelayout.setVisibility(View.VISIBLE);
        binding.OTPlayout.setVisibility(View.GONE);

        FAuth = FirebaseAuth.getInstance();
        FReff = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myotp-a9e65-default-rtdb.firebaseio.com/");

        getCompanyData();

        PD = new ProgressDialog(this);
        PD.setTitle("Please wait...");
        PD.setCanceledOnTouchOutside(false);

//        startSmartUserConsent();

        pCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                PD.dismiss();
                Toast.makeText(UserSignup.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG, "onCodeSent: "+ verificationId);

                VerificationId = verificationId;
                forceResendingToken = token;
                PD.dismiss();

                binding.phonelayout.setVisibility(View.GONE);
                binding.OTPlayout.setVisibility(View.VISIBLE);

                Toast.makeText(UserSignup.this,"Verification code sent...",Toast.LENGTH_SHORT).show();

                binding.codeSendDescription.setText("Please type the verification code we sent \nto" +binding.phonenumber.getText().toString());
            }
        };

        binding.signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String Company = binding.companyname.getText().toString();

                String Name = binding.username.getText().toString();
                String Password = binding.companypassword.getText().toString();
                String UserID = binding.userid.getText().toString();


                if (Name.isEmpty() ||Password.isEmpty() || UserID.isEmpty()){
                    Toast.makeText(UserSignup.this,"Please fill up all information",Toast.LENGTH_SHORT).show();
                }else{
                    identifycompanypassword();
//                    startPhoneNumberVerification(phone);
                }
            }
        });

        binding.resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = binding.phonenumber.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(UserSignup.this,"Please enter phone number",Toast.LENGTH_SHORT).show();

                }else{
                    resendVerification(phone, forceResendingToken);
                }

            }
        });

        binding.submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = binding.verificationcode.getText().toString();
                if (TextUtils.isEmpty(code)){
                    Toast.makeText(UserSignup.this,"Please enter verification code",Toast.LENGTH_SHORT).show();

                }else {
                    verifyPhoneNumber(VerificationId, code);
                }
            }
        });

    }

    private void getCompanyData() {

        FReff.child("Company").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    CompanyList.add(item.child("CompanyName").getValue(String.class));
                    CompanyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void identifycompanypassword() {

//        FReff.child("Users");
        FReff.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String company = spinnerCompany.getSelectedItem().toString();
                if (snapshot.hasChild(company)){
                    String getPassword = snapshot.child(company).child("CompanyPassword").getValue(String.class);
                    String Password = binding.companypassword.getText().toString();
                    if (getPassword.equals(Password)){
                        checkPhoneNo();
//                        startPhoneNumberVerification(phone);
                    }else {
                        Toast.makeText(UserSignup.this,"Wrong company password",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkPhoneNo() {

        FReff.child("Phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Phoneno = getIntent().getStringExtra("keyno");
//                String phone = binding.phonenumber.getText().toString();
                if (snapshot.hasChild(Phoneno)){
                    Toast.makeText(UserSignup.this,"Employee is already registered",Toast.LENGTH_SHORT).show();
                }else{
                    checkUserID();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUserID() {
        String phone = binding.phonenumber.getText().toString();
        FReff.child("User ID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String UserID = binding.userid.getText().toString();
                if (snapshot.hasChild(UserID)){
                    Toast.makeText(UserSignup.this,"Employee is already registered",Toast.LENGTH_SHORT).show();
                }else{
                    startPhoneNumberVerification(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //        public void Start(View view) {
//
//    }
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
//
//        binding.IMEIno.setText(IMEINumber);
//
//    }

    private void startPhoneNumberVerification(String phone){
        PD.setMessage("Verfiying Phone Number");
        PD.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(pCallBacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerification (String phone, PhoneAuthProvider.ForceResendingToken token){

        PD.setMessage("Resending Code");
        PD.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(pCallBacks)
                        .setForceResendingToken(token)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifyPhoneNumber (String VerificationId, String code) {

        PD.setMessage("Verifying Code");
        PD.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId, code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential (PhoneAuthCredential credential){
        PD.setMessage("Sign Up");

        FAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        PD.dismiss();
//                        String phone = FAuth.getCurrentUser().getPhoneNumber();

                        String Attendance = null;

                        String CompName = binding.spincompany.getSelectedItem().toString();

                        String UserName = binding.spinrank.getSelectedItem().toString() + " " + binding.username.getText().toString();
                        String UserID = binding.userid.getText().toString();
                        String Phoneno = getIntent().getStringExtra("keyno");
                        String PhoneNo = "+60" + Phoneno;

                        UserClass user = new UserClass(UserName, PhoneNo, UserID, Attendance);

//                        HashMap<String, String> userMap = new HashMap<>();
//
//                        userMap.put("User ID", UserID);
//                        userMap.put("Phone No", phone);

                        FirebaseDatabase.getInstance().getReference("Company")
//                                .child(FAuth.getUid())
                                .child(CompName)
                                .child("Attendance")
                                .child(UserName)
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(UserSignup.this,UserName+" successfully registered!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserSignup.this, MainActivity.class));
                                }else{
                                    Toast.makeText(UserSignup.this,"Failed to register",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        HashMap<String, String> userMap1 = new HashMap<>();

                        userMap1.put("User Name", UserName);
                        userMap1.put("Company", CompName);

                        FirebaseDatabase.getInstance().getReference("Phone")
                                .child(Phoneno)
                                .setValue(userMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReference("User ID")
                                .child(UserID)
                                .setValue(UserName).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        PD.dismiss();
                        Toast.makeText(UserSignup.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}