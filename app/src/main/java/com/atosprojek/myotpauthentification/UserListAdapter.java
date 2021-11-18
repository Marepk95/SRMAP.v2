package com.atosprojek.myotpauthentification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>{

    Context context;

    ArrayList<UserClass> list;

    public UserListAdapter(Context context, ArrayList<UserClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserClass user = list.get(position);
        holder.Name.setText(user.UserName);
        holder.First.setText(user.UserID);
        holder.Second.setText(user.PhoneNo);
        holder.Third.setText(user.Attendance);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Name, First,Second,Third;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.name);
            First = itemView.findViewById(R.id.first);
            Second = itemView.findViewById(R.id.second);
            Third = itemView.findViewById(R.id.third);


        }
    }

}
