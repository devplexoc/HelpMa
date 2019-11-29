package com.plexoc.helpma.Model;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.POST;

public class Article {

    @SerializedName("Id")
    public int Id;

    @SerializedName("Title")
    public String Title;

    @SerializedName("Description")
    public String Description;

    @SerializedName("CreatedAt")
    public String CreatedAt;

    @SerializedName("UpdatedAt")
    public String UpdatedAt;

    @SerializedName("CreatedAtStr")
    public String CreatedAtStr;

    @SerializedName("UpdatedAtStr")
    public String UpdatedAtStr;
}
