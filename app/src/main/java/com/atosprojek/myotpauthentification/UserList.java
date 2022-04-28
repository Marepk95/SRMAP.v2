package com.atosprojek.myotpauthentification;

import static com.google.firebase.database.core.utilities.Utilities.hardAssert;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atosprojek.myotpauthentification.databinding.UserListBinding;
import com.atosprojek.myotpauthentification.databinding.UserSignupBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.ServerValues;
import com.google.firebase.database.core.ValidationPath;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.PriorityUtilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class UserList extends AppCompatActivity{

    UserListBinding binding;

    RecyclerView recyclerView;
    DatabaseReference FReff;
    UserListAdapter userListAdapter;
    ArrayList<UserClass> list;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String CompanyName = getIntent().getStringExtra("keyname");
        binding.companyname.setText(CompanyName);

        recyclerView = findViewById(R.id.userlist);
        FReff = FirebaseDatabase.getInstance().getReference("Company");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        userListAdapter = new UserListAdapter( this,list);
        recyclerView.setAdapter(userListAdapter);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String Update = simpleDateFormat.format(calendar.getTime());
        binding.datetime.setText(Update);

        FReff.child(CompanyName).child("Attendance").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserClass userClass = dataSnapshot.getValue(UserClass.class);
                    list.add(userClass);



                }
                userListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}