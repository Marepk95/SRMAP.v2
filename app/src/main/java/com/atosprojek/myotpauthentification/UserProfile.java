package com.atosprojek.myotpauthentification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.atosprojek.myotpauthentification.databinding.UserProfileBinding;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserProfile extends AppCompatActivity {

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    private UserProfileBinding binding;

    private ProgressDialog PD;

    AlertDialog.Builder builderDialog;
    AlertDialog alertDialog;

    private static final String KEY_NO = "phoneno";

    DatabaseReference FReff;
    FirebaseAuth FAuth;

    @Override
    public void onBackPressed() {
        // Simply Do noting!
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        this.recreate();

        FReff = FirebaseDatabase.getInstance().getReferenceFromUrl("https://myotp-a9e65-default-rtdb.firebaseio.com/");
        FAuth = FirebaseAuth.getInstance();

        binding.finalupdated.setVisibility(View.GONE);
        binding.notattend.setVisibility(View.GONE);
        binding.updated.setVisibility(View.GONE);
        binding.timein.setVisibility(View.GONE);
        binding.timeout.setVisibility(View.GONE);
        binding.guide.setVisibility(View.VISIBLE);

        PD = new ProgressDialog(this);
        PD.setTitle("Please wait...");
        PD.setCanceledOnTouchOutside(false);

        binding.guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, UserGuide.class));
            }
        });

        binding.gotoadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, AdminLogin.class));
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String CompanyName = sharedPreferences.getString("CompanyName",null);
        String UserName = sharedPreferences.getString("UserName",null);
        binding.compname.setText(CompanyName);
        binding.setname.setText(UserName);

        FReff.child("Company").child(CompanyName).child("Attendance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String CompanyName = snapshot.child(Phoneno).child("Company").getValue(String.class);
//                binding.compname.setText(CompanyName);
//                String UserName = snapshot.child(Phoneno).child("User Name").getValue(String.class);
//                binding.setname.setText(UserName);
                if (snapshot.hasChild(UserName)){
                    binding.start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            PD.setMessage("Verifying your attendance");
                            PD.show();

                            try{
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                URL myIp = new URL("https://checkip.amazonaws.com/");
                                URLConnection c = myIp.openConnection();
                                c.setConnectTimeout(1000);
                                c.setReadTimeout(1000);

                                BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));

                                String Wifi = in.readLine();

                                FReff.child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(CompanyName)){
                                            String getIPaddress = snapshot.child(CompanyName).child("IPAddress").getValue(String.class);
                                            if (getIPaddress.equals(Wifi)){
                                                PD.dismiss();

                                                binding.notattend.setVisibility(View.GONE);
                                                binding.finalupdated.setVisibility(View.VISIBLE);

                                                showAlertDialogAttend(R.layout.my_sucess_dialog);

                                                FirebaseDatabase.getInstance().getReference("Company")
                                                        .child(CompanyName)
                                                        .child("Attendance")
                                                        .child(UserName)
                                                        .child("Attendance")
                                                        .setValue("✔").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });

                                            }else{
                                                PD.dismiss();
                                                binding.guide.setVisibility(View.GONE);
                                                binding.notattend.setVisibility(View.VISIBLE);
                                                binding.finalupdated.setVisibility(View.GONE);

                                                binding.send.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

//                                                PD.setMessage("Verifying your attendance");
//                                                PD.show();

                                                        String Reason = binding.reason.getText().toString();

                                                        FirebaseDatabase.getInstance().getReference("Company")
                                                                .child(CompanyName)
                                                                .child("Attendance")
                                                                .child(UserName)
                                                                .child("Attendance")
                                                                .setValue(Reason).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

//                                                        PD.dismiss();

                                                            }
                                                        });

                                                        showAlertDialog(R.layout.my_failed_dialog);

                                                    }
                                                });

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                    });

                }else{
                    binding.mainbutton.setVisibility(View.GONE);
                    binding.guide.setVisibility(View.GONE);
                    binding.backbtn.setVisibility(View.VISIBLE);
                    binding.backbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("CompanyName");
                            editor.remove("UserName");
                            editor.apply();
                            startActivity(new Intent(UserProfile.this, MainActivity.class));
                        }
                    });
                }

