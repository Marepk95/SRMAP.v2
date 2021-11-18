package com.atosprojek.myotpauthentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.atosprojek.myotpauthentification.databinding.AdminLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {

    AdminLoginBinding binding;

    private FirebaseAuth FAuth;
    private DatabaseReference FReff;
    private ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FAuth = FirebaseAuth.getInstance();
        FReff = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myotp-a9e65-default-rtdb.firebaseio.com/");

        binding.adminloginpage.setVisibility(View.VISIBLE);
        binding.adminprofilepage.setVisibility(View.GONE);

        PD = new ProgressDialog(this);
        PD.setTitle("Please wait...");
        PD.setCanceledOnTouchOutside(false);

        binding.registernowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLogin.this, AdminRegister.class));
            }
        });

        binding.adminloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String CompanyName = binding.companyname.getText().toString();
                String AdminPassword = binding.adminpassword.getText().toString();
                String Email = binding.email.getText().toString();

                if (CompanyName.isEmpty() ||AdminPassword.isEmpty() || Email.isEmpty()){
                    Toast.makeText(AdminLogin.this,"Please fill up all information",Toast.LENGTH_SHORT).show();
//                }else if (CompanyPassword.equals(AdminPassword)){
//                    Toast.makeText(AdminLogin.this,"Password not matched",Toast.LENGTH_SHORT).show();
                }else{
                    startLoginAdmin();
                }
            }
        });

    }

    private void startLoginAdmin() {

        PD.setMessage("Verifying admin login");
        PD.show();

        String CompanyName = binding.companyname.getText().toString();
        String CompanyPassword = binding.adminpassword.getText().toString();
        String Email = binding.email.getText().toString();

        FAuth.signInWithEmailAndPassword(Email, CompanyPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        PD.dismiss();

                        String CompanyID = FAuth.getCurrentUser().getUid();

                        FReff.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.hasChild(CompanyName)){
                                    String getPassword = snapshot.child(CompanyName).child("CompanyID").getValue(String.class);
                                    if (getPassword.equals(CompanyID)){
                                        Intent intent = new Intent(AdminLogin.this, AdminProfile.class);
                                        intent.putExtra("keyname",CompanyName);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(AdminLogin.this,"Company information are not mathced",Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    Toast.makeText(AdminLogin.this,"Company Name not existed",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        PD.dismiss();
                        Toast.makeText(AdminLogin.this,"Please check your email and password",Toast.LENGTH_SHORT).show();
                    }
                });

    }
}








