package com.plexoc.helpma.Model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("Id")
    public int Id;

    @SerializedName("FirstName")
    public String FirstName;

    @SerializedName("LastName")
    public String LastName;

    @SerializedName("Email")
    public String Email;

    @SerializedName("Password")
    public String Password;

    @SerializedName("Contact1")
    public String Phone;

    @SerializedName("Contact2")
    public String Phone2;

    @SerializedName("Country")
    public String Country;

    @SerializedName("State")
    public String State;

    @SerializedName("City")
    public String City;

    @SerializedName("AddressLine1")
    public String AddressLine1;

    @SerializedName("AddressLine2")
    public String AddressLine2;

    @SerializedName("UserType")
    public int UserType;

    @SerializedName("Dob")
    public String Dateofbirth;

    @SerializedName("ProfileUrl")
    public String ProfileUrl;

    @SerializedName("StateId")
    public int StateId;

    @SerializedName("StripeId")
    public String StripeId;

    @SerializedName("CountryId")
    public int CountryId;

    @SerializedName("TimeZone")
    public int TimeZone;

    @SerializedName("CreatedAt")
    public String CreatedAt;

    @SerializedName("OldPassword")
    public String OldPassword;

    @SerializedName("ConfirmPassword")
    public String ConfirmPassword;

    @SerializedName("DeviceToken")
    public String DeviceToken;

    @SerializedName("AccessToken")
    public String AccessToken;

}
