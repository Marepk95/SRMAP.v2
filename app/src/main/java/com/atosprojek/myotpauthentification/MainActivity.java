package com.atosprojek.myotpauthentification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;

import com.atosprojek.myotpauthentification.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity{

    private MainActivityBinding binding;

    private static int INTENT_AUTHENTICATE = 241;

    private ProgressDialog PD;

    LinearLayout expandableAdmin, expandableEmployee;
    CardView adminCard, employeeCard;

    private static final String KEY_NO = "phoneno";

    @Override
    public void onBackPressed() {
        // Simply Do noting!
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        expandableAdmin = findViewById(R.id.expandadmin);
        adminCard = findViewById(R.id.admincard);
        adminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableAdmin.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(adminCard, new AutoTransition());
                    expandableAdmin.setVisibility(View.VISIBLE);
                }else{
                    TransitionManager.beginDelayedTransition(adminCard, new AutoTransition());
                    expandableAdmin.setVisibility(View.GONE);
                }

            }
        });

        expandableEmployee = findViewById(R.id.expandemployee);
        employeeCard = findViewById(R.id.employeecard);
        employeeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableEmployee.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(employeeCard, new AutoTransition());
                    expandableEmployee.setVisibility(View.VISIBLE);
                }else{
                    TransitionManager.beginDelayedTransition(employeeCard, new AutoTransition());
                    expandableEmployee.setVisibility(View.GONE);
                }

            }
        });

        binding.gotoadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AdminLogin.class));
            }
        });

        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                PD = new ProgressDialog(this);
//                PD.setTitle("Please wait...");
//                PD.setCanceledOnTouchOutside(false);

                SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                String UserName = sharedPreferences.getString("UserName", null);

                if (UserName != null) {
                    Intent intent = new Intent(MainActivity.this, UserProfile.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, UserSignup.class);
                    startActivity(intent);
                }

            }
        });
    }
}