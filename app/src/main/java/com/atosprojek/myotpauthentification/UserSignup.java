package com.atosprojek.myotpauthentification;

import  androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.atosprojek.myotpauthentification.databinding.UserSignupBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSignup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    UserSignupBinding binding;

    private static final String TAG = "MAIN_TAG";

    private DatabaseReference FReff;

    private ProgressDialog PD;

//    Spinner spinnerRank;
    Spinner spinnerCompany;

    ArrayList<String> CompanyList;
    ArrayAdapter<String> CompanyAdapter;

//    AlertDialog.Builder builderDialog;
//    AlertDialog alertDialog;
//
//    private static final String KEY_NO = "phoneno";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinnerCompany = binding.spincompany;
        CompanyList = new ArrayList<>();
        CompanyAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,CompanyList);
        CompanyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCompany.setAdapter(CompanyAdapter);
        spinnerCompany.setOnItemSelectedListener(this);

        FReff = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myotp-a9e65-default-rtdb.firebaseio.com/");

        getCompanyData();

        PD = new ProgressDialog(this);
        PD.setTitle("Please wait...");
        PD.setCanceledOnTouchOutside(false);

        binding.signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = binding.username.getText().toString();
                String Password = binding.companypassword.getText().toString();
                String UserID = binding.userid.getText().toString();


                if (Name.isEmpty() ||Password.isEmpty() || UserID.isEmpty()){
                    Toast.makeText(UserSignup.this,"Please fill up all information",Toast.LENGTH_SHORT).show();
                }else{
                    identifycompanypassword();
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

        FReff.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String company = spinnerCompany.getSelectedItem().toString();

                if (snapshot.hasChild(company)){
                    String getPassword = snapshot.child(company).child("CompanyPassword").getValue(String.class);
                    String Password = binding.companypassword.getText().toString();
                    if (getPassword.equals(Password)){
                        checkPhoneNo();
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
                String Phoneno = "+6" + binding.phonenumber.getText().toString();
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

        FReff.child("User ID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String UserID = binding.userid.getText().toString();
                if (snapshot.hasChild(UserID)) {
                    Toast.makeText(UserSignup.this, "Employee is already registered", Toast.LENGTH_SHORT).show();
                } else {


                    startRegisterUser();
//                    startPhoneNumberVerification(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startRegisterUser() {
        PD.dismiss();

        String Attendance = null;
        String Update = null;
        String TimeIn = null;
        String TimeOut = null;

        String CompName = binding.spincompany.getSelectedItem().toString();

        String Rank = binding.rank.getText().toString();
        String UserName = binding.username.getText().toString();
        String UserID = binding.userid.getText().toString();
        String Phoneno = binding.phonenumber.getText().toString();
        String PhoneNo = "+6" + Phoneno;

        Intent intent = new Intent(UserSignup.this, ConfirmUserRegister.class);
        intent.putExtra("company",CompName);
        intent.putExtra("userrank",Rank);
        intent.putExtra("username",UserName);
        intent.putExtra("userid",UserID);
        intent.putExtra("userphone",PhoneNo);
        startActivity(intent);

//        UserClass user = new UserClass(Rank, UserName, PhoneNo, UserID, Attendance, Update, TimeIn, TimeOut);
//
//        FirebaseDatabase.getInstance().getReference("Company")
////                                .child(FAuth.getUid())
//                .child(CompName)
//                .child("Attendance")
//                .child(UserName)
//                .setValue(user).addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//
////                        showAlertDialogRegistered(R.layout.my_employee_register);
//
//                    }else{
//                        Toast.makeText(UserSignup.this,"Failed to register",Toast.LENGTH_LONG).show();
//                    }
//                });
//
//        HashMap<String, String> userMap1 = new HashMap<>();
//
//        userMap1.put("User Name", UserName);
//        userMap1.put("Company", CompName);
//
//        FirebaseDatabase.getInstance().getReference("Phone")
//                .child(Phoneno)
//                .setValue(userMap1).addOnCompleteListener(task -> {
//
//                });
//
//        FirebaseDatabase.getInstance().getReference("User ID")
//                .child(UserID)
//                .setValue(UserName).addOnCompleteListener(task -> {});
    }

//    private void showAlertDialogRegistered(int my_employee_register) {
//
//        builderDialog = new AlertDialog.Builder(this);
//        View layoutView = getLayoutInflater().inflate(my_employee_register, null);
//
//        Button BackBtn = layoutView.findViewById(R.id.back);
//        builderDialog.setView(layoutView);
//        alertDialog = builderDialog.create();
//        alertDialog.show();
//
//        BackBtn.setOnClickListener(v -> {
//            alertDialog.dismiss();
//
//            String phone = binding.phonenumber.getText().toString();
//
//            SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(KEY_NO,phone);
//            editor.apply();
//
//            startActivity(new Intent(UserSignup.this, UserProfile.class));
//        });
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}