//                if (UserName == null){
//                    binding.guide.setVisibility(View.GONE);
//                    binding.backbtn.setVisibility(View.VISIBLE);
//                    binding.backbtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.remove(KEY_NO);
//                            editor.apply();
//                            startActivity(new Intent(UserProfile.this, MainActivity.class));
//                        }
//                    });
//
//                }else{
//
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showAlertDialogAttend(int myLayout) {

        builderDialog = new AlertDialog.Builder(this);
        View layoutView = getLayoutInflater().inflate(myLayout, null);

        Button TimeIn = layoutView.findViewById(R.id.timein);
        Button TimeOut = layoutView.findViewById(R.id.timeout);
        builderDialog.setView(layoutView);
        alertDialog = builderDialog.create();
        alertDialog.show();

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String Update = simpleDateFormat.format(calendar.getTime());
        binding.dateandtime.setText(Update);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String CompanyName = sharedPreferences.getString("CompanyName",null);
        String UserName = sharedPreferences.getString("UserName",null);

        FirebaseDatabase.getInstance().getReference("Company")
                .child(CompanyName)
                .child("Attendance")
                .child(UserName)
                .child("Update")
                .setValue(Update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

        TimeIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.guide.setVisibility(View.GONE);
                binding.updated.setVisibility(View.VISIBLE);
                binding.timein.setVisibility(View.VISIBLE);
                binding.timeout.setVisibility(View.GONE);
                binding.finalupdated.setVisibility(View.VISIBLE);

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String TimingIn = simpleDateFormat.format(calendar.getTime());
                binding.timingin.setText(TimingIn);

                FirebaseDatabase.getInstance().getReference("Company")
                        .child(CompanyName)
                        .child("Attendance")
                        .child(UserName)
                        .child("TimeIn")
                        .setValue(TimingIn).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                alertDialog.dismiss();
            }
        });

        TimeOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.guide.setVisibility(View.GONE);
                binding.updated.setVisibility(View.VISIBLE);
                binding.timein.setVisibility(View.GONE);
                binding.timeout.setVisibility(View.VISIBLE);
                binding.finalupdated.setVisibility(View.VISIBLE);

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String TimingOut = simpleDateFormat.format(calendar.getTime());
                binding.timingout.setText(TimingOut);

                FirebaseDatabase.getInstance().getReference("Company")
                        .child(CompanyName)
                        .child("Attendance")
                        .child(UserName)
                        .child("TimeOut")
                        .setValue(TimingOut).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                alertDialog.dismiss();
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

                binding.guide.setVisibility(View.GONE);
                binding.updated.setVisibility(View.VISIBLE);
                binding.timein.setVisibility(View.VISIBLE);
                binding.notattend.setVisibility(View.GONE);
                binding.finalupdated.setVisibility(View.VISIBLE);

                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                String CompanyName = sharedPreferences.getString("CompanyName",null);
                String UserName = sharedPreferences.getString("UserName",null);

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String Update = simpleDateFormat.format(calendar.getTime());
                binding.dateandtime.setText(Update);

                FirebaseDatabase.getInstance().getReference("Company")
                        .child(CompanyName)
                        .child("Attendance")
                        .child(UserName)
                        .child("Update")
                        .setValue(Update).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                String TimingIn = simpleDateFormat.format(calendar.getTime());
                binding.timingin.setText(TimingIn);

                FirebaseDatabase.getInstance().getReference("Company")
                        .child(CompanyName)
                        .child("Attendance")
                        .child(UserName)
                        .child("TimeIn")
                        .setValue(TimingIn).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

                alertDialog.dismiss();

            }
        });
    }

}