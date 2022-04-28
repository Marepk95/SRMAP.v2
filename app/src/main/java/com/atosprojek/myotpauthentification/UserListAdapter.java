package com.atosprojek.myotpauthentification;

import android.content.Context;
import android.graphics.Color;
import android.preference.ListPreference;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

    View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
    return new MyViewHolder(v);

  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    UserClass userClass = list.get(position);
    holder.rank.setText(userClass.getRank());
    holder.username.setText(userClass.getUserName());
    holder.phoneno.setText(userClass.getPhoneNo());
    holder.userID.setText(userClass.getUserID());
    holder.attendance.setText(userClass.getAttendance());
    holder.updated.setText(userClass.getUpdate());
    holder.timein.setText(userClass.getTimeIn());
    holder.timeout.setText(userClass.getTimeOut());

  }

  @Override
  public int getItemCount() {

    return list.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder{

    LinearLayout expandableView;
    CardView cardView, cardChangeColor;
    SimpleDateFormat simpleDateFormat;
    Calendar calendar;
    ListPreference preference;
    TextView Try;

    TextView rank, username, phoneno, userID, attendance, updated, timein, timeout;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);

      expandableView = itemView.findViewById(R.id.expandview);
      cardView = itemView.findViewById(R.id.cardview);
      cardChangeColor = itemView.findViewById(R.id.cardcolorchange);

      rank = itemView.findViewById(R.id.rank);
      username = itemView.findViewById(R.id.name);
      phoneno = itemView.findViewById(R.id.second);
      userID = itemView.findViewById(R.id.first);
      attendance = itemView.findViewById(R.id.third);
      updated = itemView.findViewById(R.id.updated);
      timein = itemView.findViewById(R.id.checkin);
      timeout = itemView.findViewById(R.id.checkout);
      Try = itemView.findViewById(R.id.trial);

      String ran = attendance.getText().toString();

//      calendar = Calendar.getInstance();
//      simpleDateFormat = new SimpleDateFormat("08:00:00");
//      String Time = simpleDateFormat.format(calendar.getTime());

//          if (attendance.equals("✔")){
//            cardChangeColor.setCardBackgroundColor(Color.YELLOW);
//            if (timein.equals(Time)){
//              return;
//            }else{
//              cardChangeColor.setCardBackgroundColor(Color.YELLOW);
//            }
//          }else{
//            cardChangeColor.setCardBackgroundColor(Color.RED);
//          }

      cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (expandableView.getVisibility()==View.GONE){
            TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
            expandableView.setVisibility(View.VISIBLE);

            Log.e("RAN",ran);
            if (ran.equals("✔")){
              cardChangeColor.setCardBackgroundColor(Color.RED);
              Try.setText(ran);

            }else{
              cardChangeColor.setCardBackgroundColor(Color.GREEN);
              Try.setText(ran);
            }

          }else{
            TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
            expandableView.setVisibility(View.GONE);
          }

        }
      });


    }

//    public void expand(View view) {
//      int v = (Details.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
//
//      TransitionManager.beginDelayedTransition(Layout, new AutoTransition());
//      Details.setVisibility(v);
//    }
  }

}