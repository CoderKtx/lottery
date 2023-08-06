package com.raffleclub.app.api;

import com.google.gson.JsonObject;
import com.raffleclub.app.model.AppModel;
import com.raffleclub.app.model.BankModel;
import com.raffleclub.app.model.ConfigurationModel;
import com.raffleclub.app.model.Contest;
import com.raffleclub.app.model.CustomerModel;
import com.raffleclub.app.model.NotificationModel;
import com.raffleclub.app.model.Packages;
import com.raffleclub.app.model.ResponseUpdateNotif;
import com.raffleclub.app.model.Token;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiCalling {

    @Multipart
    @POST("paytmallinone/init_Transaction.php")
    Call<JsonObject> generateTokenCallTest(
            @Part("code") String language,
            @Part("MID") String mid,
            @Part("ORDER_ID") String order_id,
            @Part("AMOUNT") String amount
    );

    //@Multipart
    @FormUrlEncoded
    @POST("paytmallinone/init_Transaction.php")
    Call<Token> generateTokenCall(
            @Field("code") String language,
            @Field("MID") String mid,
            @Field("ORDER_ID") String order_id,
            @Field("AMOUNT") String amount
    );

    //@Multipart
    @FormUrlEncoded
    @POST("paytmallinone/init_Transaction.php")
    Call<JsonObject> generateTokenCallJson(
            @Field("code") String language,
            @Field("MID") String mid,
            @Field("ORDER_ID") String order_id,
            @Field("AMOUNT") String amount
    );

    @FormUrlEncoded
    @POST(ApiConstant.REGISTRATION_USER_WITH_REFER)
    Call<CustomerModel> customerRegistrationWithRefer(
            @Field("access_key") String access_key,
            @Field("name") String name,
            @Field("email") String email,
            @Field("device_id") String device_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("country_code") String country_code,
            @Field("mobile") String mobile,
            @Field("referer") String referer,
            @Field("fcm_token") String fcm_token,
            @Field("birth_date") String birth_date
    );


    @FormUrlEncoded
    @POST(ApiConstant.REGISTRATION_USER_WITHOUT_REFER)
    Call<CustomerModel> customerRegistrationWithoutRefer(
            @Field("access_key") String access_key,
            @Field("name") String name,
            @Field("email") String email,
            @Field("device_id") String device_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("country_code") String country_code,
            @Field("mobile") String mobile,
            @Field("fcm_token") String fcm_token,
            @Field("birth_date") String birth_date
    );

    @FormUrlEncoded
    @POST(ApiConstant.REGISTRATION_USER_WITHOUT_REFER)
    Call<JsonObject> customerRegistrationWithoutReferJson(
            @Field("access_key") String access_key,
            @Field("name") String name,
            @Field("email") String email,
            @Field("device_id") String device_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("country_code") String country_code,
            @Field("mobile") String mobile,
            @Field("fcm_token") String fcm_token,
            @Field("birth_date") String birth_date
    );

    @GET(ApiConstant.LOGIN_USER)
    Call<CustomerModel> loginUser(
            @Query("access_key") String access_key,
            @Query("mobile") String mobile,
            @Query("password") String password);

    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_USER_PROFILE)
    Call<CustomerModel> updateUserProfileDOB(
            @Field("access_key") String access_key,
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("gender") String gender,
            @Field("dob") String dob);

    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_USER_PROFILE)
    Call<CustomerModel> updateUserProfilePassword(
            @Field("access_key") String access_key,
            @Field("id") String id,
            @Field("password") String password);


    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_USER_PROFILE)
    Call<CustomerModel> updateUserProfileFCM(
            @Field("access_key") String access_key,
            @Field("id") String id,
            @Field("fcm_token") String fcm_token);

    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_USER_PHOTO)
    Call<List<Contest>> updateUserPicture(
            @Field("access_key") String access_key,
            @Field("id") String id,
            @Field("user_profile") String image);

    @GET(ApiConstant.VERIFY_MOBILE)
    Call<CustomerModel> verifyUserMobile(
            @Query("access_key") String access_key,
            @Query("mobile") String mobile);

    @GET(ApiConstant.UPDATE_NOTIFICATION)
    Call<JSONObject> updateNotification(
            @Query("access_key") String access_key,
            @Query("notification_id") String notification_id);

    @FormUrlEncoded
    @POST(ApiConstant.FORGOT_PASSWORD)
    Call<CustomerModel> userForgotPassword(
            @Field("access_key") String access_key,
            @Field("mobile") String mobile,
            @Field("password") String password);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_UPCOMING_CONTEST)
    Call<CustomerModel> getUpcomingContest(
            @Query("access_key") String access_key);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_UPCOMING_CONTEST)
    Call<JsonObject> getUpcomingContestJson(
            @Query("access_key") String access_key);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_LIVE_CONTEST)
    Call<CustomerModel> getLiveContest(
            @Query("access_key") String access_key);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_CONTEST_STATUS)
    Call<CustomerModel> getContestStatus(
            @Query("access_key") String access_key);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_PACKAGES)
    Call<List<Packages>> getPackages(
            @Query("access_key") String access_key);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_CONTEST)
    Call<List<Contest>> getContest(
            @Query("access_key") String access_key,
            @Query("contest_id") String contest_id,
            @Query("pkg_id") String pkg_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_PRICE_SLOT)
    Call<List<Contest>> getPriceSlot(
            @Query("access_key") String access_key,
            @Query("fees_id") String fees_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_TICKET_SOLD)
    Call<List<Contest>> getTicketSold(
            @Query("access_key") String access_key,
            @Query("contest_id") String contest_id,
            @Query("fees_id") String fees_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_HISTORY)
    Call<List<Contest>> getHistory(
            @Query("access_key") String access_key,
            @Query("user_id") String user_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_TRANSACTION)
    Call<List<Contest>> getTransactions(
            @Query("access_key") String access_key,
            @Query("user_id") String user_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_MY_TICKET)
    Call<List<Contest>> getMyTicket(
            @Query("access_key") String access_key,
            @Query("user_id") String user_id,
            @Query("contest_id") String contest_id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_MY_RESULTS)
    Call<List<Contest>> getMyResults(
            @Query("access_key") String access_key,
            @Query("user_id") String user_id,
            @Query("contest_id") String contest_id);


    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_USER_PROFILE)
    Call<CustomerModel> getUserProfile(
            @Query("access_key") String access_key,
            @Query("id") String id);

