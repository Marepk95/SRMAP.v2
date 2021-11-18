package com.atosprojek.myotpauthentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.atosprojek.myotpauthentification.databinding.UserProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    DatabaseReference FReff;
    FirebaseAuth FAuth;

    private UserProfileBinding binding;

    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FReff = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myotp-a9e65-default-rtdb.firebaseio.com/");
        FAuth = FirebaseAuth.getInstance();

        binding.attended.setVisibility(View.GONE);
        binding.notattend.setVisibility(View.GONE);

        PD = new ProgressDialog(this);
        PD.setTitle("Please wait...");
        PD.setCanceledOnTouchOutside(false);

        startProfile();

    }

    private void startProfile() {

        String Phoneno = getIntent().getStringExtra("keyno1");

        FReff.child("Phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String CompanyName = snapshot.child(Phoneno).child("Company").getValue(String.class);
                binding.compname.setText(CompanyName);

                String UserName = snapshot.child(Phoneno).child("User Name").getValue(String.class);
                binding.setname.setText(UserName);

                binding.refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PD.setMessage("Verifying your attendance");
                        PD.show();

                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                        String currentIPaddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

                        FReff.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild(CompanyName)){
                                    String getIPaddress = snapshot.child(CompanyName).child("IPAddress").getValue(String.class);
                                    if (getIPaddress.equals(currentIPaddress)){
                                        PD.dismiss();
                                        binding.attended.setVisibility(View.VISIBLE);
                                        binding.notattend.setVisibility(View.GONE);

                                        FirebaseDatabase.getInstance().getReference("Company")
                                                .child(CompanyName)
                                                .child("Attendance")
                                                .child(UserName)
                                                .child("Attendance")
                                                .setValue("Attend").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });

                                    }else{
                                        PD.dismiss();
                                        binding.attended.setVisibility(View.GONE);
                                        binding.notattend.setVisibility(View.VISIBLE);

                                        FirebaseDatabase.getInstance().getReference("Company")
                                                .child(CompanyName)
                                                .child("Attendance")
                                                .child(UserName)
                                                .child("Attendance")
                                                .setValue("Not Attend").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}