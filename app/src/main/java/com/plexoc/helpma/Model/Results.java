package com.plexoc.helpma.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.material.internal.ParcelableSparseArray;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Results<T> implements Serializable {

    @SerializedName("effective_time")
    public String effective_time;

    @SerializedName("inactive_ingredient")
    public String[] inactive_ingredient;

    @SerializedName("purpose")
    public String[] purpose;

    @SerializedName("when_using")
    public String[] when_using;

    @SerializedName("pregnancy_or_breast_feeding")
    public String[] pregnancy_or_breast_feeding;

    @SerializedName("keep_out_of_reach_of_children")
    public String[] keep_out_of_reach_of_children;

    @SerializedName("adverse_reactions")
    public String[] adverse_reactions;

    @SerializedName("warnings")
    public String[] warnings;

    @SerializedName("ask_doctor")
    public String[] ask_doctor;

    @SerializedName("openfda")
    public T openfda;

    @SerializedName("version")
    public String version;

    @SerializedName("dosage_and_administration")
    public String[] dosage_and_administration;

    @SerializedName("stop_use")
    public String[] stop_use;

    @SerializedName("storage_and_handling")
    public String[] storage_and_handling;

    @SerializedName("do_not_use")
    public String[] do_not_use;

    @SerializedName("indications_and_usage")
    public String[] indications_and_usage;

    @SerializedName("ask_doctor_or_pharmacist")
    public String[] ask_doctor_or_pharmacist;

    @SerializedName("overdosage")
    public String[] overdosage;

    @SerializedName("active_ingredient")
    public String[] active_ingredient;

    @SerializedName("dosage_and_administration_table")
    public String[] dosage_and_administration_table;

}
