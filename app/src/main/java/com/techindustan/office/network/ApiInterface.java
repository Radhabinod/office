package com.techindustan.office.network;

import com.techindustan.model.employee.Employee;
import com.techindustan.model.login.Login;
import com.techindustan.model.notification.Notification;
import com.techindustan.model.reset.ResetResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by binod on 27/3/18.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("auth/login")
    Call<Login> login(@Field("email") String email, @Field("password") String password, @Field("device_type") String device_type, @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("all/employees")
    Call<Employee> getEmployees(@Field("access_token") String access_token);

    @FormUrlEncoded
    @POST("send/notification")
    Call<Notification> sendMessage(@Field("access_token") String access_token, @Field("venue") String venue, @Field("message") String message, @Field("time") String time, @Field("receiver_id") String receiver_id);

    @FormUrlEncoded
    @POST("signin/send_reset_password_mail")
    Call<ResetResponse> resetPassword(@Field("email") String email, @Field("is_app") String isApp);

    @FormUrlEncoded
    @POST("send/notification")
    Call<Notification> sendReply(@Field("access_token") String access_token, @Field("message") String message, @Field("receiver_id") String receiver_id, @Field("message_type") String type);

}
