package com.e_sathiuser.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA4wg_rrk:APA91bFaHY59pXuLfvUlQVC9lsnTkw5MyinNWdeFdK-Nu6xCkMloGYUqj7iXQIkwyeUlu9bk2ArqXcmL1O3I2dtlEMioovhgQ_v069ML4OeBu9HRIHb8Xp_6bcz_UITcnZK_QEDAO40A" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

