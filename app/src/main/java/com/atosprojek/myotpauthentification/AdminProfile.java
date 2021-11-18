package com.atosprojek.myotpauthentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.atosprojek.myotpauthentification.databinding.AdminLoginBinding;
import com.atosprojek.myotpauthentification.databinding.AdminProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminProfile extends AppCompatActivity {

    AdminProfileBinding binding;

    FirebaseUser User;
    DatabaseReference FReff;

    String CompanyID;

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
        FReff = FirebaseDatabase.getInstance().getReference("Company");
        CompanyID = User.getUid();

        FReff.child(CompanyName).addListenerForSingleValueEvent(new ValueEventListener() {
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

    }
}