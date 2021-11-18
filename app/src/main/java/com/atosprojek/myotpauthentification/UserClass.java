package com.atosprojek.myotpauthentification;

public class UserClass {

    public String UserName, PhoneNo, UserID, Attendance;

    public UserClass (){

    }
//
//    public String getPhoneNo() {
//        return PhoneNo;
//    }
//
//    public String getUserID() {
//        return UserID;
//    }
//
//    public String getAttendance() {
//        return Attendance;
//    }

    public UserClass(String UserName, String PhoneNo, String UserID, String Attendance){
        this.UserName = UserName;
        this.PhoneNo = PhoneNo;
        this.UserID = UserID;
        this.Attendance = Attendance;
    }
}
