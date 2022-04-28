package com.atosprojek.myotpauthentification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.atosprojek.myotpauthentification.databinding.ActivityConfirmUserRegisterBinding;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ConfirmUserRegister extends AppCompatActivity {

    ActivityConfirmUserRegisterBinding binding;
    private static final String KEY_NO = "phoneno";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_user_register);
        binding = ActivityConfirmUserRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String CompName = getIntent().getStringExtra("company");
        String ConfirmCompany = "Register under " + CompName;
        binding.companyconfirm.setText(ConfirmCompany);

        String Rank = getIntent().getStringExtra("userrank");
        binding.rank.setText(Rank);

        String UserName = getIntent().getStringExtra("username");
        binding.name.setText(UserName);

        String UserID = getIntent().getStringExtra("userid");
        binding.userid.setText(UserID);

        String PhoneNo = getIntent().getStringExtra("userphone");
        binding.phone.setText(PhoneNo);

        binding.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Attendance = null;
                String Update = null;
                String TimeIn = null;
                String TimeOut = null;

                UserClass user = new UserClass(Rank, UserName, PhoneNo, UserID, Attendance, Update, TimeIn, TimeOut);

                FirebaseDatabase.getInstance().getReference("Company")
                        .child(CompName)
                        .child("Attendance")
                        .child(UserName)
                        .setValue(user).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("CompanyName",CompName);
                        editor.putString("UserName",UserName);
                        editor.apply();

                        startActivity(new Intent(ConfirmUserRegister.this, UserProfile.class));

                    }else{
                        Toast.makeText(ConfirmUserRegister.this,"Failed to register",Toast.LENGTH_LONG).show();
                    }
                });
//
//                HashMap<String, String> userMap1 = new HashMap<>();
//
//                userMap1.put("User Name", UserName);

                FirebaseDatabase.getInstance().getReference("Phone")
                        .child(PhoneNo)
                        .setValue(UserName).addOnCompleteListener(task -> {

                });

                FirebaseDatabase.getInstance().getReference("User ID")
                        .child(UserID)
                        .setValue(UserName).addOnCompleteListener(task -> {});

            }
        });

    }
}