package com.plexoc.helpma.Model;

import com.google.gson.annotations.SerializedName;

public class OpenFda {

    @SerializedName("generic_name")
    public String[] product_ndc;

    @SerializedName("brand_name")
    public String[] brand_name;

    @SerializedName("manufacturer_name")
    public String[] manufacturer_name;

    @SerializedName("substance_name")
    public String[] substance_name;

    @SerializedName("product_type")
    public String[] product_type;

}
