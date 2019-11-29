package com.plexoc.helpma.Service;

import com.plexoc.helpma.Model.Article;
import com.plexoc.helpma.Model.BaseCard;
import com.plexoc.helpma.Model.Card;
import com.plexoc.helpma.Model.EmergancyContact;
import com.plexoc.helpma.Model.ListResponse;
import com.plexoc.helpma.Model.MedicalDetail;
import com.plexoc.helpma.Model.Plans;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.Model.Search;
import com.plexoc.helpma.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {

    @POST("user/SignUp")
    @FormUrlEncoded
    Call<Response<User>> SignUp(@Field("FirstName") String FirstName, @Field("LastName") String LastName, @Field("Email") String Email, @Field("Password") String Password,
                                @Field("Contact1") String Contact1, @Field("DeviceToken") String DeviceToken);

    @POST("user/LogIn")
    Call<Response<User>> Login(@Query("Email") String Email, @Query("Password") String Password, @Query("MbDeviceToken") String MbDeviceToken);


    @POST("user/UpdateProfile")
    @FormUrlEncoded
    Call<Response<User>> updateProfile(@Field("Id") int Id, @Field("FirstName") String FirstName, @Field("LastName") String LastName, @Field("Email") String Email, @Field("Contact1") String Contact1,
                                       @Field("Contact2") String Contact2, @Field("Password") String Password, @Field("Dob") String Dob, @Field("AddressLine1") String AddressLine1, @Field("AddressLine2") String AddressLine2,
                                       @Field("City") String City, @Field("State") String State, @Field("Country") String Country);

    @POST("emargencyDetails/UserEmergencyDetailsSelectbyUserId/{UserId}")
    @FormUrlEncoded
    Call<ListResponse<EmergancyContact>> getEmergancyContact(@Path("UserId") int UserId, @Query("Search") String Search, @Field("Offset") int Offset, @Field("Limit") int Limit);

    @POST("emargencyDetails/InsertUpdateUserEmergencyDetails")
    @FormUrlEncoded
    Call<Response<EmergancyContact>> AddEmergancyContact(@Field("UserId") int UserId, @Field("PersonName") String PersonName, @Field("PersonEmail") String PersonEmail, @Field("PersonMobile") String PersonMobile,
                                                         @Field("PersonRelation") String PersonRelation);


    @POST("emargencyDetails/InsertUpdateUserEmergencyDetails")
    @FormUrlEncoded
    Call<Response<EmergancyContact>> EditEmergancyContact(@Field("Id") int Id, @Field("UserId") int UserId, @Field("PersonName") String PersonName, @Field("PersonEmail") String PersonEmail, @Field("PersonMobile") String PersonMobile,
                                                          @Field("PersonRelation") String PersonRelation);

    @POST("emargencyDetails/UserEmergencyDetailsDelete")
    Call<String> DeleteContact(@Query("Id") int Id);

    @POST("article/ArticlesSelectAll")
    @FormUrlEncoded
    Call<ListResponse<Article>> getAricles(@Query("Search") String Search, @Field("Offset") int Offset, @Field("Limit") int Limit);

    @POST("medicalDetails/InsertUpdateUserMedicalDetails")
    @FormUrlEncoded
    Call<Response<MedicalDetail>> insertMedicalDetail(@Field("Id") int Id, @Field("UserId") int UserId, @Field("Gender") String Gender, @Field("BloodGroup") String BloodGroup, @Field("Weight") String Weight,
                                                      @Field("Height") String Height, @Field("Allergy") String Allergy, @Field("Medicines") String Medicines, @Field("DoctorName") String DoctorName,
                                                      @Field("DoctorContact") String DoctorContact, @Field("DoctorEmail") String DoctorEmail, @Field("DoctorAddress") String DoctorAddress, @Field("FavoriteFood") String FavoriteFood,
                                                      @Field("Vegan") String Vegan, @Field("Diet") String Diet, @Field("Hospital") String Hospital, @Field("Condition") String Condition, @Field("Pregnant") String Pregnant,
                                                      @Field("Health") String Health);

    @POST("medicalDetails/UserMedicalDetailsSelectbyUserId/{UserId}")
    @FormUrlEncoded
    Call<ListResponse<MedicalDetail>> getMedicalDetail(@Path("UserId") int UserId, @Field("Offset") int Offset, @Field("Limit") int Limit);

    /*@POST("userCardDetails/UserCardDetailsSelectByUserId")
    @FormUrlEncoded
    Call<ListResponse<Card>> getCardList(@Query("UserId") int UserId, @Field("Offset") int Offset, @Field("Limit") int Limit);*/


    @POST("sOS/CallSOS")
    Call<ListResponse<EmergancyContact>> SOS(@Query("UserId") int UserId, @Query("Text") String Text);

    @POST("searchHistory/InsertSearchHistory")
    @FormUrlEncoded
    Call<ListResponse<Search>> search(@Field("UserId") int UserId, @Field("SearchText") String SearchText);

    @POST("searchHistory/SearchHistorySelectbyUserId/{UserId}")
    @FormUrlEncoded
    Call<ListResponse<Search>> getSearchHistory(@Path("UserId") int UserId, @Field("Offset") int Offset, @Field("Limit") int Limit);

    @POST("searchHistory/GetMedicineDetail")
    Call<Response<Search>> medicineDetail();

    @GET("payment/Subscriptions")
    Call<ListResponse<Plans>> getPlans();

    @GET("payment/GeneratePayment")
    Call<Object> MakePayment(@Query("email") String email, @Query("token") String token, @Query("planid") String planid);

    @GET("payment/ListOfCard")
    Call<BaseCard> getCardList(@Query("StripeId") String StripeId);

    @POST("payment/AddCard")
    Call<Object> addCard(@Query("StripeId") String StripeId, @Query("StripeToken")String StripeToken);

    @POST("payment/SetDefaultCard")
    Call<Object> setDefaultCard(@Query("CardId") String CardId, @Query("StripeId") String StripeId);

    @POST("user/Logout")
    Call<Object> logout(@Query("Id") int Id);

}
