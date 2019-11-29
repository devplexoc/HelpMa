package com.plexoc.helpma.Model;

import com.google.gson.annotations.SerializedName;

public class MedicalDetail {

    @SerializedName("Id")
    public int Id;

    @SerializedName("UserId")
    public int UserId;

    @SerializedName("Gender")
    public String Gender;

    @SerializedName("BloodGroup")
    public String BloodGroup;

    @SerializedName("Weight")
    public String Weight;

    @SerializedName("Height")
    public String Height;

    @SerializedName("Allergy")
    public String Allergy;

    @SerializedName("Medicines")
    public String Medicines;

    @SerializedName("DoctorName")
    public String DoctorName;

    @SerializedName("DoctorContact")
    public String DoctorContact;

    @SerializedName("DoctorEmail")
    public String DoctorEmail;

    @SerializedName("DoctorAddress")
    public String DoctorAddress;

    @SerializedName("FavoriteFood")
    public String FavoriteFood;

    @SerializedName("Vegan")
    public String Vegan;

    @SerializedName("Diet")
    public String Diet;

    @SerializedName("Hospital")
    public String Hospital;

    @SerializedName("Condition")
    public String Condition;

    @SerializedName("Pregnant")
    public String Pregnant;

    @SerializedName("Health")
    public String Health;

}
