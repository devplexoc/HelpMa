package com.plexoc.helpma.Model;

import com.google.gson.annotations.SerializedName;

public class Search {

    @SerializedName("Id")
    public int Id;

    @SerializedName("UserId")
    public int UserId;

    @SerializedName("SearchText")
    public String SearchText;

    @SerializedName("Description")
    public String Description;

    @SerializedName("SideEffects")
    public String SideEffects;
}
