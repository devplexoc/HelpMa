package com.plexoc.helpma.Model;

import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("Id")
    public String Id;

    @SerializedName("UserId")
    public int UserId;

    @SerializedName("Last4")
    public String Last4;

    @SerializedName("ExpMonth")
    public String ExpMonth;

    @SerializedName("ExpYear")
    public String ExpYear;

    @SerializedName("DefaultCCId")
    public String DefaultCCId;

    @SerializedName("Brand")
    public String Brand;

}
