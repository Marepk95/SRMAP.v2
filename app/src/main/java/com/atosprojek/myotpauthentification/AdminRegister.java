package com.atosprojek.myotpauthentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;

import com.atosprojek.myotpauthentification.databinding.AdminRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class AdminRegister extends AppCompatActivity {

    AdminRegistrationBinding binding;
    private DatabaseReference FReff;

    private FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());;

        FAuth = FirebaseAuth.getInstance();
        FReff = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myotp-a9e65-default-rtdb.firebaseio.com/");

        binding.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    URL myIp = new URL("https://checkip.amazonaws.com/");
                    URLConnection c = myIp.openConnection();
                    c.setConnectTimeout(1000);
                    c.setReadTimeout(1000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    binding.wifi.setText(in.readLine());

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//                binding.wifi.setText(Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()));
            }
        });

        binding.adminregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CompanyName = binding.companyname.getText().toString();
                String AdminPassword = binding.adminpassword.getText().toString();
                String CompanyPassword = binding.companypassword.getText().toString();
                String Email = binding.email.getText().toString();
                String Wifi = binding.wifi.getText().toString();

                if (CompanyName.isEmpty() ||AdminPassword.isEmpty() || CompanyPassword.isEmpty() || Email.isEmpty() || Wifi.isEmpty()){
                    Toast.makeText(AdminRegister.this,"Please fill up all information",Toast.LENGTH_SHORT).show();
                }else if (AdminPassword.equals(CompanyPassword)){
                    Toast.makeText(AdminRegister.this,"Admin Password & Company Password cannot be the same",Toast.LENGTH_SHORT).show();
                }else{
                    startCheckExisted();
                }
            }
        });

    }

    private void startCheckExisted() {

        String CompanyName = binding.companyname.getText().toString();

        FReff.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(CompanyName)){
                    Toast.makeText(AdminRegister.this,"Company Name is already registered",Toast.LENGTH_SHORT).show();
                }else{
                    startRegisterAdmin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        FReff.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    if (CompanyName.equals(snapshot.getRef())){
//                        Toast.makeText(AdminRegister.this,"Company already existed",Toast.LENGTH_SHORT).show();
//                    }else{
//                        startRegisterAdmin();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void startRegisterAdmin() {

        String AdminPassword = binding.adminpassword.getText().toString();
        String Email = binding.email.getText().toString();

        FAuth.createUserWithEmailAndPassword(Email, AdminPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        String ID = FAuth.getCurrentUser().getUid();
                        String CompanyName = binding.companyname.getText().toString();
                        String IPAddress = binding.wifi.getText().toString();
                        String CompanyPassword = binding.companypassword.getText().toString();
                        String AdminPassword = binding.adminpassword.getText().toString();

//                        HashMap<String, String> companyMap = new HashMap<>();
//                        companyMap.put("Company Name", CompanyName);
//                        companyMap.put("Company ID", CompanyID);
//                        companyMap.put("Company Password", CompanyPassword);
//                        companyMap.put("IP Address", IPAddress);

                        AdminCall adminCall = new AdminCall(CompanyName, CompanyPassword, IPAddress, ID, AdminPassword);

                        FirebaseDatabase.getInstance().getReference("Company")
                                .child(CompanyName)
                                .setValue(adminCall).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(AdminRegister.this,CompanyName + " successfully registered on SRMAP", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AdminRegister.this, AdminLogin.class));
                                }else{
                                    Toast.makeText(AdminRegister.this,"Failed to register company",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminRegister.this,"Email is already registered",Toast.LENGTH_LONG).show();
                    }
                });
    }
}









