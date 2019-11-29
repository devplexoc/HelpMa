package com.plexoc.helpma.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseCard {

    @SerializedName("stripeCardLists")
    public List<Card> stripeCardLists;
}
