package com.plexoc.helpma.Service;

import com.plexoc.helpma.Model.MedicalDetail;
import com.plexoc.helpma.Model.MedicineSearch;
import com.plexoc.helpma.Model.OpenFda;
import com.plexoc.helpma.Model.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClientMedicineApi {

    @GET("label.json")
    Call<MedicineSearch<Results<OpenFda>>> SearchMedicine(@Query("api_key") String api_key, @Query("search") String search, @Query("limit") int limit);

}
