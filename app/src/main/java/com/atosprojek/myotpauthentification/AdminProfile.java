package com.atosprojek.myotpauthentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atosprojek.myotpauthentification.databinding.AdminLoginBinding;
import com.atosprojek.myotpauthentification.databinding.AdminProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AdminProfile extends AppCompatActivity {

    AdminProfileBinding binding;

    FirebaseUser User;
    DatabaseReference FReff;
    FirebaseAuth FAuth;

    private static final String KEY_NO = "phoneno";

    String CompanyID;

    AlertDialog.Builder builderDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminProfile.this, MainActivity.class));
            }
        });

        String CompanyName = getIntent().getStringExtra("keyname");
        binding.setcompname.setText(CompanyName);

        binding.attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminProfile.this, UserList.class);
                intent.putExtra("keyname",CompanyName);
                startActivity(intent);
            }
        });

        User = FirebaseAuth.getInstance().getCurrentUser();
        FAuth = FirebaseAuth.getInstance();
        FReff = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myotp-a9e65-default-rtdb.firebaseio.com/");

        FReff.child("Company").child(CompanyName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AdminCall adminProfile = snapshot.getValue(AdminCall.class);

                if (adminProfile != null){
                    String IPaddress = adminProfile.IPAddress;
                    binding.setipadd.setText(IPaddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminProfile.this, AdminManagement.class);
                intent.putExtra("keyname",CompanyName);
                startActivity(intent);

            }
        });

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

        binding.updateip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CompanyName = binding.setcompname.getText().toString();
//                String AdminPassword = binding.adminpassword.getText().toString();
//                String CompanyPassword = binding.companypassword.getText().toString();
//                String Email = binding.email.getText().toString();
                String Wifi = binding.wifi.getText().toString();

                FirebaseDatabase.getInstance().getReference("Company")
                        .child(CompanyName)
                        .child("IPAddress")
                        .setValue(Wifi).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {



                    }
                });

                showAlertDialog(R.layout.my_admin_change_ipaddress);
            }
        });


    }

    private void showAlertDialog(int myLayout) {

        builderDialog = new AlertDialog.Builder(this);
        View layoutView = getLayoutInflater().inflate(myLayout, null);

        Button BackBtn = layoutView.findViewById(R.id.back);
        builderDialog.setView(layoutView);
        alertDialog = builderDialog.create();
        alertDialog.show();

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}