//    @Headers("Cache-Control: no-cache")
//    @GET(ApiConstant.GET_USER_PROFILE)
//    Call<JsonObject> getUserProfile(
//            @Query("access_key") String access_key ,
//            @Query("id") String id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_USER_WALLET)
    Call<CustomerModel> getUserWallet(
            @Query("access_key") String access_key,
            @Query("id") String id);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_BANK_INFO)
    Call<BankModel> getBankInfo(
            @Query("access_key") String access_key,
            @Query("id") String id);


    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_BANK_INFO)
    Call<List<Contest>> updateBankInfo(
            @Field("access_key") String access_key,
            @Field("id") String id,
            @Field("acc_name") String acc_name,
            @Field("acc_no") String acc_no,
            @Field("ifsc_code") String ifsc_code,
            @Field("pan_no") String pan_no,
            @Field("pan_card") String pan_card,
            @Field("proof_copy") String proof_copy,
            @Field("bank_name") String bank_name,
            @Field("acc_status") String acc_status
    );

    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_BANK_INFO)
    Call<List<Contest>> updateBankInfoWithoutProofCopy(
            @Field("access_key") String access_key,
            @Field("id") String id,
            @Field("acc_name") String acc_name,
            @Field("acc_no") String acc_no,
            @Field("ifsc_code") String ifsc_code,
            @Field("pan_no") String pan_no,
            @Field("bank_name") String bank_name,
            @Field("acc_status") String acc_status);


    @GET(ApiConstant.GET_TOP_WINNERS)
    Call<List<Contest>> getTopWinners(
            @Query("access_key") String access_key,
            @Query("contest_id") String contest_id,
            @Query("fees_id") String fees_id);



    @GET(ApiConstant.GET_NOTIFICATIONS)
    Call<List<NotificationModel>> getNotifications(
            @Query("access_key") String access_key);



    @GET(ApiConstant.GET_NOTIFICATION)
    Call<NotificationModel> getNotification(
            @Query("access_key") String access_key,@Query("notification_id") String notificationID);


    @GET(ApiConstant.GET_NOTIFICATION)
    Call<JsonObject> getNotificationJson(
            @Query("access_key") String access_key,@Query("notification_id") String notificationID);



    @FormUrlEncoded
    @POST(ApiConstant.ADD_TICKET)
    Call<CustomerModel> addTicket(
            @Field("access_key") String access_key,
            @Field("id") String id,
            @Field("username") String username,
            @Field("contest_id") String contest_id,
            @Field("fees_id") String fees_id,
            @Field("entry_fee") String entry_fee,
            @Field("ticket_no") String ticket_no);

    @FormUrlEncoded
    @POST(ApiConstant.ADD_TRANSACTION)
    Call<CustomerModel> addWithdrawTransaction(
            @Field("access_key") String access_key,
            @Field("id") String id,
            @Field("request_name") String request_name,
            @Field("req_from") String req_from,
            @Field("req_amount") String req_amount,
            @Field("getway_name") String getway_name);

    @FormUrlEncoded
    @POST(ApiConstant.ADD_TRANSACTION)
    Call<CustomerModel> addDepositTransaction(
            @Field("access_key") String access_key,
            @Field("id") String id,
            @Field("payment_id") String payment_id,
            @Field("req_amount") String req_amount,
            @Field("getway_name") String getway_name);

    @Headers("Cache-Control: no-cache")
    @GET(ApiConstant.GET_APP_DETAILS)
    Call<AppModel> getAppDetails(
            @Query("access_key") String purchase_key);

    @GET(ApiConstant.GET_PRIVACY_POLICY)
    Call<ConfigurationModel> getPrivacyPolicy(
            @Query("access_key") String access_key);

    @GET(ApiConstant.GET_TERMS_CONDITION)
    Call<ConfigurationModel> getTermsCondition(
            @Query("access_key") String access_key);

    @GET(ApiConstant.GET_CONTACT_US)
    Call<ConfigurationModel> getContactUs(
            @Query("access_key") String access_key);

    @GET(ApiConstant.GET_ABOUT_US)
    Call<ConfigurationModel> getAboutUs(
            @Query("access_key") String access_key);
}