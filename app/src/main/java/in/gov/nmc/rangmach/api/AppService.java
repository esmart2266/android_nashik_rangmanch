package in.gov.nmc.rangmach.api;

import java.util.Map;

import in.gov.nmc.rangmach.BuildConfig;
import in.gov.nmc.rangmach.model.AdData;
import in.gov.nmc.rangmach.model.AllOrganizationsList_Data;
import in.gov.nmc.rangmach.model.AllTheater_Data;
import in.gov.nmc.rangmach.model.BookAssets;
import in.gov.nmc.rangmach.model.Booking_Payment_Data;
import in.gov.nmc.rangmach.model.Booking_Penalty_Data;
import in.gov.nmc.rangmach.model.Booking_Refund_Data;
import in.gov.nmc.rangmach.model.CarParkingReport_Data;
import in.gov.nmc.rangmach.model.CarParking_Data;
import in.gov.nmc.rangmach.model.CheckAvailableSlots_Data;
import in.gov.nmc.rangmach.model.CustomerFoodOrders_Data;
import in.gov.nmc.rangmach.model.FoodProduct_Data;
import in.gov.nmc.rangmach.model.LanguagePOJO_Data;
import in.gov.nmc.rangmach.model.LoadSitingData;
import in.gov.nmc.rangmach.model.MyAssetsOrder_Data;
import in.gov.nmc.rangmach.model.NowShowing_Data;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.model.UserResponse;
import in.gov.nmc.rangmach.model.VideoData;
import kotlin.jvm.JvmSuppressWildcards;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

public interface AppService {

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "userregister.php")
    Call<UserData> userregister(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "login")
    Call<UserData> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "logout")
    Call<UserData> logout(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "register_user")
    Call<UserData> regpushnotification(@FieldMap Map<String, String> params);

    @GET("https://script.google.com/macros/s/AKfycbxYcS5D_O3BIsPGadYx2xo_HbjCkNNr1oWp5mpevvrdQovzDnATiSNvvLokZ451NxDTKg/exec")
    Call<LanguagePOJO_Data> languageAPI();

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "verify_otp")
    Call<UserData> verifydevice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "updatebankdetails")
    Call<UserData> updatebankdetails(@FieldMap Map<String, String> params);

    @GET(BuildConfig.API_PATH + "forgetpassword.php")
    Call<UserData> forgetpassword(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "update_profile")
    Call<UserData> updateprofile(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "create_user_org")
    Call<UserData> create_user_org(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "update_user_org")
    Call<UserData> update_user_org(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "book_parking")
    Call<UserData> book_parking(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "place_foodorder")
    Call<UserData> createfoodorder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_ads")
    Call<AdData> alladvertisements(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_events")
    Call<NowShowing_Data> allevents(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_assets")
    Call<AllTheater_Data> allassets(@FieldMap Map<String, String> params);

    @Multipart
    @JvmSuppressWildcards
    @POST(BuildConfig.BASE_URL + BuildConfig.API_PATH + "uploadimage.php")
    Call<UserResponse> uploadimage(@PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_slot")
    Call<CheckAvailableSlots_Data> checkavailableslots(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "booking_assets")
    Call<BookAssets> bookasset(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_user_bookings")
    Call<MyAssetsOrder_Data> myassetsorder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_parkings_slots")
    Call<CarParking_Data> parkingslot(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "parkingreport")
    Call<CarParkingReport_Data> parkingreport(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_customer_foodorder_details")
    Call<CustomerFoodOrders_Data> customerfoodorder(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_foodorder_details")
    Call<FoodProduct_Data> foodorderdetails(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "mypenalty")
    Call<Booking_Penalty_Data> mypanelty(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "myrefunds")
    Call<Booking_Refund_Data> myrefunds(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "mypayments")
    Call<Booking_Payment_Data> mypayments(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_org_by_user")
    Call<AllOrganizationsList_Data> get_org_by_user(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "get_active_org_by_user")
    Call<AllOrganizationsList_Data> get_active_org_by_user(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "confirmrefund")
    Call<UserResponse> confirmrefund(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "booking_cancel_request")
    Call<UserResponse> booking_cancel_request(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "reschedule_request")
    Call<UserResponse> booking_reschedule_request(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "refund_request")
    Call<UserResponse> refund_request(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "videogallery.php")
    Call<VideoData> videogallery(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "loadseatings")
    Call<LoadSitingData> loadseatings(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "bookseats")
    Call<BookAssets> bookseats(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(BuildConfig.API_PATH + "submit_feedback")
    Call<UserData> savefeedback(@FieldMap Map<String, String> params);

//    @FormUrlEncoded
//    @POST(BuildConfig.API_PATH + "blogs.php")
//    Call<BlogData> blogs(@FieldMap Map<String, String> params);
}
