package com.atosprojek.myotpauthentification;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.atosprojek.myotpauthentification.databinding.ActivityUserGuideBinding;
import com.atosprojek.myotpauthentification.databinding.UserProfileBinding;

public class UserGuide extends AppCompatActivity {

    ActivityUserGuideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}