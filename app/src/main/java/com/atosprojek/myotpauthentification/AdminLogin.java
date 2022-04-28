package com.atosprojek.myotpauthentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;

public class AdminLogin extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    AdminLoginBinding binding;

    private FirebaseAuth FAuth;
    private DatabaseReference FReff;

    Spinner spinnerCompany;

    ArrayList<String> CompanyList;
    ArrayAdapter<String> CompanyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinnerCompany = binding.spincompany;
        CompanyList = new ArrayList<>();
        CompanyAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,CompanyList);
        CompanyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCompany.setAdapter(CompanyAdapter);
        spinnerCompany.setOnItemSelectedListener(this);

        FAuth = FirebaseAuth.getInstance();
        FReff = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myotp-a9e65-default-rtdb.firebaseio.com/");

        getCompanyData();

        binding.registernowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLogin.this, AdminRegister.class));
            }
        });

        binding.adminloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String CompanyName = binding.companyname.getText().toString();
                String AdminPassword = binding.adminpassword.getText().toString();

                if (AdminPassword.isEmpty()){
                    Toast.makeText(AdminLogin.this,"Please insert company password",Toast.LENGTH_SHORT).show();
                }else{
                    startLoginAdmin();
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

    private void startLoginAdmin() {

        FReff.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String company = spinnerCompany.getSelectedItem().toString();
                if (snapshot.hasChild(company)){
                    String getPassword = snapshot.child(company).child("AdminPassword").getValue(String.class);
                    String AdminPassword = binding.adminpassword.getText().toString();
                    if (getPassword.equals(AdminPassword)){
                        Intent intent = new Intent(AdminLogin.this, AdminProfile.class);
                        intent.putExtra("keyname",company);
                        startActivity(intent);
                    }else {
                        Toast.makeText(AdminLogin.this,"Wrong company password",Toast.LENGTH_SHORT).show();
                    }
                }
//                if(snapshot.hasChild(CompanyName)){
//                    PD.dismiss();
//                    String getPassword = snapshot.child(CompanyName).child("AdminPassword").getValue(String.class);
//                    if (getPassword.equals(AdminPassword)){
//                        Intent intent = new Intent(AdminLogin.this, AdminProfile.class);
//                        intent.putExtra("keyname",CompanyName);
//                        startActivity(intent);
//                    }else {
//                        Toast.makeText(AdminLogin.this,"Company information are not mathced",Toast.LENGTH_SHORT).show();
//                    }
//
//                }else{
//                    PD.dismiss();
//                    Toast.makeText(AdminLogin.this,"Company Name not existed",Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminLogin.this,"Unable to login your company",Toast.LENGTH_SHORT).show();
            }
        });

//        FAuth.signInWithEmailAndPassword(Email, CompanyPassword)
//                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        PD.dismiss();
//
//                        String CompanyID = FAuth.getCurrentUser().getUid();
//
//                        FReff.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                if(snapshot.hasChild(CompanyName)){
//                                    String getPassword = snapshot.child(CompanyName).child("CompanyID").getValue(String.class);
//                                    if (getPassword.equals(CompanyID)){
//                                        Intent intent = new Intent(AdminLogin.this, AdminProfile.class);
//                                        intent.putExtra("keyname",CompanyName);
//                                        startActivity(intent);
//                                    }else {
//                                        Toast.makeText(AdminLogin.this,"Company information are not mathced",Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }else{
//                                    Toast.makeText(AdminLogin.this,"Company Name not existed",Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        PD.dismiss();
//                        Toast.makeText(AdminLogin.this,"Please check your email and password",Toast.LENGTH_SHORT).show();
//                    }
//                });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}








