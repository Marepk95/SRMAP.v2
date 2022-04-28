package com.atosprojek.myotpauthentification;

import com.google.firebase.database.PropertyName;

public class UserClass {

  public String Rank, UserName, PhoneNo, UserID, Attendance, Update, TimeIn, TimeOut;

  public UserClass (String Rank, String UserName, String PhoneNo, String UserID, String Attendance, String Update, String TimeIn, String TimeOut){
    this.Rank = Rank;
    this.UserName = UserName;
    this.PhoneNo = PhoneNo;
    this.UserID = UserID;
    this.Attendance = Attendance;
    this.Update = Update;
    this.TimeIn = TimeIn;
    this.TimeOut = TimeOut;
  }

  public UserClass(){}

  @PropertyName("Rank")
  public String getRank() {
    return Rank;
  }

  @PropertyName("UserName")
  public String getUserName() {
    return UserName;
  }

  @PropertyName("PhoneNo")
  public String getPhoneNo() {
    return PhoneNo;
  }

  @PropertyName("UserID")
  public String getUserID() {
    return UserID;
  }

  @PropertyName("Attendance")
  public String getAttendance() {
    return Attendance;
  }

  @PropertyName("Update")
  public String getUpdate() {
    return Update;
  }

  @PropertyName("TimeIn")
  public String getTimeIn() {
    return TimeIn;
  }

  @PropertyName("TimeOut")
  public String getTimeOut() {
    return TimeOut;
  }

}