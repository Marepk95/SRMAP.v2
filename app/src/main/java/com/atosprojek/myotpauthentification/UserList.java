package com.atosprojek.myotpauthentification;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atosprojek.myotpauthentification.databinding.AdminProfileBinding;
import com.atosprojek.myotpauthentification.databinding.UserListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {

    UserListBinding binding;

    RecyclerView recyclerView;
    DatabaseReference FReff;
    UserListAdapter userListAdapter;
    ArrayList<UserClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String CompanyName = getIntent().getStringExtra("keyname");
        binding.company.setText(CompanyName);

        recyclerView = findViewById(R.id.userlist);
        FReff = FirebaseDatabase.getInstance().getReference("Company");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        userListAdapter = new UserListAdapter(this,list);
        recyclerView.setAdapter(userListAdapter);

        FReff.child(CompanyName).child("Attendance").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserClass user = dataSnapshot.getValue(UserClass.class);
                    list.add(user);
                }
                userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}