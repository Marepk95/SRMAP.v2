package com.atosprojek.myotpauthentification;

public class AdminCall {

    public String CompanyName, IPAddress, CompanyPassword, CompanyID, AdminPassword;

    public AdminCall(){

    }

    public AdminCall(String CompanyName, String CompanyPassword, String IPAddress, String CompanyID, String AdminPassword){
        this.CompanyName = CompanyName;
        this.IPAddress = IPAddress;
        this.CompanyPassword = CompanyPassword;
        this.CompanyID = CompanyID;
        this.AdminPassword = AdminPassword;
    }

}
