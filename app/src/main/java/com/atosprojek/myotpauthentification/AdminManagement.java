package com.atosprojek.myotpauthentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.atosprojek.myotpauthentification.databinding.AdminManagementBinding;
import com.atosprojek.myotpauthentification.databinding.UserListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class AdminManagement extends AppCompatActivity {

    AdminManagementBinding binding;

    DatabaseReference FReff;
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FReff = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myotp-a9e65-default-rtdb.firebaseio.com/");
        FAuth = FirebaseAuth.getInstance();

        String CompanyName = getIntent().getStringExtra("keyname");
        binding.companyname.setText(CompanyName);

        binding.updateadminpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                FirebaseDatabase.getInstance().getReference("Company")
//                        .child(CompanyName)
//                        .child("IPAddress")
//                        .setValue(Wifi).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                    }
//                });

            }
        });

        binding.updatecompanypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String CompanyName = binding.companyname.getText().toString();
//                String AdminPassword = binding.adminpassword.getText().toString();
//                String CompanyPassword = binding.companypassword.getText().toString();
//                String Email = binding.email.getText().toString();

//                FirebaseDatabase.getInstance().getReference("Company")
//                        .child(CompanyName)
//                        .child("IPAddress")
//                        .setValue(Wifi).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                    }
//                });

            }
        });

    }
}