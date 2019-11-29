package com.plexoc.helpma.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MedicineSearch<T> {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("results")
    public List<T> results;

    @SerializedName(("error"))
    public Object error;
}
