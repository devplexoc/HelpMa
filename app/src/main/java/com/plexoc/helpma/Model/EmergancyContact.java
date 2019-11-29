package com.plexoc.helpma.Model;

import com.google.gson.annotations.SerializedName;

public class EmergancyContact {

    @SerializedName("Id")
    public int Id;

    @SerializedName("UserId")
    public int UserId;

    @SerializedName("PersonName")
    public String PersonName;

    @SerializedName("PersonEmail")
    public String PersonEmail;

    @SerializedName("PersonMobile")
    public String PersonMobile;

    @SerializedName("PersonRelation")
    public String PersonRelation;

    @SerializedName("CreatedAt")
    public String CreatedAt;

    @SerializedName("CreatedBy")
    public int CreatedBy;

    @SerializedName("UpdatedAt")
    public String UpdatedAt;

    @SerializedName("UpdatedBy")
    public int UpdatedBy;

    @SerializedName("FullName")
    public String FullName;

    @SerializedName("Contact1")
    public String Contact1;

    @SerializedName("Contact2")
    public String Contact2;

    @SerializedName("CreatedAtStr")
    public String CreatedAtStr;

    @SerializedName("UpdatedAtStr")
    public String UpdatedAtStr;

}
